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
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.Cap;
import com.huawei.hms.maps.model.Dash;
import com.huawei.hms.maps.model.Dot;
import com.huawei.hms.maps.model.Gap;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.PatternItem;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.PolylineOptions;
import com.huawei.hms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.List;

public class AirMapPolyline extends AirMapFeature {

    private PolylineOptions polylineOptions;
    private Polyline polyline;
  
    private List<LatLng> coordinates;
    private int color;
    private float width;
    private boolean tappable;
    private boolean geodesic;
    private float zIndex;
    private Cap lineCap = new RoundCap();
    private ReadableArray patternValues;
    private List<PatternItem> pattern;
  
    public AirMapPolyline(Context context) {
        super(context);
    }
  
    public void setCoordinates(ReadableArray coordinates) {
        this.coordinates = new ArrayList<>(coordinates.size());
        for (int i = 0; i < coordinates.size(); i++) {
            ReadableMap coordinate = coordinates.getMap(i);
            this.coordinates.add(i,
                new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
        }
        if (polyline != null) {
            polyline.setPoints(this.coordinates);
        }
    }
  
    public void setColor(int color) {
        this.color = color;
        if (polyline != null) {
            polyline.setColor(color);
        }
    }
  
    public void setWidth(float width) {
        this.width = width;
        if (polyline != null) {
            polyline.setWidth(width);
        }
    }
  
    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (polyline != null) {
            polyline.setZIndex(zIndex);
        }
    }
  
    public void setTappable(boolean tapabble) {
        this.tappable = tapabble;
        if (polyline != null) {
            polyline.setClickable(tappable);
        }
    }
  
    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
        if (polyline != null) {
            polyline.setGeodesic(geodesic);
        }
    }
  
    public void setLineCap(Cap cap) {
        this.lineCap = cap;
        if (polyline != null) {
            polyline.setStartCap(cap);
            polyline.setEndCap(cap);
        }
        this.applyPattern();
    }
  
    public void setLineDashPattern(ReadableArray patternValues) {
        this.patternValues = patternValues;
        this.applyPattern();
    }
  
    private void applyPattern() {
        if(patternValues == null) {
            return;
        }
        this.pattern = new ArrayList<>(patternValues.size());
        for (int i = 0; i < patternValues.size(); i++) {
            float patternValue = (float) patternValues.getDouble(i);
            boolean isGap = i % 2 != 0;
            if(isGap) {
                this.pattern.add(new Gap(patternValue));
            }else {
                PatternItem patternItem = null;
                boolean isLineCapRound = this.lineCap instanceof RoundCap;
                if(isLineCapRound) {
                    patternItem = new Dot();
                }else {
                    patternItem = new Dash(patternValue);
                }
                this.pattern.add(patternItem);
            }
        }
        if(polyline != null) {
            polyline.setPattern(this.pattern);
        }
    }
  
    public PolylineOptions getPolylineOptions() {
        if (polylineOptions == null) {
            polylineOptions = createPolylineOptions();
        }
        return polylineOptions;
    }
  
    private PolylineOptions createPolylineOptions() {
        PolylineOptions options = new PolylineOptions();
        options.addAll(coordinates);
        options.color(color);
        options.width(width);
        options.geodesic(geodesic);
        options.zIndex(zIndex);
        options.startCap(lineCap);
        options.endCap(lineCap);
        options.pattern(this.pattern);
        return options;
    }
  
    @Override
    public Object getFeature() {
        return polyline;
    }
  
    @Override
    public void addToMap(HuaweiMap map) {
        polyline = map.addPolyline(getPolylineOptions());
        polyline.setClickable(this.tappable);
    }
  
    @Override
    public void removeFromMap(HuaweiMap map) {
        polyline.remove();
    }
}
