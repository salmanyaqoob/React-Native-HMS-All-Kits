/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from "react";
import {createStackNavigator} from "react-navigation-stack";
import {createAppContainer} from "react-navigation";

import {Colors} from "react-native/Libraries/NewAppScreen";

import Home from "./screens/Home";
import LocationPage from "./screens/Location";
import LocationTestPage from "./screens/LocationTest";
import Check from "./screens/Check";
import Map from "./screens/Map";
import Push from "./screens/Push";
import DataPage from "./screens/push-screen/DataPage";
import Analytics from "./screens/Analytics";
import Account from "./screens/Account";

import AdsPage from "./screens/Ads";

const AppNavigator = createStackNavigator(
  {
    Home: {
      screen: Home,
      navigationOptions: {
        headerTitle: "React Native HMS Kits",
      },
      path: "Home",
    },
    Location: {
      screen: LocationPage,
      navigationOptions: {
        headerTitle: "HMS Location Kit",
      },
      path: "Location",
    },
    LocationTest: {
      screen: LocationTestPage,
      navigationOptions: {
        headerTitle: "HMS Location Test",
      },
      path: "LocationTest",
    },

    Check: {
      screen: Check,
      navigationOptions: {
        headerTitle: "HMS / GMS Check",
      },
      path: "Check",
    },
    Map: {
      screen: Map,
      navigationOptions: {
        headerTitle: "Huawei Map",
      },
      path: "Map",
    },
    Push: {
      screen: Push,
      navigationOptions: {
        headerTitle: "Huawei Push",
      },
      path: "Push",
    },
    DataPage: {
      screen: DataPage,
      navigationOptions: {
        headerTitle: "Huawei Push Data Page",
      },
      path: "DataPage",
    },
    Analytics: {
      screen: Analytics,
      navigationOptions: {
        headerTitle: "Huawei Analytics",
      },
      path: "Analytics",
    },
    Account: {
      screen: Account,
      navigationOptions: {
        headerTitle: "Huawei Account",
      },
      path: "Account",
    },
    Ads: {
      screen: AdsPage,
      navigationOptions: {
        headerTitle: "Huawei Ads",
      },
      path: "Ads",
    },
  },
  {
    initialRouteName: "Home",
    headerMode: "screen",
    defaultNavigationOptions: {
      cardStyle: {backgroundColor: "#FFFFFF"},
    },
  },
);

const AppContainer = createAppContainer(AppNavigator);

const App = () => {
  return <AppContainer />;
};

export default App;
