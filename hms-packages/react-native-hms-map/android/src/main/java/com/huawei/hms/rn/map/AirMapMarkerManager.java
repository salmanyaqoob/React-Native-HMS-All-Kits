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
2020.03.05-Changed methods setCenterOffset, setCalloutOffset (removed)
2020.03.22-Changed code style and indentation issues are fixed
2020.03.23-Changed remove redundant code
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

public class AirMapMarkerManager extends ViewGroupManager<AirMapMarker> {

    private static final int SHOW_INFO_WINDOW = 1;
    private static final int HIDE_INFO_WINDOW = 2;
    private static final int ANIMATE_MARKER_TO_COORDINATE = 3;
    private static final int REDRAW = 4;
  
    public static class AirMapMarkerSharedIcon {
        private BitmapDescriptor iconBitmapDescriptor;
        private Bitmap bitmap;
        private Map<AirMapMarker, Boolean> markers;
        private boolean loadImageStarted;
      
        public AirMapMarkerSharedIcon(){
            this.markers = new WeakHashMap<>();
            this.loadImageStarted = false;
        }
      
        public synchronized boolean shouldLoadImage(){
            if (!this.loadImageStarted) {
                this.loadImageStarted = true;
                return true;
            }
            return false;
        }
      
        public synchronized void addMarker(AirMapMarker marker) {
            this.markers.put(marker, true);
            if (this.iconBitmapDescriptor != null) {
                marker.setIconBitmapDescriptor(this.iconBitmapDescriptor, this.bitmap);
            }
        }
      
        public synchronized void removeMarker(AirMapMarker marker) {
            this.markers.remove(marker);
        }
      
        public synchronized boolean hasMarker(){
            return this.markers.isEmpty();
        }
      
        public synchronized void updateIcon(BitmapDescriptor bitmapDescriptor, Bitmap bitmap) {
      
            this.iconBitmapDescriptor = bitmapDescriptor;
            this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
          
            if (this.markers.isEmpty()) {
                return;
            }
          
            for (Map.Entry<AirMapMarker, Boolean> markerEntry: markers.entrySet()) {
                if (markerEntry.getKey() != null) {
                    markerEntry.getKey().setIconBitmapDescriptor(bitmapDescriptor, bitmap);
                }
            }
        }
    }
  
    private Map<String, AirMapMarkerSharedIcon> sharedIcons = new ConcurrentHashMap<>();

    public AirMapMarkerSharedIcon getSharedIcon(String uri) {
        AirMapMarkerSharedIcon icon = this.sharedIcons.get(uri);
        if (icon == null) {
            synchronized (this) {
                if((icon = this.sharedIcons.get(uri)) == null) {
                    icon = new AirMapMarkerSharedIcon();
                    this.sharedIcons.put(uri, icon);
                }
            }
        }
        return icon;
    }
  
    public void removeSharedIconIfEmpty(String uri) {
        AirMapMarkerSharedIcon icon = this.sharedIcons.get(uri);
        if (icon == null) {
            return;
        }
        if (!icon.hasMarker()) {
            synchronized (this) {
                if((icon = this.sharedIcons.get(uri)) != null && !icon.hasMarker()) {
                    this.sharedIcons.remove(uri);
                }
            }
        }
    }
  
    public AirMapMarkerManager() {
    }
  
    @Override
    public String getName() {
        return "AIRMapMarker";
    }
  
    @Override
    public AirMapMarker createViewInstance(ThemedReactContext context) {
        return new AirMapMarker(context, this);
    }
  
    @ReactProp(name = "coordinate")
    public void setCoordinate(AirMapMarker view, ReadableMap map) {
        view.setCoordinate(map);
    }
  
    @ReactProp(name = "title")
    public void setTitle(AirMapMarker view, String title) {
        view.setTitle(title);
    }
  
    @ReactProp(name = "identifier")
    public void setIdentifier(AirMapMarker view, String identifier) {
        view.setIdentifier(identifier);
    }
  
    @ReactProp(name = "description")
    public void setDescription(AirMapMarker view, String description) {
        view.setSnippet(description);
    }
  
    @ReactProp(name = "anchor")
    public void setAnchor(AirMapMarker view, ReadableMap map) {
        double x = map != null && map.hasKey("x") ? map.getDouble("x") : 0.5;
        double y = map != null && map.hasKey("y") ? map.getDouble("y") : 1.0;
        view.setAnchor(x, y);
    }
  
    @ReactProp(name = "calloutAnchor")
    public void setCalloutAnchor(AirMapMarker view, ReadableMap map) {
        double x = map != null && map.hasKey("x") ? map.getDouble("x") : 0.5;
        double y = map != null && map.hasKey("y") ? map.getDouble("y") : 0.0;
        view.setCalloutAnchor(x, y);
    }
  
    @ReactProp(name = "image")
    public void setImage(AirMapMarker view, @Nullable String source) {
        view.setImage(source);
    }
  
    @ReactProp(name = "icon")
    public void setIcon(AirMapMarker view, @Nullable String source) {
        view.setImage(source);
    }
  
    @ReactProp(name = "pinColor", defaultInt = Color.RED, customType = "Color")
    public void setPinColor(AirMapMarker view, int pinColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(pinColor, hsv);
        view.setMarkerHue(hsv[0]);
    }
  
    @ReactProp(name = "rotation", defaultFloat = 0.0f)
    public void setMarkerRotation(AirMapMarker view, float rotation) {
        view.setRotation(rotation);
    }
  
    @ReactProp(name = "flat", defaultBoolean = false)
    public void setFlat(AirMapMarker view, boolean flat) {
        view.setFlat(flat);
    }
  
    @ReactProp(name = "draggable", defaultBoolean = false)
    public void setDraggable(AirMapMarker view, boolean draggable) {
        view.setDraggable(draggable);
    }
  
    @Override
    @ReactProp(name = "zIndex", defaultFloat = 0.0f)
    public void setZIndex(AirMapMarker view, float zIndex) {
        super.setZIndex(view, zIndex);
        int integerZIndex = Math.round(zIndex);
        view.setZIndex(integerZIndex);
    }
  
    @Override
    @ReactProp(name = "opacity", defaultFloat = 1.0f)
    public void setOpacity(AirMapMarker view, float opacity) {
        super.setOpacity(view, opacity);
        view.setOpacity(opacity);
    }
  
    @ReactProp(name = "tracksViewChanges", defaultBoolean = true)
    public void setTracksViewChanges(AirMapMarker view, boolean tracksViewChanges) {
        view.setTracksViewChanges(tracksViewChanges);
    }
  
    @Override
    public void addView(AirMapMarker parent, View child, int index) {
        if (child instanceof AirMapCallout) {
            parent.setCalloutView((AirMapCallout) child);
        } else {
            super.addView(parent, child, index);
            parent.update(true);
        }
    }
  
    @Override
    public void removeViewAt(AirMapMarker parent, int index) {
        super.removeViewAt(parent, index);
        parent.update(true);
    }
  
    @Override
    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
            "showCallout", SHOW_INFO_WINDOW,
            "hideCallout", HIDE_INFO_WINDOW,
            "animateMarkerToCoordinate", ANIMATE_MARKER_TO_COORDINATE,
            "redraw", REDRAW
        );
    }
  
    @Override
    public void receiveCommand(AirMapMarker view, int commandId, @Nullable ReadableArray args) {
        Integer duration;
        Double lat;
        Double lng;
        ReadableMap region;
      
        switch (commandId) {
            case SHOW_INFO_WINDOW:
                ((Marker) view.getFeature()).showInfoWindow();
                break;
          
            case HIDE_INFO_WINDOW:
                ((Marker) view.getFeature()).hideInfoWindow();
                break;
          
            case ANIMATE_MARKER_TO_COORDINATE:
                region = args.getMap(0);
                duration = args.getInt(1);
              
                lng = region.getDouble("longitude");
                lat = region.getDouble("latitude");
                view.animateToCoodinate(new LatLng(lat, lng), duration);
                break;
          
            case REDRAW:
                view.updateMarkerIcon();
                break;
        }
    }
  
    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Map<String, String>> map = MapBuilder.of(
            "onPress", MapBuilder.of("registrationName", "onPress"),
            "onCalloutPress", MapBuilder.of("registrationName", "onCalloutPress"),
            "onDragStart", MapBuilder.of("registrationName", "onDragStart"),
            "onDrag", MapBuilder.of("registrationName", "onDrag"),
            "onDragEnd", MapBuilder.of("registrationName", "onDragEnd")
        );
      
        map.putAll(MapBuilder.of(
            "onDragStart", MapBuilder.of("registrationName", "onDragStart"),
            "onDrag", MapBuilder.of("registrationName", "onDrag"),
            "onDragEnd", MapBuilder.of("registrationName", "onDragEnd")
        ));
      
        return map;
    }
  
    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new SizeReportingShadowNode();
    }
  
    @Override
    public void updateExtraData(AirMapMarker view, Object extraData) {
        HashMap<String, Float> data = (HashMap<String, Float>) extraData;
        float width = data.get("width");
        float height = data.get("height");
        view.update((int) width, (int) height);
    }
}
