import React, {useState, useEffect} from 'react';

import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  NativeModules,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

const Check = () => {
  const [isHMSAvailable, setIsHMSAvailable] = useState(false);
  const [isGMSAvailable, setIsGMSAvailable] = useState(false);

  NativeModules.HMSBase.isHmsAvailable(status => {
    setIsHMSAvailable(status);
  });

  NativeModules.HMSBase.isGmsAvailable(status => {
    setIsGMSAvailable(status);
  });

  return (
    <SafeAreaView style={styles.safeAreaStyle}>
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={styles.scrollView}>
        <View style={styles.rowStyle}>
          <Text>GMS</Text>
          <Text>{isGMSAvailable === true ? 'Available' : 'Not Available'}</Text>
        </View>

        <View style={styles.rowStyle}>
          <Text>HMS</Text>
          <Text>{isHMSAvailable === true ? 'Available' : 'Not Available'}</Text>
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
  TextStyle: {
    fontSize: 24,
    fontWeight: '400',
    color: Colors.dark,
    textAlign: 'center',
  },
  rowStyle: {
    marginTop: 16,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
});

export default Check;
