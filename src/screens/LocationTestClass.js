import React, { Component, useState, useCallback, useEffect, useMemo } from "react";
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

import { headerStyles } from "../styles/headerStyles";

import { Colors } from "react-native/Libraries/NewAppScreen";

import HMSLocation from '@hmscore/react-native-hms-location';
import DeviceInfo from 'react-native-device-info';

class LocationTestClassPage extends Component {
  constructor(props) {
    super(props);
    this.bindFunctions();

    this.state = {
      loadingLocation: true,
      hasLocationPermission: false,
      locationAvailable: false,
      locationSettings: false,
      locationCoordinates: null,
      locationUpdateId: null,
      lastknowCheck: true,
      hasGms: null,
      hasHms: null,
    };

    DeviceInfo.hasGms().then(status => {
      this.setState({
        hasGms: status,
      });
    });
    DeviceInfo.hasHms().then(status => {
      this.setState({
        hasHms: status,
      });
    });

  }

  componentDidMount() {
    const {
      hasGms,
      hasHms,
    } = this.state;
    if (hasHms && !hasGms) {
      // Initialize Location Kit
      HMSLocation.LocationKit.Native.init()
      .then(_ => console.log("HMSLocation Initialize Done successfully"))
      .catch(ex => console.log("Error while initializing." + ex));
    }
  }

  componentWillUnmount() {
    
  }

  bindFunctions() {

    this.getUserLocation = this.getUserLocation.bind(this);
    this.handleLocationUpdate = this.handleLocationUpdate.bind(this);

   }


  // Request Location Permission
  requestLocationPermisson = () => {
    HMSLocation.FusedLocation.Native.requestPermission()
      .then(res => {
        console.log("location permission response:", res);
        setHasLocationPermission(res);
      })
      .catch(ex => {
        setHasLocationPermission(false);
        console.log("Exception while requestLocationPermisson " + ex);
      });
  };

  // Check Location Settings
  checkLocationSettings = () => {

    
  };

  // Get Location Availability
  getLocationAvailability = () => {
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
  };

  // Get Last Location
  getLastLocation = () => {
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
  };

  // Request Location Update
  requestLocationUpdate = () => {
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
  };

  

  addFusedLocationEventListener = () => {
    requestLocationUpdate();
    HMSLocation.FusedLocation.Events.addFusedLocationEventListener(
      handleLocationUpdate,
    );
    setLoadingLocation(false);
    // setAutoUpdateEnabled(true);
  };

  handleLocationUpdate = location => {
    console.log(location);
    // setLocationCoordinates(location.hwLocationList[0]);
  };

  getUserLocation = async () => {

    // Check location permissions
    HMSLocation.FusedLocation.Native.hasPermission()
    .then(result => {
      let hasPermission = result.hasPermission === true;
      // setHasLocationPermission(hasPermission);

      if(hasPermission){
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
                // setLocationSettings(true);

                HMSLocation.FusedLocation.Native.getLastLocation()
                .then(pos => {
                  console.log("Last Location:"+ JSON.stringify(pos));
                  // setLocationCoordinates(pos);
                  // setLastknowCheck(true);
                  // setLoadingLocation(false);
                })
                .catch(err => {
                  // setLastknowCheck(false);
                  console.log("Failed to get last location", err);
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
                    // setLocationUpdateId(requestCode);
                    console.log("id", requestCode);
                  })
                  .catch(ex =>
                    console.log("Exception while requestLocationUpdatesWithCallback " + ex),
                  );

                  HMSLocation.FusedLocation.Events.addFusedLocationEventListener(
                    location => {
                      console.log(location);
                      // setLocationCoordinates(location.hwLocationList[0]);
                    },
                  );

                });
            } else {
              // setLocationSettings(false);
              // setHasLocationPermission(false);
            }
          })
          .catch(ex => {
            console.log("Error while getting location settings. " + ex);
            // setLocationSettings(false);
            // setHasLocationPermission(false);
          });
      }

    }).catch(ex =>{
      console.log("Error while getting location permission info: " + ex);
      this.requestLocationPermisson();
    }
      
    );

  }


  render() {
    const {
      loadingLocation = true,
      hasLocationPermission = false,
      locationAvailable =  false,
      locationSettings = false,
      locationCoordinates = null,
      locationUpdateId = null,
      lastknowCheck = true,
      hasGms,
      hasHms,
    } = this.state;

    const hasLocationPermissionText = hasLocationPermission
    ? "Location permissions are granted."
    : "Location permissions are not granted.";

  const locationAvailableText = locationAvailable
    ? "Location is available."
    : "Location is not available.";

  const locationSettingsText = locationSettings
    ? "Location setting is enabled."
    : "Location setting is not enabled.";

    return (
      <View>
        <SafeAreaView>
          <ScrollView
            contentInsetAdjustmentBehavior="automatic"
            style={styles.scrollView}>
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
            <View style={styles.body}>
              <View style={styles.sectionContainer}>
                <View style={styles.spaceBetweenRow}>
                  <Text style={styles.sectionTitle}>Location</Text>
                </View>
                <View style={styles.spaceBetweenRow}>
                <Button
                title="Test Location"
                onPress={() => {
                  this.getUserLocation();
                }}
              />
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
                    style={styles.sectionDescription}>{`locationCoordinates: ${locationCoordinates
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

  }

}

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

  spaceBetweenRow: { flexDirection: "row", justifyContent: "space-between" },
  divider: {
    width: "90%",
    alignSelf: "center",
    height: 1,
    backgroundColor: "grey",
    marginTop: 20,
  },
  boldText: { fontWeight: "bold" },
  centralizeSelf: { alignSelf: "center" },
  centralizeContent: { flexDirection: "row", justifyContent: "center" },
  spaceAroundContent: { flexDirection: "row", justifyContent: "space-around" },
});

export default LocationTestClassPage;
