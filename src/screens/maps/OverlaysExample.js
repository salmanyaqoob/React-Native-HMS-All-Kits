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
import {Overlay} from 'react-native-hms-map';

const image1 =
  'https://huaweimobileservices.com/wp-content/uploads/2020/02/appgallerynew-400x400.png';
const image2 =
  'https://huaweimobileservices.com/wp-content/uploads/2018/04/hlogo.png';

export default class OverlaysExample extends Component {
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
        <Overlay
          bounds={[
            [41.018545, 28.966149],
            [41.002243, 28.986878],
          ]}
          image={image1}
          tappable
          onPress={() => {
            this.toastAndConsole('onPress on overlay #1');
          }}
        />
        <Overlay
          bounds={[
            [41.04188, 29.03804],
            [41.033163, 28.98669],
          ]}
          image={image2}
          tappable
          onPress={() => {
            this.toastAndConsole('onPress on overlay #2');
          }}
        />
      </View>
    );
  }
}
