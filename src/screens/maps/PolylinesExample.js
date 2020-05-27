/*
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

import React, {Component} from 'react';
import {View, ToastAndroid} from 'react-native';
import {Polyline} from 'react-native-hms-map';

const polylines1 = [
  {latitude: 41.01929, longitude: 28.967267},
  {latitude: 41.016785, longitude: 28.986971},
  {latitude: 41.001917, longitude: 28.978743},
  {latitude: 41.002298, longitude: 28.954132},
];
const polylines2 = [
  {latitude: 41.035243, longitude: 29.026812},
  {latitude: 41.022122, longitude: 29.00653},
  {latitude: 41.00415, longitude: 29.012449},
  {latitude: 41.001699, longitude: 28.978743},
];

export default class PolylinesExample extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isRender: props.isRender,
    };
  }

  toastAndConsole(msg) {
    ToastAndroid.show(msg, ToastAndroid.SHORT);
    console.log(msg);
  }

  render() {
    return (
      <View>
        <Polyline
          coordinates={polylines1}
          strokeWidth={5}
          strokeColor="#F00"
          geodesic
          tappable
          onPress={() => {
            this.toastAndConsole('onPress on polyline #1');
          }}
        />
        <Polyline
          coordinates={polylines2}
          strokeWidth={2}
          strokeColor="#00F"
          geodesic
          tappable
          onPress={() => {
            this.toastAndConsole('onPress on polyline #2');
          }}
        />
      </View>
    );
  }
}
