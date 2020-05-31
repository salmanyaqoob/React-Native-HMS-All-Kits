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

import {StyleSheet} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';

export const headerStyles = StyleSheet.create({
  headerSection: {
    height: 180,
    width: '100%',
    backgroundColor: Colors.lighter,
  },
  headerTitleWrapper: {
    position: 'absolute',
    justifyContent: 'center',
    top: 0,
    bottom: 0,
    right: 0,
    left: 20,
  },
  headerTitle: {fontSize: 17, fontWeight: '700', color: '#5FD8FF'},
  headerLogoWrapper: {alignItems: 'flex-end', justifyContent: 'center'},
  headerLogo: {height: 160, width: 160, marginRight: 10, marginTop: 10},
});
