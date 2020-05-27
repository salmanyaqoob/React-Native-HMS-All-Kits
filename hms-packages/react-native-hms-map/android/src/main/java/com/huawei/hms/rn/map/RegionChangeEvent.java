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

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;

public class RegionChangeEvent extends Event<RegionChangeEvent> {
  private final LatLngBounds bounds;
  private final boolean continuous;

  public RegionChangeEvent(int id, LatLngBounds bounds, boolean continuous) {
    super(id);
    this.bounds = bounds;
    this.continuous = continuous;
  }

  @Override
  public String getEventName() {
    return "topChange";
  }

  @Override
  public boolean canCoalesce() {
    return false;
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {

    WritableMap event = new WritableNativeMap();
    event.putBoolean("continuous", continuous);

    WritableMap region = new WritableNativeMap();
    LatLng center = bounds.getCenter();
    region.putDouble("latitude", center.latitude);
    region.putDouble("longitude", center.longitude);
    region.putDouble("latitudeDelta", bounds.northeast.latitude - bounds.southwest.latitude);
    region.putDouble("longitudeDelta", bounds.northeast.longitude - bounds.southwest.longitude);
    event.putMap("region", region);

    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), event);
  }
}
