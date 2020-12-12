import React, {useState, useCallback, useEffect, useMemo} from "react";
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Image,
  Button,
  TextInput,
} from "react-native";

import {headerStyles} from "../styles/headerStyles";

import {Colors} from "react-native/Libraries/NewAppScreen";

import HMSLocation from '@hmscore/react-native-hms-location';

const Header = () => {
  // Initialize Location Kit
useEffect(() => {
  HMSLocation.LocationKit.Native.init()
      .then(_ => console.log("Done loading"))
      .catch(ex => console.log("Error while initializing." + ex));
}, []);
  return (
  <View>
    <View style={headerStyles.headerSection}>
      <View style={headerStyles.headerTitleWrapper}>
        <Text style={headerStyles.headerTitle}>HMS Location Kit</Text>
      </View>
      <View style={headerStyles.headerLogoWrapper}>
        <Image
          style={headerStyles.headerLogo}
          source={require("../../assets/images/hms-rn-logo.png")}
        />
      </View>
    </View>
  </View>
)};

const LocationTestPage = () => {
  const [loadingLocation, setLoadingLocation] = useState(true);
  const [hasLocationPermission, setHasLocationPermission] = useState(false);
  const [locationAvailable, setLocationAvailable] = useState(false);
  const [locationSettings, setLocationSettings] = useState(false);
  const [locationCoordinates, setLocationCoordinates] = useState();
  const [locationUpdateId, setLocationUpdateId] = useState();
  const [lastknowCheck, setLastknowCheck] = useState(true);

  useEffect(() => {
    // console.log("Check Location useEffect");

    // Check location permissions
    HMSLocation.FusedLocation.Native.hasPermission()
      .then(result => {
        let hasPermission = result.hasPermission === true;
        setHasLocationPermission(hasPermission);
      })
      .catch(ex =>
        console.log("Error while getting location permission info: " + ex),
      );

    if (loadingLocation === true) {
      if (hasLocationPermission === false) {
        requestLocationPermisson();
      }

      if (hasLocationPermission === true && locationSettings === false) {
        checkLocationSettings();
      }

      // if (
      //   hasLocationPermission === true &&
      //   locationSettings === true
      // ) {
      //   getLocationAvailability();
      // }

      if (
        hasLocationPermission === true &&
        locationSettings === true &&
        typeof locationCoordinates === "undefined" &&
        lastknowCheck === true
      ) {
        getLastLocation();
      }

      if (
        hasLocationPermission === true &&
        locationSettings === true &&
        typeof locationCoordinates === "undefined" &&
        lastknowCheck === false
      ) {
        console.log("reach to location update -----------");
        addFusedLocationEventListener();
      }
    }
    // if (
    //   loadingLocation === false &&
    //   typeof locationUpdateId !== "undefined" &&
    //   typeof locationCoordinates !== "undefined" &&
    //   locationUpdateId !== "undefined"
    // ) {
    //   removeFusedLocationEventListener();
    // }
  }, [
    loadingLocation,
    hasLocationPermission,
    locationAvailable,
    lastknowCheck,
    locationSettings,
    locationCoordinates,
    locationUpdateId,
  ]);

  // Request Location Permission
  const requestLocationPermisson = useCallback(() => {
    HMSLocation.FusedLocation.Native.requestPermission()
      .then(res => {
        console.log("location permission response:", res);
        setHasLocationPermission(res);
      })
      .catch(ex => {
        setHasLocationPermission(false);
        console.log("Exception while requestLocationPermisson " + ex);
      });
  }, []);

  // Check Location Settings
  const checkLocationSettings = useCallback(() => {
    // const locationRequest = HMSLocation.FusedLocation.Request.configure({
    //   id: "locationRequest" + Math.random() * 10000,
    //   priority:
    //     HMSLocation.FusedLocation.PriorityConstants.PRIORITY_HIGH_ACCURACY,
    //   interval: 3,
    //   numUpdates: 10,
    //   fastestInterval: 1000.0,
    //   expirationTime: 200000.0,
    //   expirationTimeDuration: 200000.0,
    //   smallestDisplacement: 0.0,
    //   maxWaitTime: 2000000.0,
    //   needAddress: false,
    //   language: "en",
    //   countryCode: "en",
    // }).build();

    const locationRequest = {
      id: "locationRequest" + Math.random() * 10000,
      priority: HMSLocation.FusedLocation.PriorityConstants.PRIORITY_HIGH_ACCURACY,
      interval: 5000,
      numUpdates: 20,
      fastestInterval: 6000,
      expirationTime: 100000,
      expirationTimeDuration: 100000,
      smallestDisplacement: 0,
      maxWaitTime: 1000.0,
      needAddress: false,
      language: "en",
      countryCode: "en",
  };

  const locationSettingsRequest = {
    locationRequests: [locationRequest],
    alwaysShow: false,
    needBle: false,
};

    // const locationSettingsRequest = HMSLocation.FusedLocation.SettingsRequest.configure(
    //   {
    //     locationRequests: [locationRequest],
    //     alwaysShow: false,
    //     needBle: false,
    //   },
    // ).build();

    HMSLocation.FusedLocation.Native.checkLocationSettings(
      locationSettingsRequest,
    )
      .then(res => {
        console.log("locationSettings", res);
        console.log("locationSettings", typeof locationSettings);
        if (
          res === true || (
            res.locationSettingsStates.isGpsPresent == true
          && 
          res.locationSettingsStates.isGpsUsable == true
          && 
          res.locationSettingsStates.isLocationUsable == true
          )
          ) {
            setLocationSettings(true);
        } else {
          setLocationSettings(false);
          setHasLocationPermission(false);
        }
      })
      .catch(ex => {
        console.log("Error while getting location settings. " + ex);
        // setLocationSettings(false);
        // setHasLocationPermission(false);
      });
  });

  // Get Location Availability
  const getLocationAvailability = useCallback(() => {
    HMSLocation.FusedLocation.Native.getLocationAvailability()
      .then(available => {
        console.log("available", typeof available);
        console.log("available", available);
        let hasavailable = available.isLocationAvailable === "true";
        setLocationAvailable(hasavailable);
      })
      .catch(err => {
        setLocationAvailable(false);
        console.log("Failed to get location availability", err);
      });
  }, []);

  // Get Last Location
  const getLastLocation = useCallback(() => {
    HMSLocation.FusedLocation.Native.getLastLocation()
      .then(pos => {
        console.log(pos);
        setLocationCoordinates(pos);
        setLastknowCheck(true);
        setLoadingLocation(false);
      })
      .catch(err => {
        setLastknowCheck(false);
        console.log("Failed to get last location", err);
      });
  }, []);

  // Request Location Update
  const requestLocationUpdate = useCallback(() => {
    const LocationRequest = {
      id: 'e0048e' + Math.random() * 10000,
      priority: HMSLocation.FusedLocation.PriorityConstants.PRIORITY_HIGH_ACCURACY,
      interval: 3,
      numUpdates: 1,
      fastestInterval: 1000.0,
      expirationTime: 1000.0,
      expirationTimeDuration: 1000.0,
      smallestDisplacement: 0.0,
      maxWaitTime: 1000.0,
      needAddress: false,
      language: 'en',
      countryCode: 'en',
  };

    HMSLocation.FusedLocation.Native.requestLocationUpdatesWithCallback(
      LocationRequest,
    )
      .then(({requestCode}) => {
        setLocationUpdateId(requestCode);
        console.log("id", requestCode);
      })
      .catch(ex =>
        console.log("Exception while requestLocationUpdatesWithCallback " + ex),
      );

    // HMSLocation.FusedLocation.Native.getLastLocationWithAddress(
    //   LocationRequest,
    // ).then(res => {
    //   console.log("Adrress: ", res);
    // });
  }, []);

  const handleLocationUpdate = useCallback(location => {
    console.log(location);
    setLocationCoordinates(location.hwLocationList[0]);
  }, []);

  const addFusedLocationEventListener = useCallback(() => {
    requestLocationUpdate();
    HMSLocation.FusedLocation.Events.addFusedLocationEventListener(
      handleLocationUpdate,
    );
    setLoadingLocation(false);
    // setAutoUpdateEnabled(true);
  }, []);

  const removeFusedLocationEventListener = useCallback(() => {
    HMSLocation.FusedLocation.Events.removeFusedLocationEventListener(
      locationUpdateId,
      handleLocationUpdate,
    );
    setLoadingLocation(false);
    // setAutoUpdateEnabled(false);
  }, [locationUpdateId]);

  const hasLocationPermissionText = hasLocationPermission
    ? "Location permissions are granted."
    : "Location permissions are not granted.";

  const locationAvailableText = locationAvailable
    ? "Location is available."
    : "Location is not available.";

  const locationSettingsText = locationSettings
    ? "Location setting is enabled."
    : "Location setting is not enabled.";

  // if (hasLocationPermission === false) {
  //   requestLocationPermisson();
  // }

  return (
    <View>
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          <View style={styles.body}>
            <View style={styles.sectionContainer}>
              <View style={styles.spaceBetweenRow}>
                <Text style={styles.sectionTitle}>Location</Text>
              </View>

              <View style={styles.spaceBetweenRow}>
                <Text style={styles.sectionDescription}>
                  {`Loading: ${loadingLocation}`}
                </Text>
              </View>

              <View style={styles.spaceBetweenRow}>
                <Text style={styles.sectionDescription}>
                  {hasLocationPermissionText}
                </Text>
              </View>

              <View style={styles.spaceBetweenRow}>
                <Text style={styles.sectionDescription}>
                  {locationSettingsText}
                </Text>
              </View>

              <View style={styles.spaceBetweenRow}>
                <Text style={styles.sectionDescription}>
                  {locationAvailableText}
                </Text>
              </View>

              <View style={styles.spaceBetweenRow}>
                <Text
                  style={styles.sectionDescription}>{`locationCoordinates: ${
                  locationCoordinates
                    ? locationCoordinates.latitude +
                      `,` +
                      locationCoordinates.longitude
                    : ""
                }`}</Text>
              </View>
            </View>
          </View>
        </ScrollView>
      </SafeAreaView>
    </View>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: "absolute",
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: "600",
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 16,
    fontWeight: "400",
    color: Colors.dark,
  },
  activityData: {
    marginTop: 8,
    marginLeft: 5,
    fontSize: 16,
    fontWeight: "400",
    color: Colors.dark,
  },
  highlight: {
    fontWeight: "700",
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: "600",
    padding: 4,
    paddingRight: 12,
    textAlign: "right",
  },

  spaceBetweenRow: {flexDirection: "row", justifyContent: "space-between"},
  divider: {
    width: "90%",
    alignSelf: "center",
    height: 1,
    backgroundColor: "grey",
    marginTop: 20,
  },
  boldText: {fontWeight: "bold"},
  centralizeSelf: {alignSelf: "center"},
  centralizeContent: {flexDirection: "row", justifyContent: "center"},
  spaceAroundContent: {flexDirection: "row", justifyContent: "space-around"},
});

export default LocationTestPage;
