/*Copyright (c) 2015 Airbnb

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

2020.03.05-modified according to Huawei Map
Huawei Technologies Co., Ltd.
*/

import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { View, StyleSheet, Image, Animated } from 'react-native';

import decorateMapComponent, {
  SUPPORTED,
  USES_DEFAULT_IMPLEMENTATION,
} from './decorateMapComponent';

const viewConfig = {
  uiViewClassName: 'AIR<provider>MapOverlay',
  validAttributes: {
    image: true,
  },
};

const propTypes = {
  ...View.propTypes,
  // A custom image to be used as overlay.
  image: PropTypes.any.isRequired,
  // Top left and bottom right coordinates for the overlay
  bounds: PropTypes.arrayOf(PropTypes.array.isRequired).isRequired,
  /* Boolean to allow an overlay to be tappable and use the
   * onPress function
   */
  tappable: PropTypes.bool,
  // Callback that is called when the user presses on the overlay
  onPress: PropTypes.func,
};

class MapOverlay extends Component {
  render() {
    let image;
    if (this.props.image) {
      if (
        typeof this.props.image.startsWith === 'function' &&
        this.props.image.startsWith('http')
      ) {
        image = this.props.image;
      } else {
        image = Image.resolveAssetSource(this.props.image) || {};
        image = image.uri;
      }
    }

    const AIRMapOverlay = this.getAirComponent();

    return (
      <AIRMapOverlay
        {...this.props}
        image={image}
        style={[styles.overlay, this.props.style]}
      />
    );
  }
}

MapOverlay.propTypes = propTypes;
MapOverlay.viewConfig = viewConfig;

const styles = StyleSheet.create({
  overlay: {
    position: 'absolute',
    backgroundColor: 'transparent',
  },
});

MapOverlay.Animated = Animated.createAnimatedComponent(MapOverlay);

export default decorateMapComponent(MapOverlay, {
  componentType: 'Overlay',
});
