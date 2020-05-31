import React, {useState, useEffect} from "react";

import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Image,
  NativeModules,
} from "react-native";

import {Colors} from "react-native/Libraries/NewAppScreen";

import {headerStyles} from "../styles/headerStyles";

const Check = () => {
  const [isHMSAvailable, setIsHMSAvailable] = useState(false);
  const [isGMSAvailable, setIsGMSAvailable] = useState(false);

  NativeModules.HMSBase.isHmsAvailable(status => {
    setIsHMSAvailable(status);
  });

  NativeModules.HMSBase.isGmsAvailable(status => {
    setIsGMSAvailable(status);
  });

  const Header = () => (
    <>
      <View style={headerStyles.headerSection}>
        <View style={headerStyles.headerTitleWrapper}>
          <Text style={headerStyles.headerTitle}>HMS / GMS Availability</Text>
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

  return (
    <>
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          <View style={styles.body}>
            <View style={styles.rowStyle}>
              <Text>GMS</Text>
              <Text>
                {isGMSAvailable === true ? "Available" : "Not Available"}
              </Text>
            </View>

            <View style={styles.rowStyle}>
              <Text>HMS</Text>
              <Text>
                {isHMSAvailable === true ? "Available" : "Not Available"}
              </Text>
            </View>
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
  body: {
    backgroundColor: Colors.white,
    padding: 30,
  },
  TextStyle: {
    fontSize: 24,
    fontWeight: "400",
    color: Colors.dark,
    textAlign: "center",
  },
  rowStyle: {
    marginTop: 16,
    flexDirection: "row",
    justifyContent: "space-between",
  },
});

export default Check;
