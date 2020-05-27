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
