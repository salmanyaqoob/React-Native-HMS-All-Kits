import React from 'react';
import {Text, View, Image} from 'react-native';

import {styles} from '../../styles/styles';

export default class DataPage extends React.Component {
  render() {
    return (
      <View style={styles.imageView}>
        <Image
          source={{
            uri:
              'https://developer.huawei.com/dev_index/img/bbs_en_logo.png?v=123',
          }}
          style={styles.image}
        />
        <Text style={styles.paddingX}>
          Example Data:{' '}
          {this.props.navigation.getParam('item', {key: 'value'}).key}
        </Text>
      </View>
    );
  }
}
