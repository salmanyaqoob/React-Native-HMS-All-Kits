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
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

public class AirMapPolygonManager extends ViewGroupManager<AirMapPolygon> {
    private final DisplayMetrics metrics;
  
    public AirMapPolygonManager(ReactApplicationContext reactContext) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }
  
    @Override
    public String getName() {
        return "AIRMapPolygon";
    }
  
    @Override
    public AirMapPolygon createViewInstance(ThemedReactContext context) {
        return new AirMapPolygon(context);
    }
  
    @ReactProp(name = "coordinates")
    public void setCoordinate(AirMapPolygon view, ReadableArray coordinates) {
        view.setCoordinates(coordinates);
    }
  
    @ReactProp(name = "holes")
    public  void setHoles(AirMapPolygon view, ReadableArray holes) {
        view.setHoles(holes);
    }
  
    @ReactProp(name = "strokeWidth", defaultFloat = 1f)
    public void setStrokeWidth(AirMapPolygon view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints;
        view.setStrokeWidth(widthInScreenPx);
    }
  
    @ReactProp(name = "fillColor", defaultInt = Color.RED, customType = "Color")
    public void setFillColor(AirMapPolygon view, int color) {
        view.setFillColor(color);
    }
  
    @ReactProp(name = "strokeColor", defaultInt = Color.RED, customType = "Color")
    public void setStrokeColor(AirMapPolygon view, int color) {
        view.setStrokeColor(color);
    }
  
    @ReactProp(name = "tappable", defaultBoolean = false)
    public void setTappable(AirMapPolygon view, boolean tapabble) {
        view.setTappable(tapabble);
    }
  
    @ReactProp(name = "geodesic", defaultBoolean = false)
    public void setGeodesic(AirMapPolygon view, boolean geodesic) {
        view.setGeodesic(geodesic);
    }
  
    @ReactProp(name = "zIndex", defaultFloat = 1.0f)
    public void setZIndex(AirMapPolygon view, float zIndex) {
        view.setZIndex(zIndex);
    }
  
    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
            "onPress", MapBuilder.of("registrationName", "onPress")
        );
    }
}
