import React, {useState} from "react";
import haInterface from "react-native-ha-interface";
import {
  Button,
  View,
  Text,
  ToastAndroid,
  StyleSheet,
  Image,
  NativeModules,
} from "react-native";

import {headerStyles} from "../styles/headerStyles";

const Analytics = () => {
  const [isHmsAvailable, setIsHmsAvailable] = useState(false);
  function checkHmsAvailability() {
    NativeModules.HMSBase.isHmsAvailable(availability => {
      setIsHmsAvailable(availability);
    });
  }
  checkHmsAvailability();

  const sendEvent = () => {
    /*
     * You can trigger firebase or hms analytics conditionally.
     *
     * Assume that we imported analytics
     * from @react-native-firebase/analytics;
     *
     * isHmsAvailable
     *   ? analytics.logEvent(eventName, object)
     *   : haInterface.onEvent(eventName, object)
     *
     */
    haInterface.onEvent("testEvent", {
      testString: "TestValue",
      testInt: 20,
      testDouble: 2.2,
      testBoolean: false,
    });
    alert("Test Event Sent!");
  };

  const getAppInstanceID = () => {
    haInterface.getAAID().then(aaid => alert(`AAID: ${aaid}`));
  };

  const Header = () => (
    <View style={headerStyles.headerSection}>
      <View style={headerStyles.headerTitleWrapper}>
        <Text style={headerStyles.headerTitle}>Huawei Analytics Kit</Text>
      </View>
      <View style={headerStyles.headerLogoWrapper}>
        <Image
          style={headerStyles.headerLogo}
          source={require("../../assets/images/hms-rn-logo.png")}
        />
      </View>
    </View>
  );

  return (
    <View>
      <Header />
      <View style={styles.container}>
        {/* <Text style={styles.header}>{`isHmsAvailable: ${isHmsAvailable}`}</Text> */}

        <View style={styles.buttonContainer}>
          <Button onPress={sendEvent} title="Send Analytics Event" />
          <Button onPress={getAppInstanceID} title="Get AAID" />
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    display: "flex",
    height: 200,
  },
  buttonContainer: {
    justifyContent: "space-between",
    height: 80,
    margin: 20,
  },
  header: {
    textAlign: "center",
    fontSize: 20,
  },
});

export default Analytics;
