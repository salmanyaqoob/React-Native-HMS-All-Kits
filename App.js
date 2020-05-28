/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {createStackNavigator} from 'react-navigation-stack';
import {createAppContainer} from 'react-navigation';

import Home from './src/screens/Home';
import LocationPage from './src/screens/Location';
import Check from './src/screens/Check';
import Map from './src/screens/Map';
import Push from './src/screens/Push';
import DataPage from './src/screens/push-screen/DataPage';

const AppNavigator = createStackNavigator(
  {
    Home: {
      screen: Home,
      navigationOptions: {
        headerTitle: 'HMS Kits',
      },
      path: 'Home',
    },
    Location: {
      screen: LocationPage,
      navigationOptions: {
        headerTitle: 'HMS Location Kit',
      },
      path: 'Location',
    },
    Check: {
      screen: Check,
      navigationOptions: {
        headerTitle: 'HMS / GMS Check',
      },
      path: 'Check',
    },
    Map: {
      screen: Map,
      navigationOptions: {
        headerTitle: 'Huawei Map',
      },
      path: 'Map',
    },
    Push: {
      screen: Push,
      navigationOptions: {
        headerTitle: 'Huawei Push',
      },
      path: 'Push',
    },
    DataPage: {
      screen: DataPage,
      navigationOptions: {
        headerTitle: 'Huawei Push Data Page',
      },
      path: 'DataPage',
    },
  },
  {
    initialRouteName: 'Home',
  },
);

const AppContainer = createAppContainer(AppNavigator);

const App = () => {
  return <AppContainer />;
};

export default App;
