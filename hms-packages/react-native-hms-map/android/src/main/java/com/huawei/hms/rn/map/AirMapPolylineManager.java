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
import com.huawei.hms.maps.model.ButtCap;
import com.huawei.hms.maps.model.Cap;
import com.huawei.hms.maps.model.RoundCap;
import com.huawei.hms.maps.model.SquareCap;

import java.util.Map;

import javax.annotation.Nullable;

public class AirMapPolylineManager extends ViewGroupManager<AirMapPolyline> {
    private final DisplayMetrics metrics;
  
    public AirMapPolylineManager(ReactApplicationContext reactContext) {
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
        return "AIRMapPolyline";
    }
  
    @Override
    public AirMapPolyline createViewInstance(ThemedReactContext context) {
        return new AirMapPolyline(context);
    }
  
    @ReactProp(name = "coordinates")
    public void setCoordinate(AirMapPolyline view, ReadableArray coordinates) {
        view.setCoordinates(coordinates);
    }
  
    @ReactProp(name = "strokeWidth", defaultFloat = 1f)
    public void setStrokeWidth(AirMapPolyline view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints;
        view.setWidth(widthInScreenPx);
    }
  
    @ReactProp(name = "strokeColor", defaultInt = Color.RED, customType = "Color")
    public void setStrokeColor(AirMapPolyline view, int color) {
        view.setColor(color);
    }
  
    @ReactProp(name = "tappable", defaultBoolean = false)
    public void setTappable(AirMapPolyline view, boolean tapabble) {
        view.setTappable(tapabble);
    }
  
    @ReactProp(name = "geodesic", defaultBoolean = false)
    public void setGeodesic(AirMapPolyline view, boolean geodesic) {
        view.setGeodesic(geodesic);
    }
  
    @ReactProp(name = "zIndex", defaultFloat = 1.0f)
    public void setZIndex(AirMapPolyline view, float zIndex) {
        view.setZIndex(zIndex);
    }
  
    @ReactProp(name = "lineCap")
    public void setlineCap(AirMapPolyline view, String lineCap) {
        Cap cap = null;
        switch (lineCap) {
            case "butt":
                cap = new ButtCap();
                break;
            case "round":
                cap = new RoundCap();
                break;
            case "square":
                cap = new SquareCap();
                break;
            default:
                cap = new RoundCap();
                break;
        }
        view.setLineCap(cap);
    }
  
    @ReactProp(name = "lineDashPattern")
    public void setLineDashPattern(AirMapPolyline view, ReadableArray patternValues) {
        view.setLineDashPattern(patternValues);
    }
  
    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
            "onPress", MapBuilder.of("registrationName", "onPress")
        );
    }
}
