/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import React, {useState, useEffect} from "react";
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Image,
  Button,
  ToastAndroid,
  TextInput,
} from "react-native";

import {Colors} from "react-native/Libraries/NewAppScreen";

import {headerStyles} from "../styles/headerStyles";

import {EventRegister} from "react-native-event-listeners";

import RNHMSSite from "react-native-hms-site";

const Header = () => (
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
);

const toast = val => {
  ToastAndroid.show(val, ToastAndroid.SHORT);
};

const config = {
  apiKey:
    "CV72aTZfhM2EtIcxjVNG2WuXbKCmZIdxpJF1hI+LUyhqT2DTr4h2hRXaLI0nEnrNXpNXygRFIAXaT/ipF7UTM4656qN/",
};

RNHMSSite.initializeService(config)
  .then(() => {
    console.log("Service is initialized successfully");
    EventRegister.emit("logsEvent", "Service is initialized successfully");
  })
  .catch(err => {
    console.log("Error : " + err);
    EventRegister.emit(
      "logsEvent",
      "Service is initialized Error : " + JSON.stringify(err),
    );
  });

const SitePage = ({listener}) => {
  let siteLogs = "";
  const [logData, setLogData] = useState();
  const [address, setAddress] = useState("");

  useEffect(() => {
    listener = EventRegister.addEventListener("logsEvent", data => {
      setLogs(data);
    });
  }, [logData]);

  const clearLog = () => {
    toast("Clear Logs");
    // EventRegister.emit("logsEvent", "awsome");
    siteLogs = "";
    console.log("start clearLog");
    setLogData(siteLogs);
  };

  const setLogs = msg => {
    siteLogs = logData + msg + "\n";
    setLogData(siteLogs);
  };

  const onTextSearch = () => {
    let textSearchReq = {
      query: address,
      //   location: {
      //     lat: 48.893478,
      //     lng: 2.334595,
      //   },
      radius: 1000,
      countryCode: "SA",
      language: "en",
      pageIndex: 1,
      pageSize: 5,
    };
    RNHMSSite.textSearch(textSearchReq)
      .then(res => {
        EventRegister.emit(
          "logsEvent",
          "Search Address: " + JSON.stringify(res),
        );
        console.log(JSON.stringify(res));
      })
      .catch(err => {
        EventRegister.emit("logsEvent", "Error: " + JSON.stringify(err));
      });
  };

  const onDetailSearch = () => {
    let detailSearchReq = {
      siteId: "45F1003307A7A6F51330AD4F5C0C3DC0",
      language: "en",
    };
    RNHMSSite.detailSearch(detailSearchReq)
      .then(res => {
        EventRegister.emit(
          "logsEvent",
          "Search Detail: " + JSON.stringify(res),
        );
        console.log(JSON.stringify(res));
      })
      .catch(err => {
        EventRegister.emit("logsEvent", "Error: " + JSON.stringify(err));
      });
  };

  const onQuerySuggestion = () => {
    let querySuggestionReq = {
      query: address,
      //   location: {
      //     lat: 48.893478,
      //     lng: 2.334595,
      //   },
      radius: 1000,
      countryCode: "SA",
      language: "en",
      poiTypes: [
        RNHMSSite.LocationType.ADDRESS,
        RNHMSSite.LocationType.GEOCODE,
      ],
    };
    RNHMSSite.querySuggestion(querySuggestionReq)
      .then(res => {
        EventRegister.emit(
          "logsEvent",
          "Query Suggestion: " + JSON.stringify(res),
        );
        console.log(JSON.stringify(res));
      })
      .catch(err => {
        EventRegister.emit("logsEvent", "Error: " + JSON.stringify(err));
      });
  };

  const onNearbySearch = () => {
    let nearbySearchReq = {
      query: address,
      location: {
        lat: 24.613374,
        lng: 46.728183,
      },
      radius: 1000,
      poiType: RNHMSSite.LocationType.ADDRESS,
      countryCode: "SA",
      language: "en",
      pageIndex: 1,
      pageSize: 5,
    };
    RNHMSSite.nearbySearch(nearbySearchReq)
      .then(res => {
        EventRegister.emit(
          "logsEvent",
          "Near By Search: " + JSON.stringify(res),
        );
        console.log(JSON.stringify(res));
      })
      .catch(err => {
        EventRegister.emit("logsEvent", "Error: " + JSON.stringify(err));
      });
  };

  return (
    <View>
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          <View style={styles.body}>
            <View>
              <TextInput
                placeholder="Input Address"
                style={{
                  height: 40,
                  borderColor: "gray",
                  borderWidth: 1,
                  marginBottom: 10,
                }}
                onChangeText={text => setAddress(text)}
                value={address}
              />
            </View>

            <View>
              <Button title="Text Search" onPress={onTextSearch} />
            </View>

            <View style={styles.btnContainer}>
              <Button title="Detail Search" onPress={onDetailSearch} />
            </View>

            <View style={styles.btnContainer}>
              <Button title="Query Suggestion" onPress={onQuerySuggestion} />
            </View>

            <View style={styles.btnContainer}>
              <Button title="Nearby Search" onPress={onNearbySearch} />
            </View>

            <View style={styles.spaceBetweenRow}>
              <Text style={styles.headingTextStyle}>Logs</Text>
              <Button title="ClearLog" onPress={clearLog}>
                ClearLog
              </Button>
            </View>

            <ScrollView>
              {logData == "" ? null : <Text>{logData}</Text>}
            </ScrollView>
          </View>
        </ScrollView>
      </SafeAreaView>
    </View>
  );
};

const styles = StyleSheet.create({
  btnContainer: {
    marginTop: 20,
  },
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  body: {
    backgroundColor: Colors.white,
    // marginHorizontal: 30,
    padding: 30,
    display: "flex",
  },
  spaceBetweenRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 10,
  },
  headingTextStyle: {
    paddingTop: 7,
    fontSize: 16,
    fontWeight: "400",
    color: Colors.dark,
    textAlign: "center",
  },
});

export default SitePage;
