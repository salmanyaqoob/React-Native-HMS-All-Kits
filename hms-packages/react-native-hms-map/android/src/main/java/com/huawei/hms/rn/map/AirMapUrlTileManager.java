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
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AirMapUrlTileManager extends ViewGroupManager<AirMapUrlTile> {
  private DisplayMetrics metrics;

  public AirMapUrlTileManager(ReactApplicationContext reactContext) {
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
    return "AIRMapUrlTile";
  }

  @Override
  public AirMapUrlTile createViewInstance(ThemedReactContext context) {
    return new AirMapUrlTile(context);
  }

  @ReactProp(name = "urlTemplate")
  public void setUrlTemplate(AirMapUrlTile view, String urlTemplate) {
    view.setUrlTemplate(urlTemplate);
  }

  @ReactProp(name = "zIndex", defaultFloat = -1.0f)
  public void setZIndex(AirMapUrlTile view, float zIndex) {
    view.setZIndex(zIndex);
  }

  @ReactProp(name = "minimumZ", defaultFloat = 0.0f)
  public void setMinimumZ(AirMapUrlTile view, float minimumZ) {
    view.setMinimumZ(minimumZ);
  }

  @ReactProp(name = "maximumZ", defaultFloat = 100.0f)
  public void setMaximumZ(AirMapUrlTile view, float maximumZ) {
    view.setMaximumZ(maximumZ);
  }

  @ReactProp(name = "flipY", defaultBoolean = false)
  public void setFlipY(AirMapUrlTile view, boolean flipY) {
    view.setFlipY(flipY);
  }

}
