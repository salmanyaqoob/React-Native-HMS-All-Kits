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

const Home = ({navigation}) => {
  const commingSoon = () => {
    toast('Comming Soon');
  };

  const toast = msg => {
    ToastAndroid.show(msg, ToastAndroid.SHORT);
  };

  return (
    <SafeAreaView style={styles.safeAreaStyle}>
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={styles.scrollView}>
        <Text style={styles.headingTextStyle}>HMS Kits</Text>

        <View style={styles.appButtonStyle}>
          <Button
            title="HMS / GMS Check"
            onPress={() => {
              navigation.navigate('Check');
            }}
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
            onPress={() => {
              navigation.navigate('Map');
            }}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="HMS Push"
            onPress={() => {
              navigation.navigate('Push');
            }}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="Huawei Analytics"
            onPress={() => {
              navigation.navigate('Analytics');
            }}
            style={styles.appButton}
          />
        </View>

        <View style={styles.appButtonStyle}>
          <Button
            title="Huawei Account"
            onPress={() => {
              navigation.navigate('Account');
            }}
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
