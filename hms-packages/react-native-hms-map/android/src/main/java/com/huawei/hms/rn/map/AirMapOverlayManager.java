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
2020.03.23-Changed remove redundant code
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.content.Context;
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

public class AirMapOverlayManager extends ViewGroupManager<AirMapOverlay> {
    private final DisplayMetrics metrics;
  
    public AirMapOverlayManager(ReactApplicationContext reactContext) {
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
        return "AIRMapOverlay";
    }
  
    @Override
    public AirMapOverlay createViewInstance(ThemedReactContext context) {
        return new AirMapOverlay(context);
    }
  
    @ReactProp(name = "bounds")
    public void setBounds(AirMapOverlay view, ReadableArray bounds) {
        view.setBounds(bounds);
    }
  
    @ReactProp(name = "zIndex", defaultFloat = 1.0f)
    public void setZIndex(AirMapOverlay view, float zIndex) {
        view.setZIndex(zIndex);
    }
  
    @ReactProp(name = "image")
    public void setImage(AirMapOverlay view, @Nullable String source) {
        view.setImage(source);
    }
  
    @ReactProp(name = "tappable", defaultBoolean = false)
    public void setTappable(AirMapOverlay view, boolean tapabble) {
        view.setTappable(tapabble);
    }
  
    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
            "onPress", MapBuilder.of("registrationName", "onPress")
        );
    }
}
