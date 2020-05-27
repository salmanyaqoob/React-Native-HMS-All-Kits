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

import { ViewPropTypes, View } from 'react-native';

import decorateMapComponent, {
  USES_DEFAULT_IMPLEMENTATION,
  SUPPORTED,
} from './decorateMapComponent';

// if ViewPropTypes is not defined fall back to View.propType (to support RN < 0.44)
const viewPropTypes = ViewPropTypes || View.propTypes;

const propTypes = {
  ...viewPropTypes,

  /**
   * The url template of the tile server. The patterns {minX} {maxX} {minY} {maxY} {width} {height}
   * will be replaced at runtime according to EPSG:900913 specification bounding box.
   * For example, https://demo.geo-solutions.it/geoserver/tiger/wms?service=WMS&version=1.1.0&request=GetMap&layers=tiger:poi&styles=&bbox={minX},{minY},{maxX},{maxY}&width={width}&height={height}&srs=EPSG:900913&format=image/png&transparent=true&format_options=dpi:213
   */
  urlTemplate: PropTypes.string.isRequired,

  /**
   * The order in which this tile overlay is drawn with respect to other overlays. An overlay
   * with a larger z-index is drawn over overlays with smaller z-indices. The order of overlays
   * with the same z-index is arbitrary. The default zIndex is -1.
   *
   * @platform android
   */
  zIndex: PropTypes.number,
  /**
   * The maximum zoom level for this tile overlay.
   *
   */
  maximumZ: PropTypes.number,

  /**
   * The minimum zoom level for this tile overlay.
   *
   */
  minimumZ: PropTypes.number,

  /**
   * tileSize.
   *
   */
  tileSize: PropTypes.number,

  /**
   * opacity. between 0 - 1
   *
   */
  opacity: PropTypes.number,
};

class MapWMSTile extends React.Component {
  render() {
    const AIRMapWMSTile = this.getAirComponent();
    return <AIRMapWMSTile {...this.props} />;
  }
}

MapWMSTile.propTypes = propTypes;
export default decorateMapComponent(MapWMSTile, {
  componentType: 'WMSTile',
});
