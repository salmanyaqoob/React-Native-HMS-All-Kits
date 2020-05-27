import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  ToastAndroid,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

// import {NavigationContainer} from '@react-navigation/native';
// import {createStackNavigator} from '@react-navigation/stack';

const Home = ({navigation}) => {
  //   alert(navigation);
  const commingSoon = () => {
    toast('Comming Soon');
  };

  const toast = msg => {
    ToastAndroid.show(msg, ToastAndroid.SHORT);
  };
  //   toast('Home Screen Loads');

  return (
    <SafeAreaView style={styles.safeAreaStyle}>
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={styles.scrollView}>
        <Text style={styles.headingTextStyle}>HMS Kits</Text>

        <View style={styles.appButtonStyle}>
          <Button
            title="HMS / GMS Check"
            onPress={commingSoon}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="HMS Location"
            onPress={() => {
              navigation.navigate('Location');
            }}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="Huawei Map"
            onPress={commingSoon}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="HMS Push"
            onPress={commingSoon}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="Huawei Analytics"
            onPress={commingSoon}
            style={styles.appButton}
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeAreaStyle: {
    flex: 1,
    backgroundColor: Colors.lighter,
    marginHorizontal: 30,
  },
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  headingTextStyle: {
    marginTop: 8,
    marginBottom: 20,
    fontSize: 24,
    fontWeight: '400',
    color: Colors.dark,
    textAlign: 'center',
  },
  appButtonStyle: {
    marginBottom: 16,
  },
});

export default Home;
