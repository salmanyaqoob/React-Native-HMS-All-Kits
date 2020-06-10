import React, {useState, useCallback, useEffect} from "react";
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

import HMSLocation from "react-native-hms-location";

const Header = () => (
  <>
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
  </>
);

const Permissions = () => {
  const [hasLocationPermission, setHasLocationPermission] = useState(false);
  const [
    hasActivityIdentificationPermission,
    setHasActivityIdentificationPermission,
  ] = useState(false);

  useEffect(() => {
    console.log("useEffect");
    // Check location permissions
    HMSLocation.FusedLocation.Native.hasPermission()
      .then(result => setHasLocationPermission(result))
      .catch(ex =>
        console.log("Error while getting location permission info: " + ex),
      );

    // Check ActivityIdentification permissions
    HMSLocation.ActivityIdentification.Native.hasPermission()
      .then(result => setHasActivityIdentificationPermission(result))
      .catch(ex =>
        console.log(
          "Error while getting activity identification permission info: " + ex,
        ),
      );
  });

  const requestLocationPermisson = useCallback(() => {
    HMSLocation.FusedLocation.Native.requestPermission();
  }, []);

  const requestActivityIdentificationPermisson = useCallback(() => {
    HMSLocation.ActivityIdentification.Native.requestPermission();
  }, []);
  const hasLocationPermissionText = hasLocationPermission
    ? "Location permissions are granted."
    : "Location permissions are not granted.";

  const hasActivityIdentificationPermissionText = hasActivityIdentificationPermission
    ? "ActivityIdentification permissions are granted."
    : "ActivityIdentification permissions are not granted.";
  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Permissions</Text>
        </View>
      </View>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Location</Text>
          <Button
            title="Request Permission"
            onPress={requestLocationPermisson}
          />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            {hasLocationPermissionText}
          </Text>
        </View>
      </View>

      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>ActivityIdentification</Text>
          <Button
            title="Request Permission"
            onPress={requestActivityIdentificationPermisson}
          />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            {hasActivityIdentificationPermissionText}
          </Text>
        </View>
      </View>
    </>
  );
};

const LocationAvailability = () => {
  const [locationAvailable, setLocationAvailable] = useState(false);
  const getLocationAvailability = useCallback(() => {
    HMSLocation.FusedLocation.Native.getLocationAvailability()
      .then(x => setLocationAvailable(x))
      .catch(err => console.log("Failed to get location availability", err));
  }, []);

  const locationAvailableText = locationAvailable
    ? "Location is available."
    : "Location is not available.";

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Location Availability</Text>
          <Button title="Check" onPress={getLocationAvailability} />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.centralizeSelf}>{locationAvailableText}</Text>
        </View>
      </View>
    </>
  );
};

const MockLocation = () => {
  const [lat, setLat] = useState("41.3");
  const [long, setLong] = useState("29.1");

  const enableMockLocation = () => {
    HMSLocation.FusedLocation.Native.setMockMode(true)
      .then(res => console.log("Mock mode ", res))
      .catch(err => console.log(err));
  };

  const disableMockLocation = () => {
    HMSLocation.FusedLocation.Native.setMockMode(false)
      .then(res => console.log("Mock mode ", res))
      .catch(err => console.log(err));
  };

  const setMockLocation = () => {
    HMSLocation.FusedLocation.Native.setMockMode(true)
      .then(res => {
        console.log("Mock mode ", res);
      })
      .catch(err => {
        console.log(err);
      });

    HMSLocation.FusedLocation.Native.setMockLocation({
      latitude: parseFloat(lat),
      longitude: parseFloat(long),
    })
      .then(res => {
        console.log("MOCK SET", res);
      })
      .catch(err => {
        console.log(err);
      });
  };

  return (
    <>
      <View style={styles.sectionContainer}>
        <Text style={styles.sectionTitle}>Mock Location</Text>
        <View>
          <TextInput
            style={styles.input}
            placeholder="LAT"
            value={long}
            keyboardType="numeric"
            onChangeText={setLong}
          />
          <TextInput
            style={styles.input}
            placeholder="LONG"
            value={lat}
            onChangeText={setLat}
          />
        </View>
        <View style={styles.spaceAroundContent}>
          <Button title="Set" onPress={setMockLocation} />
          <Button title="Enable" onPress={enableMockLocation} />
          <Button title="Disable" onPress={disableMockLocation} />
        </View>
      </View>
    </>
  );
};

const LocationSettings = () => {
  const [locationSettings, setLocationSettings] = useState();

  const checkLocationSettings = useCallback(() => {
    const locationRequest = HMSLocation.FusedLocation.Request.configure({
      id: "e0048e" + Math.random() * 10000,
      priority:
        HMSLocation.FusedLocation.PriorityConstants.PRIORITY_HIGH_ACCURACY,
      interval: 3,
      numUpdates: 10,
      fastestInterval: 1000.0,
      expirationTime: 200000.0,
      expirationTimeDuration: 200000.0,
      smallestDisplacement: 0.0,
      maxWaitTime: 2000000.0,
      needAddress: true,
      language: "en",
      countryCode: "en",
    }).build();

    const locationSettingsRequest = HMSLocation.FusedLocation.SettingsRequest.configure(
      {
        locationRequests: [locationRequest],
        alwaysShow: false,
        needBle: false,
      },
    ).build();

    HMSLocation.FusedLocation.Native.checkLocationSettings(
      locationSettingsRequest,
    )
      .then(res => setLocationSettings(res))
      .catch(ex => console.log("Error while getting location settings. " + ex));
  });

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Location Settings</Text>
          <Button title="Check" onPress={checkLocationSettings} />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>BLE Present</Text>:{" "}
            {`${locationSettings?.isBlePresent || ""}`} |{" "}
            <Text style={styles.boldText}>BLE Usable</Text>:{" "}
            {`${locationSettings?.isBleUsable || ""}`}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>GPS Present</Text>:{" "}
            {`${locationSettings?.isGpsPresent || ""}`} |{" "}
            <Text style={styles.boldText}>GPS Usable</Text>:{" "}
            {`${locationSettings?.isGpsUsable || ""}`}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>Location Present</Text>:{" "}
            {`${locationSettings?.isLocationPresent || ""}`} |{" "}
            <Text style={styles.boldText}>Location Usable</Text>:{" "}
            {`${locationSettings?.isLocationUsable || ""}`}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>Network Location Present</Text>:{" "}
            {`${locationSettings?.isNetworkLocationPresent || ""}`} |{" "}
            <Text style={styles.boldText}>Network Location Usable</Text>:{" "}
            {`${locationSettings?.isNetworkLocationUsable || ""}`}
          </Text>
        </View>
      </View>
    </>
  );
};

const LocationInfo = () => {
  const [position, setPosition] = useState();
  const [locationUpdateId, setLocationUpdateId] = useState();
  const [autoUpdateEnabled, setAutoUpdateEnabled] = useState(false);

  const getLocation = useCallback(() => {
    HMSLocation.FusedLocation.Native.getLastLocation()
      .then(pos => setPosition(pos))
      .catch(err => console.log("Failed to get last location", err));
  }, []);

  const requestLocationUpdate = useCallback(() => {
    const LocationRequest = HMSLocation.FusedLocation.Request.configure({
      id: "e0048e" + Math.random() * 10000,
      priority:
        HMSLocation.FusedLocation.PriorityConstants.PRIORITY_HIGH_ACCURACY,
      interval: 3,
      numUpdates: 10,
      fastestInterval: 1000.0,
      expirationTime: 200000.0,
      expirationTimeDuration: 200000.0,
      smallestDisplacement: 0.0,
      maxWaitTime: 2000000.0,
      needAddress: true,
      language: "en",
      countryCode: "en",
    }).build();

    HMSLocation.FusedLocation.Native.requestLocationUpdatesWithCallback(
      LocationRequest,
    )
      .then(({id}) => setLocationUpdateId(id))
      .catch(ex =>
        console.log("Exception while requestLocationUpdatesWithCallback " + ex),
      );

    // FIXME:
    HMSLocation.FusedLocation.Native.getLastLocationWithAddress(
      LocationRequest,
    ).then(res => {
      console.log("Adrress: ", res);
    });
  }, []);

  const handleLocationUpdate = useCallback(location => {
    console.log(location);
    setPosition(location);
  }, []);

  const addFusedLocationEventListener = useCallback(() => {
    requestLocationUpdate();
    HMSLocation.FusedLocation.Events.addFusedLocationEventListener(
      handleLocationUpdate,
    );
    setAutoUpdateEnabled(true);
  }, []);

  const removeFusedLocationEventListener = useCallback(() => {
    HMSLocation.FusedLocation.Events.removeFusedLocationEventListener(
      locationUpdateId,
      handleLocationUpdate,
    );
    setAutoUpdateEnabled(false);
  }, [locationUpdateId]);

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Location Info</Text>
          <Button title="Get last location" onPress={getLocation} />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>LAT</Text>: {position?.latitude || 0}{" "}
            | <Text style={styles.boldText}>LONG</Text>:{" "}
            {position?.longitude || 0}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>VERTICAL ACCURACY</Text>:{" "}
            {position?.verticalAccuracyMeters || 0}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>ACCURACY</Text>:{" "}
            {position?.accuracy || 0}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>SPEED</Text>: {position?.speed || 0}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>TIME</Text>: {position?.time || 0}
          </Text>
        </View>
        <View style={styles.centralizeContent}>
          <Button
            title={
              autoUpdateEnabled ? "Disable auto-update" : "Enable auto-update"
            }
            onPress={() => {
              if (autoUpdateEnabled) {
                removeFusedLocationEventListener();
              } else {
                addFusedLocationEventListener();
              }
            }}
          />
        </View>
      </View>
    </>
  );
};

const Geofence = () => {
  const [reqCode, setReqCode] = useState();
  const [geoSubscribed, setGeoSubscribed] = useState(false);
  const [geofenceResponse, setGeofenceResponse] = useState();

  const geofence1 = HMSLocation.Geofence.Builder.configure({
    longitude: 42.0,
    latitude: 29.0,
    radius: 20.0,
    uniqueId: "e00322",
    conversions: 1,
    validContinueTime: 10000.0,
    dwellDelayTime: 10,
    notificationInterval: 1,
  }).build();

  const geofence2 = HMSLocation.Geofence.Builder.configure({
    longitude: 41.0,
    latitude: 27.0,
    radius: 340.0,
    uniqueId: "e00491",
    conversions: 2,
    validContinueTime: 1000.0,
    dwellDelayTime: 10,
    notificationInterval: 1,
  }).build();

  /**
   * Geofence List
   */
  const geofenceRequest = HMSLocation.Geofence.Request.configure({
    geofences: [geofence1, geofence2],
    conversions: 1,
    coordinate: 1,
  }).build();

  const createGeofenceList = useCallback(() => {
    HMSLocation.Geofence.Native.createGeofenceList(
      geofenceRequest.geofences,
      geofenceRequest.conversions,
      geofenceRequest.coordinate,
    )
      .then(res => {
        console.log(res);
        setReqCode(parseInt(res.requestCode));
      })
      .catch(err => {
        console.log(err);
      });
  });

  const deleteGeofenceList = useCallback(reqCode => {
    HMSLocation.Geofence.Native.deleteGeofenceList(reqCode)
      .then(res => {
        console.log(res);
        setReqCode(null);
      })
      .catch(err => console.log("ERROR: GeofenceList deletion failed", err));
  }, []);

  const handleGeofenceEvent = useCallback(geo => {
    console.log("GEOFENCE : ", geo);
    setGeofenceResponse(geo);
  });

  const addGeofenceEventListener = useCallback(() => {
    HMSLocation.Geofence.Events.addGeofenceEventListener(handleGeofenceEvent);
    setGeoSubscribed(true);
  }, []);

  const removeGeofenceEventListener = useCallback(() => {
    HMSLocation.Geofence.Events.removeGeofenceEventListener(
      handleGeofenceEvent,
    );
    setGeoSubscribed(false);
  });

  const geofenceData =
    geofenceResponse &&
    HMSLocation.Geofence.Data.configure(geofenceResponse).build();

  const geofenceLocationData =
    geofenceData && geofenceData.errorCode === 0 ? (
      geofenceData.convertingLocation &&
      geofenceData.convertingLocation.map(loc => (
        <>
          <Text style={styles.boldText}>{"  "}Location Data</Text>
          <View style={styles.spaceBetweenRow}>
            <Text>
              <Text>{"    "}Lat</Text>: {loc?.latitude || 0} | <Text>Long</Text>
              : {loc?.longitude || 0}
            </Text>
          </View>
          <View style={styles.spaceBetweenRow}>
            <Text>
              <Text>{"    "}Vertical Accuracy</Text>:{" "}
              {loc?.verticalAccuracyMeters || 0}
            </Text>
          </View>
          <View style={styles.spaceBetweenRow}>
            <Text>
              <Text>{"    "}Accuracy</Text>: {loc?.accuracy || 0}
            </Text>
          </View>
          <View style={styles.spaceBetweenRow}>
            <Text>
              <Text>{"    "}Speed</Text>: {loc?.speed || 0}
            </Text>
          </View>
          <View style={styles.spaceBetweenRow}>
            <Text>
              <Text>{"    "}Time</Text>: {loc?.time || 0}
            </Text>
          </View>
        </>
      ))
    ) : (
      <>
        <Text style={styles.boldText}>{"  "}Error</Text>
        <View style={styles.spaceBetweenRow}>
          <Text>
            <Text>{"    "}Error Code</Text>: {geofenceData?.errorCode}
          </Text>
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text>
            <Text>{"    "}Message</Text>:{" "}
            {geofenceData?.errorMessage || "Unknown"}
          </Text>
        </View>
      </>
    );

  console.log("Geo Fence Location Data : ", geofenceData);

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Geofence</Text>
        </View>
        <View style={styles.spaceAroundContent}>
          <Button
            title={reqCode ? "Remove Geofence" : "Create Geofence"}
            onPress={() => {
              if (reqCode) {
                deleteGeofenceList(reqCode);
              } else {
                createGeofenceList();
              }
            }}
          />
          <Button
            title={geoSubscribed ? "Unsubscribe" : "Subscribe"}
            onPress={() => {
              if (geoSubscribed) {
                removeGeofenceEventListener();
              } else {
                addGeofenceEventListener();
              }
            }}
          />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>Geofence Request Code</Text>:{" "}
            {`${reqCode || ""}`}
          </Text>
        </View>
        {geofenceData ? (
          <>
            <View style={styles.spaceBetweenRow}>
              <Text style={styles.sectionDescription}>
                <Text style={styles.boldText}>Converting Geofence List</Text>:{" "}
                {`${geofenceData.convertingGeofenceList.map(
                  geo => geo.uniqueId,
                ) || ""}`}
              </Text>
            </View>
            <View>
              <Text style={styles.sectionDescription}>
                <Text style={styles.boldText}>Conversion</Text>:{" "}
                {`${geofenceData.conversion || ""}`}
              </Text>
            </View>
            <View>
              <Text style={styles.sectionDescription}>
                <Text style={styles.boldText}>Converting Location</Text>{" "}
              </Text>
              {geofenceLocationData}
            </View>
          </>
        ) : null}
      </View>
    </>
  );
};

const ActivityIdentification = () => {
  const [idReqCode, setIdReqCode] = useState();

  const [identificationSubscribed, setIdentificationSubscribed] = useState(
    false,
  );

  const [identificationResponse, setIdentificationResponse] = useState();

  // Activity Identification
  const createActivityIdentification = useCallback(() => {
    HMSLocation.ActivityIdentification.Native.createActivityIdentificationUpdates(
      2000,
    )
      .then(res => {
        console.log(res);
        setIdReqCode(res.requestCode);
      })
      .catch(err => console.log("ERROR: Activity identification failed", err));
  }, []);
  const removeActivityIdentification = useCallback(idReqCode => {
    HMSLocation.ActivityIdentification.Native.deleteActivityIdentificationUpdates(
      idReqCode,
    )
      .then(res => {
        console.log(res);
        setIdReqCode(null);
      })
      .catch(err =>
        console.log("ERROR: Activity identification deletion failed", err),
      );
  }, []);

  const handleActivityIdentification = useCallback(act => {
    console.log("ACTIVITY : ", act);
    setIdentificationResponse(act);
  }, []);

  const addActivityIdentificationEventListener = useCallback(() => {
    HMSLocation.ActivityIdentification.Events.addActivityIdentificationEventListener(
      handleActivityIdentification,
    );
    setIdentificationSubscribed(true);
  }, []);

  const removeActivityIdentificationEventListener = useCallback(() => {
    HMSLocation.ActivityIdentification.Events.removeActivityIdentificationEventListener(
      handleActivityIdentification,
    );
    setIdentificationSubscribed(false);
  }, []);

  const identificationDatas =
    identificationResponse &&
    identificationResponse.activityIdentificationDatas &&
    identificationResponse.activityIdentificationDatas.map(idData => (
      <View key={Math.random()}>
        <Text style={styles.sectionDescription}>
          <Text style={styles.boldText}>Activity Data</Text>:{" "}
        </Text>
        <Text style={styles.activityData}>
          <Text>Possibility</Text>: {`${idData.possibility}`} |{" "}
          <Text>Identification Activity</Text>:{" "}
          {`${idData.identificationActivity}`}
        </Text>
      </View>
    ));

  return (
    <>
      <View style={styles.sectionContainer}>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Activity Identification</Text>
        </View>
        <View style={styles.spaceAroundContent}>
          <Button
            title={idReqCode ? "Remove Identification" : "Get Identification"}
            onPress={() => {
              if (idReqCode) {
                removeActivityIdentification(idReqCode);
              } else {
                createActivityIdentification(2000);
              }
            }}
          />
          <Button
            title={identificationSubscribed ? "Unsubscribe" : "Subscribe"}
            onPress={() => {
              if (identificationSubscribed) {
                removeActivityIdentificationEventListener();
              } else {
                addActivityIdentificationEventListener();
              }
            }}
          />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>Activity Request Code</Text>:{" "}
            {`${idReqCode || ""}`}
          </Text>
        </View>
        {identificationDatas ? (
          <View style={styles.spaceBetweenRow}>
            <Text style={styles.sectionDescription}>
              <Text style={styles.boldText}>Time</Text>:{" "}
              {`${identificationResponse?.time || ""}`} |{" "}
              <Text style={styles.boldText}>Elapsed Time</Text>:{" "}
              {`${identificationResponse?.elapsedTimeFromReboot || ""}`}
            </Text>
          </View>
        ) : null}
        {identificationDatas ? (
          <View>
            <Text style={styles.sectionDescription}>
              <Text style={styles.boldText}>Most Activity Data</Text>:{" "}
            </Text>
            <Text style={styles.activityData}>
              <Text>Possibility</Text>:{" "}
              {`${identificationResponse?.mostActivityIdentification
                ?.possibility || ""}`}{" "}
              | <Text>Identification Activity</Text>:{" "}
              {`${identificationResponse?.mostActivityIdentification
                ?.identificationActivity || ""}`}
            </Text>
          </View>
        ) : null}
        {identificationDatas}
      </View>
    </>
  );
};

const ActivityConversion = () => {
  const [convReqCode, setConvReqCode] = useState();
  const [conversionSubscribed, setConversionSubscribed] = useState(false);
  const [conversionResponse, setConversionResponse] = useState();

  // Activity Conversion
  const handleActivityConversion = useCallback(conv => {
    console.log("CONVERSION : ", conv);
    setConversionResponse(conv);
  }, []);

  const createConversionUpdates = useCallback(() => {
    HMSLocation.ActivityIdentification.Native.createActivityConversionUpdates([
      // STILL
      {
        conversionType:
          HMSLocation.ActivityIdentification.ActivityConversions
            .ENTER_ACTIVITY_CONVERSION,
        activityType: HMSLocation.ActivityIdentification.Activities.STILL,
      },
      {
        conversionType:
          HMSLocation.ActivityIdentification.ActivityConversions
            .EXIT_ACTIVITY_CONVERSION,
        activityType: HMSLocation.ActivityIdentification.Activities.STILL,
      },

      // ON FOOT
      {
        conversionType:
          HMSLocation.ActivityIdentification.ActivityConversions
            .ENTER_ACTIVITY_CONVERSION,
        activityType: HMSLocation.ActivityIdentification.Activities.FOOT,
      },
      {
        conversionType:
          HMSLocation.ActivityIdentification.ActivityConversions
            .EXIT_ACTIVITY_CONVERSION,
        activityType: HMSLocation.ActivityIdentification.Activities.FOOT,
      },

      // RUNNING
      {
        conversionType:
          HMSLocation.ActivityIdentification.ActivityConversions
            .ENTER_ACTIVITY_CONVERSION,
        activityType: HMSLocation.ActivityIdentification.Activities.RUNNING,
      },
      {
        conversionType:
          HMSLocation.ActivityIdentification.ActivityConversions
            .EXIT_ACTIVITY_CONVERSION,
        activityType: HMSLocation.ActivityIdentification.Activities.RUNNING,
      },
    ])
      .then(res => {
        console.log(res);
        setConvReqCode(res.requestCode);
      })
      .catch(err =>
        console.log("ERROR: Activity Conversion creation failed", err),
      );
  }, []);

  const deleteConversionUpdates = useCallback(convReqCode => {
    HMSLocation.ActivityIdentification.Native.deleteActivityConversionUpdates(
      convReqCode,
    )
      .then(res => {
        console.log(res);
        setConvReqCode(null);
      })
      .catch(err =>
        console.log("ERROR: Activity Conversion deletion failed", err),
      );
  }, []);

  const addActivityConversionEventListener = useCallback(() => {
    HMSLocation.ActivityIdentification.Events.addActivityConversionEventListener(
      handleActivityConversion,
    );
    setConversionSubscribed(true);
  }, []);

  const removeActivityConversionEventListener = useCallback(() => {
    HMSLocation.ActivityIdentification.Events.removeActivityConversionEventListener(
      handleActivityConversion,
    );
    setConversionSubscribed(false);
  }, []);

  const conversionDatas =
    conversionResponse &&
    conversionResponse.activityConversionDatas &&
    conversionResponse.activityConversionDatas.map(conData => (
      <View key={Math.random()}>
        <Text style={styles.sectionDescription}>
          <Text style={styles.boldText}>Conversion Data</Text>:{" "}
        </Text>
        <Text style={styles.activityData}>
          <Text>Elapsed Time From Reboot</Text>:{" "}
          {`${conData?.elapsedTimeFromReboot || ""}`}
        </Text>
        <Text style={styles.activityData}>
          <Text>Activity Type</Text>: {`${conData?.activityType || ""}`}
        </Text>
        <Text style={styles.activityData}>
          <Text>Conversion Type</Text>: {`${conData?.conversionType || ""}`}
        </Text>
      </View>
    ));

  return (
    <>
      <View style={styles.sectionContainer}>
        {/* Conversion */}
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionTitle}>Conversion Update</Text>
        </View>
        <View style={styles.spaceAroundContent}>
          <Button
            title={convReqCode ? "Remove Update" : "Create Update"}
            onPress={() => {
              if (convReqCode) {
                console.log("CONV REQ CODE BEFORE REMOVAL", convReqCode);
                deleteConversionUpdates(convReqCode);
              } else {
                createConversionUpdates();
              }
            }}
          />
          <Button
            title={conversionSubscribed ? "Unsubscribe" : "Subscribe"}
            onPress={() => {
              if (conversionSubscribed) {
                removeActivityConversionEventListener();
              } else {
                addActivityConversionEventListener();
              }
            }}
          />
        </View>
        <View style={styles.spaceBetweenRow}>
          <Text style={styles.sectionDescription}>
            <Text style={styles.boldText}>Conversion Request Code</Text>:{" "}
            {`${convReqCode || ""}`}
          </Text>
        </View>
        {conversionDatas}
      </View>
    </>
  );
};

const LocationPage = ({navigation}) => {
  return (
    <>
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          <View style={styles.body}>
            <Button
              title="Test Location flow"
              onPress={() => {
                navigation.navigate("LocationTest");
              }}
            />
            <View style={styles.divider} />
            <Permissions />
            <View style={styles.divider} />
            <LocationAvailability />
            <View style={styles.divider} />
            <LocationSettings />
            <View style={styles.divider} />
            <LocationInfo />
            <View style={styles.divider} />
            <MockLocation />
            <View style={styles.divider} />
            <Geofence />
            <View style={styles.divider} />
            <ActivityIdentification />
            <View style={styles.divider} />
            <ActivityConversion />
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
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

export default LocationPage;
