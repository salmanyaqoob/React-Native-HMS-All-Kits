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

import MapView, {Animated, MAP_TYPES, ProviderPropType} from './modules/MapView';
import Marker from './modules/MapMarker.js';
import Overlay from './modules/MapOverlay.js';

export {default as Polyline} from './modules/MapPolyline.js';
export {default as Heatmap} from './modules/MapHeatmap.js';
export {default as Polygon} from './modules/MapPolygon.js';
export {default as Circle} from './modules/MapCircle.js';
export {default as UrlTile} from './modules/MapUrlTile.js';
export {default as WMSTile} from './modules/MapWMSTile.js';
export {default as LocalTile} from './modules/MapLocalTile.js';
export {default as Callout} from './modules/MapCallout.js';
export {default as CalloutSubview} from './modules/MapCalloutSubview.js';
export {default as AnimatedRegion} from './modules/AnimatedRegion.js';
export {default as Geojson} from './modules/Geojson.js';

export {Marker, Overlay};
export {Animated, MAP_TYPES, ProviderPropType};

export const PROVIDER_HUAWEI = MapView.PROVIDER_HUAWEI;
export const PROVIDER_DEFAULT = MapView.PROVIDER_DEFAULT;

export const MarkerAnimated = Marker.Animated;
export const OverlayAnimated = Overlay.Animated;

export default MapView;
