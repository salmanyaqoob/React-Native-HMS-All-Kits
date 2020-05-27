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
import {View, Text, ToastAndroid} from 'react-native';
import {Marker, Callout} from 'react-native-hms-map';

import {styles} from './styles';

import CustomImage from '../../../assets/images/marker.png';

export default class MarkersExample extends Component {
  constructor(props) {
    super(props);
    this.ref1 = React.createRef();
    this.ref2 = React.createRef();
    this.state = {
      isRender: props.isRender,
    };
  }

  showCallout() {
    this.ref2.current.showCallout();
  }
  toastAndConsole(msg) {
    ToastAndroid.show(msg, ToastAndroid.SHORT);
    console.log(msg);
  }

  render() {
    return (
      <View>
        <Marker
          ref={this.ref1}
          coordinate={{latitude: 41.0083, longitude: 28.9784}}
          title={'Marker Title #1'}
          description={'Marker Desc #2'}
          pinColor={'#0000ff'}
          anchor={{x: 1, y: 1}}
          opacity={1}
          onPress={e => this.toastAndConsole('onPress #1')}
          onCalloutPress={() => this.toastAndConsole('onCalloutPress #1')}
          onDragStart={() => this.toastAndConsole('onDragStart #1')}
          onDrag={() => this.toastAndConsole('onDrag #1')}
          onDragEnd={() => this.toastAndConsole('onDragEnd #1')}
        />
        <Marker
          ref={this.ref2}
          coordinate={{
            latitude: 41.022231,
            longitude: 29.008118,
          }}
          title={'Marker Title #2'}
          description={'Marker Desc #2'}
          pinColor={'#ff0000'}
          //anchor={{x: 2, y: 6}}
          opacity={1}
          onPress={e => {
            this.toastAndConsole('onPress #2');
            this.showCallout();
          }}
          onCalloutPress={() => this.toastAndConsole('onCalloutPress #2')}
          onDragStart={() => this.toastAndConsole('onDragStart #2')}
          onDrag={() => this.toastAndConsole('onDrag #2')}
          onDragEnd={() => this.toastAndConsole('onDragEnd #2')}>
          <Callout>
            <Text style={styles.color}>This is Callout</Text>
          </Callout>
        </Marker>
        <Marker
          ref={this.ref1}
          coordinate={{
            latitude: 41.005784,
            longitude: 28.997364,
          }}
          title={'Marker Title #3'}
          description={'Marker Desc #3'}
          image={CustomImage}
          opacity={0.8}
          draggable
          onPress={e => this.toastAndConsole('onPress #3')}
          onCalloutPress={() => this.toastAndConsole('onCalloutPress #3')}
          onDragStart={() => this.toastAndConsole('onDragStart #3')}
          onDrag={() => this.toastAndConsole('onDrag #3')}
          onDragEnd={() => this.toastAndConsole('onDragEnd #3')}>
          <Callout style={styles.plainView} tooltip={true}>
            <View>
              <Text>This is Callout</Text>
            </View>
          </Callout>
        </Marker>
        <Marker
          coordinate={{
            latitude: 41.028435,
            longitude: 28.988186,
          }}
          title={'Marker Title #4'}
          description={'Marker Desc #4'}
          pinColor={'#00ff00'}
          //anchor={{x: 2, y: 6}}
          draggable
          opacity={1}
          onPress={e => {
            this.toastAndConsole('onPress #4');
          }}
          onCalloutPress={() => this.toastAndConsole('onCalloutPress #4')}
          onDragStart={() => this.toastAndConsole('onDragStart #4')}
          onDrag={() => this.toastAndConsole('onDrag #4')}
          onDragEnd={() => this.toastAndConsole('onDragEnd #4')}>
          <Callout>
            <Text style={styles.color}>This is Callout</Text>
          </Callout>
        </Marker>
      </View>
    );
  }
}
