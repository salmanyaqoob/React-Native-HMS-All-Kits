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
2020.03.05-Changed contructor AirMapView
2020.03.05-Changed methods hasPermissions, addFeature, removeFeatureAt, setKmlSrc, getMarkerMap
2020.03.22-Changed code style and indentation issues are fixed
Huawei Technologies Co., Ltd.
*/


package com.huawei.hms.rn.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;

import androidx.core.content.PermissionChecker;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.location.Location;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.HuaweiMapOptions;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.Projection;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.GroundOverlay;
import com.huawei.hms.maps.model.IndoorBuilding;
import com.huawei.hms.maps.model.IndoorLevel;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.PointOfInterest;
import com.huawei.hms.maps.model.Polygon;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.TileOverlay;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class AirMapView extends MapView implements HuaweiMap.InfoWindowAdapter, HuaweiMap.OnMarkerDragListener,
    OnMapReadyCallback, HuaweiMap.OnPoiClickListener, HuaweiMap.OnIndoorStateChangeListener {
    public HuaweiMap map;
    private ProgressBar mapLoadingProgressBar;
    private RelativeLayout mapLoadingLayout;
    private ImageView cacheImageView;
    private Boolean isMapLoaded = false;
    private Integer loadingBackgroundColor = null;
    private Integer loadingIndicatorColor = null;
    private final int baseMapPadding = 50;
  
    private LatLngBounds boundsToMove;
    private CameraUpdate cameraToSet;
    private boolean showUserLocation = false;
    private boolean handlePanDrag = false;
    private boolean moveOnMarkerPress = true;
    private boolean cacheEnabled = false;
    private boolean initialRegionSet = false;
    private boolean initialCameraSet = false;
    private LatLngBounds cameraLastIdleBounds;
    private int cameraMoveReason = 0;
  
    private static final String[] PERMISSIONS = new String[] { "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION" };
  
    private final List<AirMapFeature> features = new ArrayList<>();
    private final Map<Marker, AirMapMarker> markerMap = new HashMap<>();
    private final Map<Polyline, AirMapPolyline> polylineMap = new HashMap<>();
    private final Map<Polygon, AirMapPolygon> polygonMap = new HashMap<>();
    private final Map<GroundOverlay, AirMapOverlay> overlayMap = new HashMap<>();
    private final GestureDetectorCompat gestureDetector;
    private final AirMapManager manager;
    private LifecycleEventListener lifecycleListener;
    private boolean paused = false;
    private boolean destroyed = false;
    private final ThemedReactContext context;
    private final EventDispatcher eventDispatcher;
  
    private ViewAttacherGroup attacherGroup;
  
    private static boolean contextHasBug(Context context) {
        return context == null || context.getResources() == null || context.getResources().getConfiguration() == null;
    }
  
    private static Context getNonBuggyContext(ThemedReactContext reactContext, ReactApplicationContext appContext) {
        Context superContext = reactContext;
        if (!contextHasBug(appContext.getCurrentActivity())) {
            superContext = appContext.getCurrentActivity();
        } else if (contextHasBug(superContext)) {
            if (!contextHasBug(reactContext.getCurrentActivity())) {
                superContext = reactContext.getCurrentActivity();
            } else if (!contextHasBug(reactContext.getApplicationContext())) {
                superContext = reactContext.getApplicationContext();
            } 
        }
        return superContext;
    }
  
    public AirMapView(ThemedReactContext reactContext, ReactApplicationContext appContext, AirMapManager manager,
        HuaweiMapOptions huaweiMapOptions) {
        super(getNonBuggyContext(reactContext, appContext), huaweiMapOptions);
      
        this.manager = manager;
        this.context = reactContext;
      
        super.onCreate(null);
        super.onResume();
        super.getMapAsync(this);
      
        final AirMapView view = this;
      
        gestureDetector = new GestureDetectorCompat(reactContext, new GestureDetector.SimpleOnGestureListener() {
          
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (handlePanDrag) {
                    onPanDrag(e2);
                }
                return false;
            }
          
            @Override
            public boolean onDoubleTap(MotionEvent ev) {
                onDoublePress(ev);
                return false;
            }
        });
      
        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                    int oldRight, int oldBottom) {
                if (!paused) {
                    AirMapView.this.cacheView();
                }
            }
        });
      
        eventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
      
        attacherGroup = new ViewAttacherGroup(context);
        LayoutParams attacherLayoutParams = new LayoutParams(0, 0);
        attacherLayoutParams.width = 0;
        attacherLayoutParams.height = 0;
        attacherLayoutParams.leftMargin = 99999999;
        attacherLayoutParams.topMargin = 99999999;
        attacherGroup.setLayoutParams(attacherLayoutParams);
        addView(attacherGroup);
    }
  
    @Override
    public void onMapReady(final HuaweiMap map) {
        if (destroyed) {
            return;
        }
        this.map = map;
        this.map.setInfoWindowAdapter(this);
        this.map.setOnMarkerDragListener(this);
        this.map.setOnPoiClickListener(this);
        this.map.setOnIndoorStateChangeListener(this);
      
        manager.pushEvent(context, this, "onMapReady", new WritableNativeMap());
      
        final AirMapView view = this;
      
        map.setOnMarkerClickListener(new HuaweiMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                WritableMap event;
                AirMapMarker airMapMarker = getMarkerMap(marker);
              
                event = makeClickEventData(marker.getPosition());
                event.putString("action", "marker-press");
                event.putString("id", airMapMarker.getIdentifier());
                manager.pushEvent(context, view, "onMarkerPress", event);
              
                event = makeClickEventData(marker.getPosition());
                event.putString("action", "marker-press");
                event.putString("id", airMapMarker.getIdentifier());
                manager.pushEvent(context, airMapMarker, "onPress", event);
              
                if (view.moveOnMarkerPress) {
                    return false;
                } else {
                    marker.showInfoWindow();
                    return true;
                }
            }
        });
      
        map.setOnPolygonClickListener(new HuaweiMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                WritableMap event = makeClickEventData(polygon.getPoints().get(0));
                event.putString("action", "polygon-press");
                manager.pushEvent(context, polygonMap.get(polygon), "onPress", event);
            }
        });
      
        map.setOnPolylineClickListener(new HuaweiMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                WritableMap event = makeClickEventData(polyline.getPoints().get(0));
                event.putString("action", "polyline-press");
                manager.pushEvent(context, polylineMap.get(polyline), "onPress", event);
            }
        });
      
        map.setOnInfoWindowClickListener(new HuaweiMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                WritableMap event;
              
                event = makeClickEventData(marker.getPosition());
                event.putString("action", "callout-press");
                manager.pushEvent(context, view, "onCalloutPress", event);
              
                event = makeClickEventData(marker.getPosition());
                event.putString("action", "callout-press");
                AirMapMarker markerView = getMarkerMap(marker);
                manager.pushEvent(context, markerView, "onCalloutPress", event);
              
                event = makeClickEventData(marker.getPosition());
                event.putString("action", "callout-press");
                AirMapCallout infoWindow = markerView.getCalloutView();
                if (infoWindow != null) {
                    manager.pushEvent(context, infoWindow, "onPress", event);
                }
            }
        });
      
        map.setOnMapClickListener(new HuaweiMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                WritableMap event = makeClickEventData(point);
                event.putString("action", "press");
                manager.pushEvent(context, view, "onPress", event);
            }
        });
      
        map.setOnMapLongClickListener(new HuaweiMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                WritableMap event = makeClickEventData(point);
                event.putString("action", "long-press");
                manager.pushEvent(context, view, "onLongPress", makeClickEventData(point));
            }
        });
      
        map.setOnGroundOverlayClickListener(new HuaweiMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                WritableMap event = makeClickEventData(groundOverlay.getPosition());
                event.putString("action", "overlay-press");
                manager.pushEvent(context, overlayMap.get(groundOverlay), "onPress", event);
            }
        });
      
        map.setOnCameraMoveStartedListener(new HuaweiMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                cameraMoveReason = reason;
            }
        });
      
        map.setOnCameraMoveListener(new HuaweiMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                cameraLastIdleBounds = null;
                eventDispatcher.dispatchEvent(new RegionChangeEvent(getId(), bounds, true));
            }
        });
      
        map.setOnCameraIdleListener(new HuaweiMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                if ((cameraMoveReason != 0)
                    && ((cameraLastIdleBounds == null) || LatLngBoundsUtils.boundsAreDifferent(bounds, cameraLastIdleBounds))) {
                    cameraLastIdleBounds = bounds;
                    eventDispatcher.dispatchEvent(new RegionChangeEvent(getId(), bounds, false));
                }
            }
        });
      
        map.setOnMapLoadedCallback(new HuaweiMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                isMapLoaded = true;
                manager.pushEvent(context, view, "onMapLoaded", new WritableNativeMap());
                AirMapView.this.cacheView();
            }
        });
      
        lifecycleListener = new LifecycleEventListener() {
            @Override
            public void onHostResume() {
                if (hasPermissions()) {
                    map.setMyLocationEnabled(showUserLocation);
                }
                synchronized (AirMapView.this) {
                    if (!destroyed) {
                        AirMapView.this.onResume();
                    }
                    paused = false;
                }
            }
          
            @Override
            public void onHostPause() {
                if (hasPermissions()) {
                    map.setMyLocationEnabled(false);
                }
                synchronized (AirMapView.this) {
                    if (!destroyed) {
                        AirMapView.this.onPause();
                    }
                    paused = true;
                }
            }
          
            @Override
            public void onHostDestroy() {
                AirMapView.this.doDestroy();
            }
        };
      
        context.addLifecycleEventListener(lifecycleListener);
    }
  
    private boolean hasPermissions() {
        return checkSelfPermission(getContext(), PERMISSIONS[0]) == PermissionChecker.PERMISSION_GRANTED
            || checkSelfPermission(getContext(), PERMISSIONS[1]) == PermissionChecker.PERMISSION_GRANTED;
    }
  
    public synchronized void doDestroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
      
        if (lifecycleListener != null && context != null) {
            context.removeLifecycleEventListener(lifecycleListener);
            lifecycleListener = null;
        }
        if (!paused) {
            onPause();
            paused = true;
        }
        onDestroy();
    }
  
    public void setInitialRegion(ReadableMap initialRegion) {
        if (!initialRegionSet && initialRegion != null) {
            setRegion(initialRegion);
            initialRegionSet = true;
        }
    }
  
    public void setInitialCamera(ReadableMap initialCamera) {
        if (!initialCameraSet && initialCamera != null) {
            setCamera(initialCamera);
            initialCameraSet = true;
        }
    }
  
    public void setRegion(ReadableMap region) {
        if (region == null) {
            return;
        }
      
        Double lng = region.getDouble("longitude");
        Double lat = region.getDouble("latitude");
        Double lngDelta = region.getDouble("longitudeDelta");
        Double latDelta = region.getDouble("latitudeDelta");
        LatLngBounds bounds = new LatLngBounds(new LatLng(lat - latDelta / 2, lng - lngDelta / 2), // southwest
            new LatLng(lat + latDelta / 2, lng + lngDelta / 2) // northeast
        );
        if (super.getHeight() <= 0 || super.getWidth() <= 0) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
            boundsToMove = bounds;
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
            boundsToMove = null;
        }
    }
  
    public void setCamera(ReadableMap camera) {
        if (camera == null) {
            return;
        }
      
        CameraPosition.Builder builder = new CameraPosition.Builder();
      
        ReadableMap center = camera.getMap("center");
        if (center != null) {
            Double lng = center.getDouble("longitude");
            Double lat = center.getDouble("latitude");
            builder.target(new LatLng(lat, lng));
        }
      
        builder.tilt((float) camera.getDouble("pitch"));
        builder.bearing((float) camera.getDouble("heading"));
        builder.zoom(camera.getInt("zoom"));
      
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(builder.build());
      
        if (super.getHeight() <= 0 || super.getWidth() <= 0) {
            cameraToSet = update;
        } else {
            map.moveCamera(update);
            cameraToSet = null;
        }
    }
  
    public void setShowsUserLocation(boolean showUserLocation) {
        this.showUserLocation = showUserLocation;
        if (hasPermissions()) {
            map.setMyLocationEnabled(showUserLocation);
        }
    }
  
    public void setShowsMyLocationButton(boolean showMyLocationButton) {
        if (hasPermissions() || !showMyLocationButton) {
            map.getUiSettings().setMyLocationButtonEnabled(showMyLocationButton);
        }
    }
  
    public void setToolbarEnabled(boolean toolbarEnabled) {
        if (hasPermissions() || !toolbarEnabled) {
            map.getUiSettings().setMapToolbarEnabled(toolbarEnabled);
        }
    }
  
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        this.cacheView();
    }
  
    public void enableMapLoading(boolean loadingEnabled) {
        if (loadingEnabled && !this.isMapLoaded) {
            this.getMapLoadingLayoutView().setVisibility(View.VISIBLE);
        }
    }
  
    public void setMoveOnMarkerPress(boolean moveOnPress) {
        this.moveOnMarkerPress = moveOnPress;
    }
  
    public void setLoadingBackgroundColor(Integer loadingBackgroundColor) {
        this.loadingBackgroundColor = loadingBackgroundColor;
      
        if (this.mapLoadingLayout != null) {
            if (loadingBackgroundColor == null) {
                this.mapLoadingLayout.setBackgroundColor(Color.WHITE);
            } else {
                this.mapLoadingLayout.setBackgroundColor(this.loadingBackgroundColor);
            }
        }
    }
  
    public void setLoadingIndicatorColor(Integer loadingIndicatorColor) {
        this.loadingIndicatorColor = loadingIndicatorColor;
        if (this.mapLoadingProgressBar != null) {
            Integer color = loadingIndicatorColor;
            if (color == null) {
                color = Color.parseColor("#606060");
            }
          
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList progressTintList = ColorStateList.valueOf(loadingIndicatorColor);
                ColorStateList secondaryProgressTintList = ColorStateList.valueOf(loadingIndicatorColor);
                ColorStateList indeterminateTintList = ColorStateList.valueOf(loadingIndicatorColor);
              
                this.mapLoadingProgressBar.setProgressTintList(progressTintList);
                this.mapLoadingProgressBar.setSecondaryProgressTintList(secondaryProgressTintList);
                this.mapLoadingProgressBar.setIndeterminateTintList(indeterminateTintList);
            } else {
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    mode = PorterDuff.Mode.MULTIPLY;
                }
                if (this.mapLoadingProgressBar.getIndeterminateDrawable() != null) {
                    this.mapLoadingProgressBar.getIndeterminateDrawable().setColorFilter(color, mode);
                }
                if (this.mapLoadingProgressBar.getProgressDrawable() != null) {
                    this.mapLoadingProgressBar.getProgressDrawable().setColorFilter(color, mode);
                }
            }
        }
    }
  
    public void setHandlePanDrag(boolean handlePanDrag) {
        this.handlePanDrag = handlePanDrag;
    }
  
    public void addFeature(View child, int index) {
        if (child instanceof AirMapMarker) {
            AirMapMarker annotation = (AirMapMarker) child;
            annotation.addToMap(map);
            features.add(index, annotation);
          
            int visibility = annotation.getVisibility();
            annotation.setVisibility(INVISIBLE);
          
            ViewGroup annotationParent = (ViewGroup) annotation.getParent();
            if (annotationParent != null) {
                annotationParent.removeView(annotation);
            }
        
            attacherGroup.addView(annotation);
          
            annotation.setVisibility(visibility);
          
            Marker marker = (Marker) annotation.getFeature();
            markerMap.put(marker, annotation);
        } else if (child instanceof AirMapPolyline) {
            AirMapPolyline polylineView = (AirMapPolyline) child;
            polylineView.addToMap(map);
            features.add(index, polylineView);
            Polyline polyline = (Polyline) polylineView.getFeature();
            polylineMap.put(polyline, polylineView);
        } else if (child instanceof AirMapPolygon) {
            AirMapPolygon polygonView = (AirMapPolygon) child;
            polygonView.addToMap(map);
            features.add(index, polygonView);
            Polygon polygon = (Polygon) polygonView.getFeature();
            polygonMap.put(polygon, polygonView);
        } else if (child instanceof AirMapCircle) {
            AirMapCircle circleView = (AirMapCircle) child;
            circleView.addToMap(map);
            features.add(index, circleView);
        } else if (child instanceof AirMapUrlTile) {
            AirMapUrlTile urlTileView = (AirMapUrlTile) child;
            urlTileView.addToMap(map);
            features.add(index, urlTileView);
        } else if (child instanceof AirMapWMSTile) {
            AirMapWMSTile urlTileView = (AirMapWMSTile) child;
            urlTileView.addToMap(map);
            features.add(index, urlTileView);
        } else if (child instanceof AirMapLocalTile) {
            AirMapLocalTile localTileView = (AirMapLocalTile) child;
            localTileView.addToMap(map);
            features.add(index, localTileView);
        } else if (child instanceof AirMapOverlay) {
            AirMapOverlay overlayView = (AirMapOverlay) child;
            overlayView.addToMap(map);
            features.add(index, overlayView);
            GroundOverlay overlay = (GroundOverlay) overlayView.getFeature();
            overlayMap.put(overlay, overlayView);
        } else if (child instanceof ViewGroup) {
            ViewGroup children = (ViewGroup) child;
            for (int i = 0; i < children.getChildCount(); i++) {
                addFeature(children.getChildAt(i), index);
            }
        } else {
            addView(child, index);
        }
    }
  
    public int getFeatureCount() {
        return features.size();
    }
  
    public View getFeatureAt(int index) {
        return features.get(index);
    }
  
    public void removeFeatureAt(int index) {
        AirMapFeature feature = features.remove(index);
        if (feature instanceof AirMapMarker) {
            markerMap.remove(feature.getFeature());
        }
        feature.removeFromMap(map);
    }
  
    public WritableMap makeClickEventData(LatLng point) {
        WritableMap event = new WritableNativeMap();
      
        WritableMap coordinate = new WritableNativeMap();
        coordinate.putDouble("latitude", point.latitude);
        coordinate.putDouble("longitude", point.longitude);
        event.putMap("coordinate", coordinate);
      
        Projection projection = map.getProjection();
        Point screenPoint = projection.toScreenLocation(point);
      
        WritableMap position = new WritableNativeMap();
        position.putDouble("x", screenPoint.x);
        position.putDouble("y", screenPoint.y);
        event.putMap("position", position);
      
        return event;
    }
  
    public void updateExtraData(Object extraData) {
        if (boundsToMove != null) {
            HashMap<String, Float> data = (HashMap<String, Float>) extraData;
            int width = data.get("width") == null ? 0 : data.get("width").intValue();
            int height = data.get("height") == null ? 0 : data.get("height").intValue();
          
            if (width <= 0 || height <= 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsToMove, 0));
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsToMove, width, height, 0));
            }
          
            boundsToMove = null;
            cameraToSet = null;
        } else if (cameraToSet != null) {
            map.moveCamera(cameraToSet);
            cameraToSet = null;
        }
    }
  
    public void animateToCamera(ReadableMap camera, int duration) {
        if (map == null) {
            return;
        }
        CameraPosition.Builder builder = new CameraPosition.Builder(map.getCameraPosition());
        if (camera.hasKey("zoom")) {
            builder.zoom((float) camera.getDouble("zoom"));
        }
        if (camera.hasKey("heading")) {
            builder.bearing((float) camera.getDouble("heading"));
        }
        if (camera.hasKey("pitch")) {
            builder.tilt((float) camera.getDouble("pitch"));
        }
        if (camera.hasKey("center")) {
            ReadableMap center = camera.getMap("center");
            builder.target(new LatLng(center.getDouble("latitude"), center.getDouble("longitude")));
        }
      
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(builder.build());
      
        if (duration <= 0) {
            map.moveCamera(update);
        } else {
            map.animateCamera(update, duration, null);
        }
    }
  
    public void animateToNavigation(LatLng location, float bearing, float angle, int duration) {
        if (map == null) {
            return;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder(map.getCameraPosition()).bearing(bearing).tilt(angle)
            .target(location).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, null);
    }
  
    public void animateToRegion(LatLngBounds bounds, int duration) {
        if (map == null) {
            return;
        }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0), duration, null);
    }
  
    public void animateToViewingAngle(float angle, int duration) {
        if (map == null) {
            return;
        }
      
        CameraPosition cameraPosition = new CameraPosition.Builder(map.getCameraPosition()).tilt(angle).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, null);
    }
  
    public void animateToBearing(float bearing, int duration) {
        if (map == null) {
            return;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder(map.getCameraPosition()).bearing(bearing).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, null);
    }
  
    public void animateToCoordinate(LatLng coordinate, int duration) {
        if (map == null) {
            return;
        }
        map.animateCamera(CameraUpdateFactory.newLatLng(coordinate), duration, null);
    }
  
    public void fitToElements(boolean animated) {
        if (map == null) {
            return;
        }
      
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
      
        boolean addedPosition = false;
      
        for (AirMapFeature feature : features) {
            if (feature instanceof AirMapMarker) {
                Marker marker = (Marker) feature.getFeature();
                builder.include(marker.getPosition());
                addedPosition = true;
            }
        }
        if (addedPosition) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, baseMapPadding);
            if (animated) {
                map.animateCamera(cu);
            } else {
                map.moveCamera(cu);
            }
        }
    }
  
    public void fitToSuppliedMarkers(ReadableArray markerIDsArray, ReadableMap edgePadding, boolean animated) {
        if (map == null) {
            return;
        }
      
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
      
        String[] markerIDs = new String[markerIDsArray.size()];
        for (int i = 0; i < markerIDsArray.size(); i++) {
            markerIDs[i] = markerIDsArray.getString(i);
        }
      
        boolean addedPosition = false;
      
        List<String> markerIDList = Arrays.asList(markerIDs);
      
        for (AirMapFeature feature : features) {
            if (feature instanceof AirMapMarker) {
                String identifier = ((AirMapMarker) feature).getIdentifier();
                Marker marker = (Marker) feature.getFeature();
                if (markerIDList.contains(identifier)) {
                    builder.include(marker.getPosition());
                    addedPosition = true;
                }
            }
        }
      
        if (addedPosition) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, baseMapPadding);
          
            if (edgePadding != null) {
                map.setPadding(edgePadding.getInt("left"), edgePadding.getInt("top"), edgePadding.getInt("right"),
                    edgePadding.getInt("bottom"));
            }
          
            if (animated) {
                map.animateCamera(cu);
            } else {
                map.moveCamera(cu);
            }
        }
    }
  
    public void fitToCoordinates(ReadableArray coordinatesArray, ReadableMap edgePadding, boolean animated) {
        if (map == null) {
            return;
        }
      
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
      
        for (int i = 0; i < coordinatesArray.size(); i++) {
            ReadableMap latLng = coordinatesArray.getMap(i);
            Double lat = latLng.getDouble("latitude");
            Double lng = latLng.getDouble("longitude");
            builder.include(new LatLng(lat, lng));
        }
      
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, baseMapPadding);
      
        if (edgePadding != null) {
            map.setPadding(edgePadding.getInt("left"), edgePadding.getInt("top"), edgePadding.getInt("right"),
                edgePadding.getInt("bottom"));
        }
      
        if (animated) {
            map.animateCamera(cu);
        } else {
            map.moveCamera(cu);
        }
        map.setPadding(0, 0, 0, 0);
    }
  
    public double[][] getMapBoundaries() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        LatLng northEast = bounds.northeast;
        LatLng southWest = bounds.southwest;
      
        return new double[][] { { northEast.longitude, northEast.latitude }, { southWest.longitude, southWest.latitude } };
    }
  
    public void setMapBoundaries(ReadableMap northEast, ReadableMap southWest) {
        if (map == null) {
            return;
        }
      
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
      
        Double latNE = northEast.getDouble("latitude");
        Double lngNE = northEast.getDouble("longitude");
        builder.include(new LatLng(latNE, lngNE));
      
        Double latSW = southWest.getDouble("latitude");
        Double lngSW = southWest.getDouble("longitude");
        builder.include(new LatLng(latSW, lngSW));
      
        LatLngBounds bounds = builder.build();
      
        map.setLatLngBoundsForCameraTarget(bounds);
    }
  
  
    @Override
    public View getInfoWindow(Marker marker) {
        AirMapMarker markerView = getMarkerMap(marker);
        return markerView.getCallout();
    }
  
    @Override
    public View getInfoContents(Marker marker) {
        AirMapMarker markerView = getMarkerMap(marker);
        return markerView.getInfoContents();
    }
  
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
      
        int action = MotionEventCompat.getActionMasked(ev);
      
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                this.getParent()
                    .requestDisallowInterceptTouchEvent(map != null && map.getUiSettings().isScrollGesturesEnabled());
                break;
            case (MotionEvent.ACTION_UP):
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }
  
    @Override
    public void onMarkerDragStart(Marker marker) {
        WritableMap event = makeClickEventData(marker.getPosition());
        manager.pushEvent(context, this, "onMarkerDragStart", event);
      
        AirMapMarker markerView = getMarkerMap(marker);
        event = makeClickEventData(marker.getPosition());
        manager.pushEvent(context, markerView, "onDragStart", event);
    }
  
    @Override
    public void onMarkerDrag(Marker marker) {
        WritableMap event = makeClickEventData(marker.getPosition());
        manager.pushEvent(context, this, "onMarkerDrag", event);
      
        AirMapMarker markerView = getMarkerMap(marker);
        event = makeClickEventData(marker.getPosition());
        manager.pushEvent(context, markerView, "onDrag", event);
    }
  
    @Override
    public void onMarkerDragEnd(Marker marker) {
        WritableMap event = makeClickEventData(marker.getPosition());
        manager.pushEvent(context, this, "onMarkerDragEnd", event);
      
        AirMapMarker markerView = getMarkerMap(marker);
        event = makeClickEventData(marker.getPosition());
        manager.pushEvent(context, markerView, "onDragEnd", event);
    }
  
    @Override
    public void onPoiClick(PointOfInterest poi) {
        WritableMap event = makeClickEventData(poi.latLng);
      
        event.putString("placeId", poi.placeId);
        event.putString("name", poi.name);
      
        manager.pushEvent(context, this, "onPoiClick", event);
    }
  
    private ProgressBar getMapLoadingProgressBar() {
        if (this.mapLoadingProgressBar == null) {
            this.mapLoadingProgressBar = new ProgressBar(getContext());
            this.mapLoadingProgressBar.setIndeterminate(true);
        }
        if (this.loadingIndicatorColor != null) {
            this.setLoadingIndicatorColor(this.loadingIndicatorColor);
        }
        return this.mapLoadingProgressBar;
    }
  
    private RelativeLayout getMapLoadingLayoutView() {
        if (this.mapLoadingLayout == null) {
            this.mapLoadingLayout = new RelativeLayout(getContext());
            this.mapLoadingLayout.setBackgroundColor(Color.LTGRAY);
            this.addView(this.mapLoadingLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
           
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            this.mapLoadingLayout.addView(this.getMapLoadingProgressBar(), params);
           
            this.mapLoadingLayout.setVisibility(View.INVISIBLE);
        }
        this.setLoadingBackgroundColor(this.loadingBackgroundColor);
        return this.mapLoadingLayout;
    }
  
    private ImageView getCacheImageView() {
        if (this.cacheImageView == null) {
            this.cacheImageView = new ImageView(getContext());
            this.addView(this.cacheImageView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            this.cacheImageView.setVisibility(View.INVISIBLE);
        }
        return this.cacheImageView;
    }
  
    private void removeCacheImageView() {
        if (this.cacheImageView != null) {
            ((ViewGroup) this.cacheImageView.getParent()).removeView(this.cacheImageView);
            this.cacheImageView = null;
        }
    }
  
    private void removeMapLoadingProgressBar() {
        if (this.mapLoadingProgressBar != null) {
            ((ViewGroup) this.mapLoadingProgressBar.getParent()).removeView(this.mapLoadingProgressBar);
            this.mapLoadingProgressBar = null;
        }
    }
  
    private void removeMapLoadingLayoutView() {
        this.removeMapLoadingProgressBar();
        if (this.mapLoadingLayout != null) {
            ((ViewGroup) this.mapLoadingLayout.getParent()).removeView(this.mapLoadingLayout);
            this.mapLoadingLayout = null;
        }
    }
  
    private void cacheView() {
        if (this.cacheEnabled) {
            final ImageView cacheImageView = this.getCacheImageView();
            final RelativeLayout mapLoadingLayout = this.getMapLoadingLayoutView();
            cacheImageView.setVisibility(View.INVISIBLE);
            mapLoadingLayout.setVisibility(View.VISIBLE);
            if (this.isMapLoaded) {
                this.map.snapshot(new HuaweiMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        cacheImageView.setImageBitmap(bitmap);
                        cacheImageView.setVisibility(View.VISIBLE);
                        mapLoadingLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        } else {
            this.removeCacheImageView();
            if (this.isMapLoaded) {
                this.removeMapLoadingLayoutView();
            }
        }
    }
  
    public void onPanDrag(MotionEvent ev) {
        Point point = new Point((int) ev.getX(), (int) ev.getY());
        LatLng coords = this.map.getProjection().fromScreenLocation(point);
        WritableMap event = makeClickEventData(coords);
        manager.pushEvent(context, this, "onPanDrag", event);
    }
  
    public void onDoublePress(MotionEvent ev) {
        Point point = new Point((int) ev.getX(), (int) ev.getY());
        LatLng coords = this.map.getProjection().fromScreenLocation(point);
        WritableMap event = makeClickEventData(coords);
        manager.pushEvent(context, this, "onDoublePress", event);
    }
  
    @Override
    public void onIndoorBuildingFocused() {
        IndoorBuilding building = this.map.getFocusedBuilding();
        if (building != null) {
            List<IndoorLevel> levels = building.getLevels();
            int index = 0;
            WritableArray levelsArray = Arguments.createArray();
            for (IndoorLevel level : levels) {
                WritableMap levelMap = Arguments.createMap();
                levelMap.putInt("index", index);
                levelMap.putString("name", level.getName());
                levelMap.putString("shortName", level.getShortName());
                levelsArray.pushMap(levelMap);
                index++;
            }
            WritableMap event = Arguments.createMap();
            WritableMap indoorBuilding = Arguments.createMap();
            indoorBuilding.putArray("levels", levelsArray);
            indoorBuilding.putInt("activeLevelIndex", building.getActiveLevelIndex());
            indoorBuilding.putBoolean("underground", building.isUnderground());
          
            event.putMap("IndoorBuilding", indoorBuilding);
          
            manager.pushEvent(context, this, "onIndoorBuildingFocused", event);
        } else {
            WritableMap event = Arguments.createMap();
            WritableArray levelsArray = Arguments.createArray();
            WritableMap indoorBuilding = Arguments.createMap();
            indoorBuilding.putArray("levels", levelsArray);
            indoorBuilding.putInt("activeLevelIndex", 0);
            indoorBuilding.putBoolean("underground", false);
          
            event.putMap("IndoorBuilding", indoorBuilding);
          
            manager.pushEvent(context, this, "onIndoorBuildingFocused", event);
        }
    }
  
    @Override
    public void onIndoorLevelActivated(IndoorBuilding building) {
        if (building == null) {
            return;
        }
        int activeLevelIndex = building.getActiveLevelIndex();
        if (activeLevelIndex < 0 || activeLevelIndex >= building.getLevels().size()) {
            return;
        }
        IndoorLevel level = building.getLevels().get(activeLevelIndex);
      
        WritableMap event = Arguments.createMap();
        WritableMap indoorlevel = Arguments.createMap();
      
        indoorlevel.putInt("activeLevelIndex", activeLevelIndex);
        indoorlevel.putString("name", level.getName());
        indoorlevel.putString("shortName", level.getShortName());
      
        event.putMap("IndoorLevel", indoorlevel);
      
        manager.pushEvent(context, this, "onIndoorLevelActivated", event);
    }
  
    public void setIndoorActiveLevelIndex(int activeLevelIndex) {
        IndoorBuilding building = this.map.getFocusedBuilding();
        if (building != null) {
            if (activeLevelIndex >= 0 && activeLevelIndex < building.getLevels().size()) {
                IndoorLevel level = building.getLevels().get(activeLevelIndex);
                if (level != null) {
                    level.activate();
                }
            }
        }
    }
  
    private AirMapMarker getMarkerMap(Marker marker) {
        AirMapMarker airMarker = markerMap.get(marker);
       
        if (airMarker != null) {
            return airMarker;
        }
       
        for (Map.Entry<Marker, AirMapMarker> entryMarker : markerMap.entrySet()) {
            if (entryMarker.getKey().equals(marker)) {
                airMarker = entryMarker.getValue();
                break;
            }
        }
       
        return airMarker;
    }
}
