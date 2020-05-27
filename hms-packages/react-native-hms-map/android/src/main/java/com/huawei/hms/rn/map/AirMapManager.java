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

2020.03.05-Changed package name 
2020.03.05-Changed components which are belongs to Google replaced with Huawei
2020.03.05-Changed methods setKmlSrc, setUserLocationPriority, setUserLocationUpdateInterval,
setUserLocationFastestInterval
2020.03.22-Changed code style and indentation issues are fixed
2020.03.23-Changed remove redundant code
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.HuaweiMapOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.MapStyleOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AirMapManager extends ViewGroupManager<AirMapView> {

    private static final String REACT_CLASS = "AIRMap";
    private static final int ANIMATE_TO_REGION = 1;
    private static final int ANIMATE_TO_COORDINATE = 2;
    private static final int ANIMATE_TO_VIEWING_ANGLE = 3;
    private static final int ANIMATE_TO_BEARING = 4;
    private static final int FIT_TO_ELEMENTS = 5;
    private static final int FIT_TO_SUPPLIED_MARKERS = 6;
    private static final int FIT_TO_COORDINATES = 7;
    private static final int SET_MAP_BOUNDARIES = 8;
    private static final int ANIMATE_TO_NAVIGATION = 9; 
    private static final int SET_INDOOR_ACTIVE_LEVEL_INDEX = 10;
    private static final int SET_CAMERA = 11;
    private static final int ANIMATE_CAMERA = 12;
  
    private final Map<String, Integer> MAP_TYPES = MapBuilder.of(
        "standard", HuaweiMap.MAP_TYPE_NORMAL,
        "satellite", HuaweiMap.MAP_TYPE_SATELLITE,
        "hybrid", HuaweiMap.MAP_TYPE_HYBRID,
        "terrain", HuaweiMap.MAP_TYPE_TERRAIN,
        "none", HuaweiMap.MAP_TYPE_NONE
    );
  
    private final Map<String, Integer> MY_LOCATION_PRIORITY = MapBuilder.of(
        "balanced", LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
        "high", LocationRequest.PRIORITY_HIGH_ACCURACY,
        "low", LocationRequest.PRIORITY_LOW_POWER,
        "passive", LocationRequest.PRIORITY_NO_POWER
    );
  
    private final ReactApplicationContext appContext;
    private AirMapMarkerManager markerManager;

    protected HuaweiMapOptions huaweiMapOptions;

    public AirMapManager(ReactApplicationContext context) {
        this.appContext = context;
        this.huaweiMapOptions = new HuaweiMapOptions();
    }
  
    public AirMapMarkerManager getMarkerManager() {
        return this.markerManager;
    }
    public void setMarkerManager(AirMapMarkerManager markerManager) {
        this.markerManager = markerManager;
    }
  
    @Override
    public String getName() {
        return REACT_CLASS;
    }
  
    @Override
    protected AirMapView createViewInstance(ThemedReactContext context) {
        return new AirMapView(context, this.appContext, this, huaweiMapOptions);
    }
  
    private void emitMapError(ThemedReactContext context, String message, String type) {
        WritableMap error = Arguments.createMap();
        error.putString("message", message);
        error.putString("type", type);
      
        context
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onError", error);
    }
  
    @ReactProp(name = "region")
    public void setRegion(AirMapView view, ReadableMap region) {
        view.setRegion(region);
    }
  
    @ReactProp(name = "initialRegion")
    public void setInitialRegion(AirMapView view, ReadableMap initialRegion) {
        view.setInitialRegion(initialRegion);
    }
  
    @ReactProp(name = "camera")
    public void setCamera(AirMapView view, ReadableMap camera) {
        view.setCamera(camera);
    }
  
    @ReactProp(name = "initialCamera")
    public void setInitialCamera(AirMapView view, ReadableMap initialCamera) {
        view.setInitialCamera(initialCamera);
    }
  
    @ReactProp(name = "mapType")
    public void setMapType(AirMapView view, @Nullable String mapType) {
        int typeId = MAP_TYPES.get(mapType);
        view.map.setMapType(typeId);
    }
  
    @ReactProp(name = "customMapStyleString")
    public void setMapStyle(AirMapView view, @Nullable String customMapStyleString) {
        view.map.setMapStyle(new MapStyleOptions(customMapStyleString));
    }
  
    @ReactProp(name = "mapPadding")
    public void setMapPadding(AirMapView view, @Nullable ReadableMap padding) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        double density = (double) view.getResources().getDisplayMetrics().density;
      
        if (padding != null) {
            if (padding.hasKey("left")) {
                left = (int) (padding.getDouble("left") * density);
            }
          
            if (padding.hasKey("top")) {
                top = (int) (padding.getDouble("top") * density);
            }
          
            if (padding.hasKey("right")) {
                right = (int) (padding.getDouble("right") * density);
            }
          
            if (padding.hasKey("bottom")) {
                bottom = (int) (padding.getDouble("bottom") * density);
            }
        }
        view.map.setPadding(left, top, right, bottom);
    }
  
    @ReactProp(name = "showsUserLocation", defaultBoolean = false)
    public void setShowsUserLocation(AirMapView view, boolean showUserLocation) {
        view.setShowsUserLocation(showUserLocation);
    }
  
    @ReactProp(name = "showsMyLocationButton", defaultBoolean = true)
    public void setShowsMyLocationButton(AirMapView view, boolean showMyLocationButton) {
        view.setShowsMyLocationButton(showMyLocationButton);
    }
  
    @ReactProp(name = "toolbarEnabled", defaultBoolean = true)
    public void setToolbarEnabled(AirMapView view, boolean toolbarEnabled) {
        view.setToolbarEnabled(toolbarEnabled);
    }
  
    @ReactProp(name = "handlePanDrag", defaultBoolean = false)
    public void setHandlePanDrag(AirMapView view, boolean handlePanDrag) {
        view.setHandlePanDrag(handlePanDrag);
    }
  
    @ReactProp(name = "showsTraffic", defaultBoolean = false)
    public void setShowTraffic(AirMapView view, boolean showTraffic) {
        view.map.setTrafficEnabled(showTraffic);
    }
  
    @ReactProp(name = "showsBuildings", defaultBoolean = false)
    public void setShowBuildings(AirMapView view, boolean showBuildings) {
        view.map.setBuildingsEnabled(showBuildings);
    }
  
    @ReactProp(name = "showsIndoors", defaultBoolean = false)
    public void setShowIndoors(AirMapView view, boolean showIndoors) {
        view.map.setIndoorEnabled(showIndoors);
    }
  
    @ReactProp(name = "showsIndoorLevelPicker", defaultBoolean = false)
    public void setShowsIndoorLevelPicker(AirMapView view, boolean showsIndoorLevelPicker) {
        view.map.getUiSettings().setIndoorLevelPickerEnabled(showsIndoorLevelPicker);
    }
  
    @ReactProp(name = "showsCompass", defaultBoolean = false)
    public void setShowsCompass(AirMapView view, boolean showsCompass) {
        view.map.getUiSettings().setCompassEnabled(showsCompass);
    }
  
    @ReactProp(name = "scrollEnabled", defaultBoolean = false)
    public void setScrollEnabled(AirMapView view, boolean scrollEnabled) {
        view.map.getUiSettings().setScrollGesturesEnabled(scrollEnabled);
    }
  
    @ReactProp(name = "zoomEnabled", defaultBoolean = false)
    public void setZoomEnabled(AirMapView view, boolean zoomEnabled) {
        view.map.getUiSettings().setZoomGesturesEnabled(zoomEnabled);
    }
  
    @ReactProp(name = "zoomControlEnabled", defaultBoolean = true)
    public void setZoomControlEnabled(AirMapView view, boolean zoomControlEnabled) {
        view.map.getUiSettings().setZoomControlsEnabled(zoomControlEnabled);
    }
  
    @ReactProp(name = "rotateEnabled", defaultBoolean = false)
    public void setRotateEnabled(AirMapView view, boolean rotateEnabled) {
        view.map.getUiSettings().setRotateGesturesEnabled(rotateEnabled);
    }
  
    @ReactProp(name = "cacheEnabled", defaultBoolean = false)
    public void setCacheEnabled(AirMapView view, boolean cacheEnabled) {
        view.setCacheEnabled(cacheEnabled);
    }
  
    @ReactProp(name = "loadingEnabled", defaultBoolean = false)
    public void setLoadingEnabled(AirMapView view, boolean loadingEnabled) {
        view.enableMapLoading(loadingEnabled);
    }
  
    @ReactProp(name = "moveOnMarkerPress", defaultBoolean = true)
    public void setMoveOnMarkerPress(AirMapView view, boolean moveOnPress) {
        view.setMoveOnMarkerPress(moveOnPress);
    }
  
    @ReactProp(name = "loadingBackgroundColor", customType = "Color")
    public void setLoadingBackgroundColor(AirMapView view, @Nullable Integer loadingBackgroundColor) {
        view.setLoadingBackgroundColor(loadingBackgroundColor);
    }
  
    @ReactProp(name = "loadingIndicatorColor", customType = "Color")
    public void setLoadingIndicatorColor(AirMapView view, @Nullable Integer loadingIndicatorColor) {
        view.setLoadingIndicatorColor(loadingIndicatorColor);
    }
  
    @ReactProp(name = "pitchEnabled", defaultBoolean = false)
    public void setPitchEnabled(AirMapView view, boolean pitchEnabled) {
        view.map.getUiSettings().setTiltGesturesEnabled(pitchEnabled);
    }
  
    @ReactProp(name = "minZoomLevel")
    public void setMinZoomLevel(AirMapView view, float minZoomLevel) {
        view.map.setMinZoomPreference(minZoomLevel);
    }
  
    @ReactProp(name = "maxZoomLevel")
    public void setMaxZoomLevel(AirMapView view, float maxZoomLevel) {
        view.map.setMaxZoomPreference(maxZoomLevel);
    }
  
    @Override
    public void receiveCommand(AirMapView view, int commandId, @Nullable ReadableArray args) {
        Integer duration;
        Double lat;
        Double lng;
        Double lngDelta;
        Double latDelta;
        float bearing;
        float angle;
        ReadableMap region;
        ReadableMap camera;
      
        switch (commandId) {
            case SET_CAMERA:
                camera = args.getMap(0);
                view.animateToCamera(camera, 0);
                break;
          
            case ANIMATE_CAMERA:
                camera = args.getMap(0);
                duration = args.getInt(1);
                view.animateToCamera(camera, duration);
                break;
          
            case ANIMATE_TO_NAVIGATION:
                region = args.getMap(0);
                lng = region.getDouble("longitude");
                lat = region.getDouble("latitude");
                LatLng location = new LatLng(lat, lng);
                bearing = (float)args.getDouble(1);
                angle = (float)args.getDouble(2);
                duration = args.getInt(3);
                view.animateToNavigation(location, bearing, angle, duration);
                break;
          
            case ANIMATE_TO_REGION:
                region = args.getMap(0);
                duration = args.getInt(1);
                lng = region.getDouble("longitude");
                lat = region.getDouble("latitude");
                lngDelta = region.getDouble("longitudeDelta");
                latDelta = region.getDouble("latitudeDelta");
                LatLngBounds bounds = new LatLngBounds(
                    new LatLng(lat - latDelta / 2, lng - lngDelta / 2),
                    new LatLng(lat + latDelta / 2, lng + lngDelta / 2)
                );
                view.animateToRegion(bounds, duration);
                break;
          
            case ANIMATE_TO_COORDINATE:
                region = args.getMap(0);
                duration = args.getInt(1);
                lng = region.getDouble("longitude");
                lat = region.getDouble("latitude");
                view.animateToCoordinate(new LatLng(lat, lng), duration);
                break;
          
            case ANIMATE_TO_VIEWING_ANGLE:
                angle = (float)args.getDouble(0);
                duration = args.getInt(1);
                view.animateToViewingAngle(angle, duration);
                break;
          
            case ANIMATE_TO_BEARING:
                bearing = (float)args.getDouble(0);
                duration = args.getInt(1);
                view.animateToBearing(bearing, duration);
                break;
          
            case FIT_TO_ELEMENTS:
                view.fitToElements(args.getBoolean(0));
                break;
              
            case FIT_TO_SUPPLIED_MARKERS:
                view.fitToSuppliedMarkers(args.getArray(0), args.getMap(1), args.getBoolean(2));
                break;
          
            case FIT_TO_COORDINATES:
                view.fitToCoordinates(args.getArray(0), args.getMap(1), args.getBoolean(2));
                break;
          
            case SET_MAP_BOUNDARIES:
                view.setMapBoundaries(args.getMap(0), args.getMap(1));
                break;
          
            case SET_INDOOR_ACTIVE_LEVEL_INDEX:
                view.setIndoorActiveLevelIndex(args.getInt(0));
                break;
        }
    }
  
    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Map<String, String>> map = MapBuilder.of(
            "onMapReady", MapBuilder.of("registrationName", "onMapReady"),
            "onPress", MapBuilder.of("registrationName", "onPress"),
            "onLongPress", MapBuilder.of("registrationName", "onLongPress"),
            "onMarkerPress", MapBuilder.of("registrationName", "onMarkerPress"),
            "onMarkerSelect", MapBuilder.of("registrationName", "onMarkerSelect"),
            "onMarkerDeselect", MapBuilder.of("registrationName", "onMarkerDeselect"),
            "onCalloutPress", MapBuilder.of("registrationName", "onCalloutPress")
        );
      
        map.putAll(MapBuilder.of(
            "onUserLocationChange", MapBuilder.of("registrationName", "onUserLocationChange"),
            "onMarkerDragStart", MapBuilder.of("registrationName", "onMarkerDragStart"),
            "onMarkerDrag", MapBuilder.of("registrationName", "onMarkerDrag"),
            "onMarkerDragEnd", MapBuilder.of("registrationName", "onMarkerDragEnd"),
            "onPanDrag", MapBuilder.of("registrationName", "onPanDrag"),
            "onKmlReady", MapBuilder.of("registrationName", "onKmlReady"),
            "onPoiClick", MapBuilder.of("registrationName", "onPoiClick")
        ));
      
        map.putAll(MapBuilder.of(
            "onIndoorLevelActivated", MapBuilder.of("registrationName", "onIndoorLevelActivated"),
            "onIndoorBuildingFocused", MapBuilder.of("registrationName", "onIndoorBuildingFocused"),
            "onDoublePress", MapBuilder.of("registrationName", "onDoublePress"),
            "onMapLoaded", MapBuilder.of("registrationName", "onMapLoaded")
        ));
      
        return map;
    }
    
    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> map = this.createMap(
            "setCamera", SET_CAMERA,
            "animateCamera", ANIMATE_CAMERA,
            "animateToRegion", ANIMATE_TO_REGION,
            "animateToCoordinate", ANIMATE_TO_COORDINATE,
            "animateToViewingAngle", ANIMATE_TO_VIEWING_ANGLE,
            "animateToBearing", ANIMATE_TO_BEARING,
            "fitToElements", FIT_TO_ELEMENTS,
            "fitToSuppliedMarkers", FIT_TO_SUPPLIED_MARKERS,
            "fitToCoordinates", FIT_TO_COORDINATES,
            "animateToNavigation", ANIMATE_TO_NAVIGATION
        );
      
        map.putAll(MapBuilder.of(
            "setMapBoundaries", SET_MAP_BOUNDARIES,
            "setIndoorActiveLevelIndex", SET_INDOOR_ACTIVE_LEVEL_INDEX
        ));
      
        return map;
    }
  
    public static <K, V> Map<K, V> createMap(
    K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        Map map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        return map;
    }
  
    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new SizeReportingShadowNode();
    }
  
    @Override
    public void addView(AirMapView parent, View child, int index) {
        parent.addFeature(child, index);
    }
  
    @Override
    public int getChildCount(AirMapView view) {
        return view.getFeatureCount();
    }
  
    @Override
    public View getChildAt(AirMapView view, int index) {
        return view.getFeatureAt(index);
    }
  
    @Override
    public void removeViewAt(AirMapView parent, int index) {
        parent.removeFeatureAt(index);
    }
  
    @Override
    public void updateExtraData(AirMapView view, Object extraData) {
        view.updateExtraData(extraData);
    }
  
    void pushEvent(ThemedReactContext context, View view, String name, WritableMap data) {
        context.getJSModule(RCTEventEmitter.class)
            .receiveEvent(view.getId(), name, data);
    }
  
    @Override
    public void onDropViewInstance(AirMapView view) {
        view.doDestroy();
        super.onDropViewInstance(view);
    }
  
}
