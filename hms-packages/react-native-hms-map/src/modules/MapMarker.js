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
import {
  ColorPropType,
  StyleSheet,
  Platform,
  NativeModules,
  Animated,
  Image,
  findNodeHandle,
  ViewPropTypes,
  View,
} from 'react-native';

import decorateMapComponent, {
  SUPPORTED,
  USES_DEFAULT_IMPLEMENTATION,
} from './decorateMapComponent';

const viewConfig = {
  uiViewClassName: 'AIR<provider>MapMarker',
  validAttributes: {
    coordinate: true,
  },
};

// if ViewPropTypes is not defined fall back to View.propType (to support RN < 0.44)
const viewPropTypes = ViewPropTypes || View.propTypes;

const propTypes = {
  ...viewPropTypes,

  // TODO(lmr): get rid of these?
  identifier: PropTypes.string,
  reuseIdentifier: PropTypes.string,

  /**
   * The title of the marker. This is only used if the <Marker /> component has no children that
   * are a `<Callout />`, in which case the default callout behavior will be used, which
   * will show both the `title` and the `description`, if provided.
   */
  title: PropTypes.string,

  /**
   * The description of the marker. This is only used if the <Marker /> component has no children
   * that are a `<Callout />`, in which case the default callout behavior will be used,
   * which will show both the `title` and the `description`, if provided.
   */
  description: PropTypes.string,

  /**
   * Test ID for end to end test automation
   */
  testID: PropTypes.string,

  /**
   * A custom image to be used as the marker's icon. Only local image resources are allowed to be
   * used.
   */
  image: PropTypes.any,

  /**
   * Marker icon to render (equivalent to `icon` property of GMSMarker Class).
   * Using this property has an advantage over `image` in term of performance, battery usage...
   * because it doesn't trigger tracksViewChanges.
   * (tracksViewChanges is set to YES by default if iconView is not nil).
   */
  icon: PropTypes.any,

  /**
   * Opacity level of view/image based markers
   */
  opacity: PropTypes.number,

  /**
   * If no custom marker view or custom image is provided, the platform default pin will be used,
   * which can be customized by this color. Ignored if a custom marker is being used.
   */
  pinColor: ColorPropType,

  /**
   * When true, the marker will be pre-selected. Setting this to true allows the user to
   * drag the marker without needing to tap on it once to focus on it.
   */
  isPreselected: PropTypes.bool,

  /**
   * The coordinate for the marker.
   */
  coordinate: PropTypes.shape({
    /**
     * Coordinates for the anchor point of the marker.
     */
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,

  /**
   * Sets the anchor point for the marker.
   *
   * The anchor specifies the point in the icon image that is anchored to the marker's position
   * on the Earth's surface.
   *
   * The anchor point is specified in the continuous space [0.0, 1.0] x [0.0, 1.0], where (0, 0)
   * is the top-left corner of the image, and (1, 1) is the bottom-right corner. The anchoring
   * point in a W x H image is the nearest discrete grid point in a (W + 1) x (H + 1) grid,
   * obtained by scaling the then rounding. For example, in a 4 x 2 image, the anchor point
   * (0.7, 0.6) resolves to the grid point at (3, 1).
   *
   * @platform android
   */
  anchor: PropTypes.shape({
    /**
     * Offset to the callout
     */
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
  }),

  /**
   * Specifies the point in the marker image at which to anchor the callout when it is displayed.
   * This is specified in the same coordinate system as the anchor. See the `andor` prop for more
   * details.
   *
   * The default is the top middle of the image.
   *
   * @platform android
   */
  calloutAnchor: PropTypes.shape({
    /**
     * Offset to the callout
     */
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
  }),

  /**
   * Sets whether this marker should be flat against the map true or a billboard facing the
   * camera false.
   *
   * @platform android
   */
  flat: PropTypes.bool,

  draggable: PropTypes.bool,

  /**
   * Sets whether this marker should track view changes true.
   */

  /**
   * Callback that is called when the user presses on the marker
   */
  onPress: PropTypes.func,

  /**
   * Callback that is called when the user taps the callout view.
   */
  onCalloutPress: PropTypes.func,

  /**
   * Callback that is called when the user initiates a drag on this marker (if it is draggable)
   */
  onDragStart: PropTypes.func,

  /**
   * Callback called continuously as the marker is dragged
   */
  onDrag: PropTypes.func,

  /**
   * Callback that is called when a drag on this marker finishes. This is usually the point you
   * will want to setState on the marker's coordinate again
   */
  onDragEnd: PropTypes.func,
};

const defaultProps = {
  stopPropagation: false,
};

class MapMarker extends React.Component {
  constructor(props) {
    super(props);

    this.showCallout = this.showCallout.bind(this);
    this.hideCallout = this.hideCallout.bind(this);
    this.redrawCallout = this.redrawCallout.bind(this);
    this.animateMarkerToCoordinate = this.animateMarkerToCoordinate.bind(this);
  }

  setNativeProps(props) {
    this.marker.setNativeProps(props);
  }

  showCallout() {
    this._runCommand('showCallout', []);
  }

  hideCallout() {
    this._runCommand('hideCallout', []);
  }

  redrawCallout() {
    this._runCommand('redrawCallout', []);
  }

  animateMarkerToCoordinate(coordinate, duration) {
    this._runCommand('animateMarkerToCoordinate', [
      coordinate,
      duration || 500,
    ]);
  }

  redraw() {
    this._runCommand('redraw', []);
  }

  _getHandle() {
    return findNodeHandle(this.marker);
  }

  _runCommand(name, args) {
    switch (Platform.OS) {
      case 'android':
        NativeModules.UIManager.dispatchViewManagerCommand(
          this._getHandle(),
          this.getUIManagerCommand(name),
          args
        );
        break;
      default:
        break;
    }
  }

  render() {
    let image;
    if (this.props.image) {
      image = Image.resolveAssetSource(this.props.image) || {};
      image = image.uri || this.props.image;
    }

    let icon;
    if (this.props.icon) {
      icon = Image.resolveAssetSource(this.props.icon) || {};
      icon = icon.uri;
    }

    const AIRMapMarker = this.getAirComponent();

    return (
      <AIRMapMarker
        ref={ref => {
          this.marker = ref;
        }}
        {...this.props}
        image={image}
        icon={icon}
        style={[styles.marker, this.props.style]}
        onPress={event => {
          if (this.props.stopPropagation) {
            event.stopPropagation();
          }
          if (this.props.onPress) {
            this.props.onPress(event);
          }
        }}
      />
    );
  }
}

MapMarker.propTypes = propTypes;
MapMarker.defaultProps = defaultProps;
MapMarker.viewConfig = viewConfig;

const styles = StyleSheet.create({
  marker: {
    position: 'absolute',
    backgroundColor: 'transparent',
  },
});

MapMarker.Animated = Animated.createAnimatedComponent(MapMarker);

export default decorateMapComponent(MapMarker, {
  componentType: 'Marker',
});
