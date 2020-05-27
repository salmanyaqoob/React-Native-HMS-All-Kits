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

const AppNavigator = createStackNavigator(
  {
    Home: {
      screen: Home,
      navigationOptions: {
        headerTitle: 'HMS Kits',
      },
      path: 'home',
    },
    Location: {
      screen: LocationPage,
      navigationOptions: {
        headerTitle: 'HMS Location Kit',
      },
      path: 'Location',
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
