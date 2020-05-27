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
   * An array of coordinates to describe the polygon
   */
  coordinates: PropTypes.arrayOf(
    PropTypes.shape({
      /**
       * Latitude/Longitude coordinates
       */
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
    })
  ),

  /**
   * An array of array of coordinates to describe the polygon holes
   */
  holes: PropTypes.arrayOf(
    PropTypes.arrayOf(
      PropTypes.shape({
        /**
         * Latitude/Longitude coordinates
         */
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      })
    )
  ),

  /**
   * Callback that is called when the user presses on the polygon
   */
  onPress: PropTypes.func,

  /**
   * Boolean to allow a polygon to be tappable and use the
   * onPress function
   */
  tappable: PropTypes.bool,

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

  /**
   * Boolean to indicate whether to draw each segment of the line as a geodesic as opposed to
   * straight lines on the Mercator projection. A geodesic is the shortest path between two
   * points on the Earth's surface. The geodesic curve is constructed assuming the Earth is
   * a sphere.
   *
   */
  geodesic: PropTypes.bool,

};

const defaultProps = {
  strokeColor: '#000',
  strokeWidth: 1,
};

class MapPolygon extends React.Component {
  setNativeProps(props) {
    this.polygon.setNativeProps(props);
  }

  render() {
    const AIRMapPolygon = this.getAirComponent();
    return (
      <AIRMapPolygon
        {...this.props}
        ref={ref => {
          this.polygon = ref;
        }}
      />
    );
  }
}

MapPolygon.propTypes = propTypes;
MapPolygon.defaultProps = defaultProps;

export default decorateMapComponent(MapPolygon, {
  componentType: 'Polygon',
});
