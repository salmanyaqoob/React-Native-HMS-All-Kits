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
2020.03.05-Changed methods createViewManagers
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapsPackage implements ReactPackage {
  public MapsPackage(Activity activity) {
  } // backwards compatibility

  public MapsPackage() {
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    return Arrays.<NativeModule>asList(new AirMapModule(reactContext));
  }

  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    AirMapCalloutManager calloutManager = new AirMapCalloutManager();
    AirMapMarkerManager annotationManager = new AirMapMarkerManager();
    AirMapPolylineManager polylineManager = new AirMapPolylineManager(reactContext);
    AirMapPolygonManager polygonManager = new AirMapPolygonManager(reactContext);
    AirMapCircleManager circleManager = new AirMapCircleManager(reactContext);
    AirMapManager mapManager = new AirMapManager(reactContext);
    AirMapLiteManager mapLiteManager = new AirMapLiteManager(reactContext);
    AirMapUrlTileManager urlTileManager = new AirMapUrlTileManager(reactContext);
    AirMapWMSTileManager gsUrlTileManager = new AirMapWMSTileManager(reactContext);
    AirMapLocalTileManager localTileManager = new AirMapLocalTileManager(reactContext);
    AirMapOverlayManager overlayManager = new AirMapOverlayManager(reactContext);
    mapManager.setMarkerManager(annotationManager);

    return Arrays.<ViewManager>asList(
        calloutManager,
        annotationManager,
        polylineManager,
        polygonManager,
        circleManager,
        mapManager,
        mapLiteManager,
        urlTileManager,
        gsUrlTileManager,
        localTileManager,
        overlayManager
    );
  }
}
