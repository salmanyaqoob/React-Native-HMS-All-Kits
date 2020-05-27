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
import { ViewPropTypes, View, processColor } from 'react-native';
import decorateMapComponent, {
  SUPPORTED,
  USES_DEFAULT_IMPLEMENTATION,
} from './decorateMapComponent';

// if ViewPropTypes is not defined fall back to View.propType (to support RN < 0.44)
const viewPropTypes = ViewPropTypes || View.propTypes;

const propTypes = {
  ...viewPropTypes,

  /**
   * Array of heatmap entries to apply towards density.
   */
  points: PropTypes.arrayOf(
    PropTypes.shape({
      /**
       * Latitude/Longitude coordinates
       */
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
      weight: PropTypes.number,
    })
  ),

  /**
   * The radius of the heatmap points in pixels, between 10 and 50
   * (default 20).
   */
  radius: PropTypes.number,

  /**
   * The opacity of the heatmap (default 0.7).
   */
  opacity: PropTypes.number,

  /**
   * Heatmap gradient configuration.
   */
  gradient: PropTypes.shape({
    /**
     * Colors (one or more) to use for gradient.
     */
    colors: PropTypes.arrayOf(PropTypes.string),
    /**
     * Array of floating point values from 0 to 1 representing
     * where each color starts.
     */
    startPoints: PropTypes.arrayOf(PropTypes.number),
    /**
     * Resolution of color map -- number corresponding to the
     * number of steps colors are interpolated into (default 256).
     */
    colorMapSize: PropTypes.number,
  }),
};

const defaultProps = {
  strokeColor: '#000',
  strokeWidth: 1,
};

class MapHeatmap extends React.Component {
  setNativeProps(props) {
    this.heatmap.setNativeProps(props);
  }

  render() {
    const AIRMapHeatmap = this.getAirComponent();
    let gradient;
    if (this.props.gradient) {
      gradient = Object.assign({}, this.props.gradient);
      gradient.colors = gradient.colors.map(c => processColor(c));
    }
    return (
      <AIRMapHeatmap
        {...this.props}
        gradient={gradient}
        ref={ref => {
          this.heatmap = ref;
        }}
      />
    );
  }
}

MapHeatmap.propTypes = propTypes;
MapHeatmap.defaultProps = defaultProps;

export default decorateMapComponent(MapHeatmap, {
  componentType: 'Heatmap',
});
