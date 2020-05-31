import React, {useState} from "react";
import {
  Button,
  View,
  Text,
  NativeModules,
  StyleSheet,
  Image,
} from "react-native";

import {headerStyles} from "../styles/headerStyles";

const Account = () => {
  const [user, setUser] = useState(null);

  const handleLogin = () => {
    NativeModules.HMSLogin.login().then(
      account => {
        setUser(account);
      },
      (code, message) => {
        console.log(message);
      },
    );
  };

  const handleLogout = () => {
    NativeModules.HMSLogin.logout();
    setUser(null);
  };

  const Login = () => {
    return <Button onPress={handleLogin} title="Login" />;
  };

  const Header = () => (
    <View style={headerStyles.headerSection}>
      <View style={headerStyles.headerTitleWrapper}>
        <Text style={headerStyles.headerTitle}>Huawei Account Kit</Text>
      </View>
      <View style={headerStyles.headerLogoWrapper}>
        <Image
          style={headerStyles.headerLogo}
          source={require("../../assets/images/hms-rn-logo.png")}
        />
      </View>
    </View>
  );

  const Logout = () => {
    const {displayName} = user;
    return (
      <>
        <Text style={styles.text}>{`Welcome ${displayName}`}</Text>
        <Button onPress={handleLogout} title="Logout" />
      </>
    );
  };
  return (
    <>
      <Header />
      <View style={styles.container}>{!user ? <Login /> : <Logout />}</View>
    </>
  );
};

export const styles = StyleSheet.create({
  container: {
    display: "flex",
    justifyContent: "space-between",
    height: 70,
    margin: 20,
  },
  header: {
    textAlign: "center",
    fontSize: 20,
  },
  text: {
    margin: 20,
    fontSize: 16,
  },
});

export default Account;
