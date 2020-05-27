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

import PropTypes from 'prop-types';
import React from 'react';
import { ColorPropType, ViewPropTypes, View } from 'react-native';
import decorateMapComponent, {
  USES_DEFAULT_IMPLEMENTATION,
  SUPPORTED,
} from './decorateMapComponent';

// if ViewPropTypes is not defined fall back to View.propType (to support RN < 0.44)
const viewPropTypes = ViewPropTypes || View.propTypes;

const propTypes = {
  ...viewPropTypes,

  /**
   * The coordinate of the center of the circle
   */
  center: PropTypes.shape({
    /**
     * Coordinates for the center of the circle.
     */
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,

  /**
   * The radius of the circle to be drawn (in meters)
   */
  radius: PropTypes.number.isRequired,

  /**
   * Callback that is called when the user presses on the circle
   */
  onPress: PropTypes.func,

  /**
   * The stroke width to use for the path.
   */
  strokeWidth: PropTypes.number,

  /**
   * The stroke color to use for the path.
   */
  strokeColor: ColorPropType,

  /**
   * The fill color to use for the path.
   */
  fillColor: ColorPropType,

  /**
   * The order in which this tile overlay is drawn with respect to other overlays. An overlay
   * with a larger z-index is drawn over overlays with smaller z-indices. The order of overlays
   * with the same z-index is arbitrary. The default zIndex is 0.
   *
   * @platform android
   */
  zIndex: PropTypes.number,
};

const defaultProps = {
  strokeColor: '#000',
  strokeWidth: 1,
};

class MapCircle extends React.Component {
  setNativeProps(props) {
    this.circle.setNativeProps(props);
  }

  render() {
    const AIRMapCircle = this.getAirComponent();
    return (
      <AIRMapCircle
        {...this.props}
        ref={ref => {
          this.circle = ref;
        }}
      />
    );
  }
}

MapCircle.propTypes = propTypes;
MapCircle.defaultProps = defaultProps;

export default decorateMapComponent(MapCircle, {
  componentType: 'Circle',
});
