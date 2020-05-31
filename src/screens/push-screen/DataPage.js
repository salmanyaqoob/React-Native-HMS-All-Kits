import React from "react";
import {Text, View, Image, StyleSheet} from "react-native";

import {headerStyles} from "../../styles/headerStyles";

import {styles} from "../../styles/styles";

export default class DataPage extends React.Component {
  render() {
    return (
      <View>
        <View style={headerStyles.headerSection}>
          <View style={headerStyles.headerTitleWrapper}>
            <Text style={headerStyles.headerTitle}>Push Data Page</Text>
          </View>
          <View style={headerStyles.headerLogoWrapper}>
            <Image
              style={headerStyles.headerLogo}
              source={require("../../../assets/images/hms-rn-logo.png")}
            />
          </View>
        </View>

        <View style={pageStyles.container}>
          <Text style={styles.paddingX}>
            Example Data:{" "}
            {this.props.navigation.getParam("item", {key: "value"}).key}
          </Text>
        </View>
      </View>
    );
  }
}

export const pageStyles = StyleSheet.create({
  container: {
    display: "flex",
    justifyContent: "space-between",
    height: 70,
    margin: 20,
  },
});
