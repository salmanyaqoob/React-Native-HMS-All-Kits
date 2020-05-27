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
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.huawei.hms.maps.model.LatLng;


public class AirMapCircleManager extends ViewGroupManager<AirMapCircle> {
    private final DisplayMetrics metrics;
  
    public AirMapCircleManager(ReactApplicationContext reactContext) {
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
        return "AIRMapCircle";
    }
  
    @Override
    public AirMapCircle createViewInstance(ThemedReactContext context) {
        return new AirMapCircle(context);
    }
  
    @ReactProp(name = "center")
    public void setCenter(AirMapCircle view, ReadableMap center) {
        view.setCenter(new LatLng(center.getDouble("latitude"), center.getDouble("longitude")));
    }
  
    @ReactProp(name = "radius", defaultDouble = 0)
    public void setRadius(AirMapCircle view, double radius) {
        view.setRadius(radius);
    }
  
    @ReactProp(name = "strokeWidth", defaultFloat = 1f)
    public void setStrokeWidth(AirMapCircle view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints; 
        view.setStrokeWidth(widthInScreenPx);
    }
  
    @ReactProp(name = "fillColor", defaultInt = Color.RED, customType = "Color")
    public void setFillColor(AirMapCircle view, int color) {
        view.setFillColor(color);
    }
  
    @ReactProp(name = "strokeColor", defaultInt = Color.RED, customType = "Color")
    public void setStrokeColor(AirMapCircle view, int color) {
        view.setStrokeColor(color);
    }
  
    @ReactProp(name = "zIndex", defaultFloat = 1.0f)
    public void setZIndex(AirMapCircle view, float zIndex) {
        view.setZIndex(zIndex);
    }
  
}
