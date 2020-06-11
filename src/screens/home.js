import React from "react";
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Image,
  Button,
  ToastAndroid,
} from "react-native";

import {Colors} from "react-native/Libraries/NewAppScreen";

import {headerStyles} from "../styles/headerStyles";

const Home = ({navigation}) => {
  const commingSoon = () => {
    toast("Comming Soon");
  };

  const toast = msg => {
    ToastAndroid.show(msg, ToastAndroid.SHORT);
  };

  const Header = () => (
    <View style={headerStyles.headerSection}>
      <View style={headerStyles.headerTitleWrapper}>
        <Text style={headerStyles.headerTitle}>React Native HMS Kits</Text>
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
    <>
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />

          <View style={styles.body}>
            <View style={styles.appButtonStyle}>
              <Button
                title="HMS / GMS Check"
                onPress={() => {
                  navigation.navigate("Check");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="HMS Location"
                onPress={() => {
                  navigation.navigate("Location");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="Huawei Map"
                onPress={() => {
                  navigation.navigate("Map");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="HMS Push"
                onPress={() => {
                  navigation.navigate("Push");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="Huawei Analytics"
                onPress={() => {
                  navigation.navigate("Analytics");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="Huawei Account"
                onPress={() => {
                  navigation.navigate("Account");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="Huawei Ads"
                onPress={() => {
                  navigation.navigate("Ads");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="HMS Site Kit"
                onPress={() => {
                  navigation.navigate("Site");
                }}
                style={styles.appButton}
              />
            </View>

            <View style={styles.appButtonStyle}>
              <Button
                title="HMS Scan Kit"
                onPress={commingSoon}
                style={styles.appButton}
              />
            </View>
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  safeAreaStyle: {backgroundColor: Colors.white},
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  body: {
    backgroundColor: Colors.white,
    // marginHorizontal: 30,
    padding: 30,
    display: "flex",
  },
  headingTextStyle: {
    marginTop: 8,
    marginBottom: 20,
    fontSize: 24,
    fontWeight: "400",
    color: Colors.dark,
    textAlign: "center",
  },
  appButtonStyle: {
    marginBottom: 16,
  },
});

export default Home;
