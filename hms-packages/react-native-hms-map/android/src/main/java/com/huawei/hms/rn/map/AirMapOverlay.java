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
2020.03.23-Changed remove redundant code
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.GroundOverlay;
import com.huawei.hms.maps.model.GroundOverlayOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class AirMapOverlay extends AirMapFeature implements ImageReadable {

    private GroundOverlayOptions groundOverlayOptions;
    private GroundOverlay groundOverlay;
    private LatLngBounds bounds;
    private BitmapDescriptor iconBitmapDescriptor;
    private Bitmap iconBitmap;
    private boolean tappable;
    private float zIndex;
    private float transparency;
  
    private final ImageReader mImageReader;
    private HuaweiMap map;
  
    public AirMapOverlay(Context context) {
        super(context);
        this.mImageReader = new ImageReader(context, getResources(), this);
    }
  
    public void setBounds(ReadableArray bounds) {
        LatLng sw = new LatLng(bounds.getArray(1).getDouble(0), bounds.getArray(0).getDouble(1));
        LatLng ne = new LatLng(bounds.getArray(0).getDouble(0), bounds.getArray(1).getDouble(1));
        this.bounds = new LatLngBounds(sw, ne);
        if (this.groundOverlay != null) {
            this.groundOverlay.setPositionFromBounds(this.bounds);
        }
    }
  
    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (this.groundOverlay != null) {
            this.groundOverlay.setZIndex(zIndex);
        }
    }
  
    public void setImage(String uri) {
        this.mImageReader.setImage(uri);
    }
  
    public void setTappable(boolean tapabble) {
        this.tappable = tapabble;
        if (groundOverlay != null) {
            groundOverlay.setClickable(tappable);
        }
    }
  
  
    public GroundOverlayOptions getGroundOverlayOptions() {
        if (this.groundOverlayOptions == null) {
            this.groundOverlayOptions = createGroundOverlayOptions();
        }
        return this.groundOverlayOptions;
    }
  
    private GroundOverlayOptions createGroundOverlayOptions() {
        if (this.groundOverlayOptions != null) {
            return this.groundOverlayOptions;
        }
        GroundOverlayOptions options = new GroundOverlayOptions();
        if (this.iconBitmapDescriptor != null) {
            options.image(iconBitmapDescriptor);
        } else {
            options.image(BitmapDescriptorFactory.defaultMarker());
            options.visible(false);
        }
        options.positionFromBounds(bounds);
        options.zIndex(zIndex);
        return options;
    }
  
    @Override
    public Object getFeature() {
        return groundOverlay;
    }
  
    @Override
    public void addToMap(HuaweiMap map) {
        GroundOverlayOptions groundOverlayOptions = getGroundOverlayOptions();
        if (groundOverlayOptions != null) {
            this.groundOverlay = map.addGroundOverlay(groundOverlayOptions);
            this.groundOverlay.setClickable(this.tappable);
        } else {
            this.map = map;
        }
    }
  
    @Override
    public void removeFromMap(HuaweiMap map) {
        this.map = null;
        if (this.groundOverlay != null) {
            this.groundOverlay.remove();
            this.groundOverlay = null;
            this.groundOverlayOptions = null;
        }
    }
  
    @Override
    public void setIconBitmap(Bitmap bitmap) {
        this.iconBitmap = bitmap;
    }
  
    @Override
    public void setIconBitmapDescriptor(
        BitmapDescriptor iconBitmapDescriptor) {
            this.iconBitmapDescriptor = iconBitmapDescriptor;
    }
  
    @Override
    public void update() {
        this.groundOverlay = getGroundOverlay();
        if (this.groundOverlay != null) {
            this.groundOverlay.setVisible(true);
            this.groundOverlay.setImage(this.iconBitmapDescriptor);
            this.groundOverlay.setClickable(this.tappable);
        }
    }
  
    private GroundOverlay getGroundOverlay() {
        if (this.groundOverlay != null) {
            return this.groundOverlay;
        }
        if (this.map == null) {
            return null;
        }
        GroundOverlayOptions groundOverlayOptions = getGroundOverlayOptions();
        if (groundOverlayOptions != null) {
            return this.map.addGroundOverlay(groundOverlayOptions);
        }
        return null;
    }
}
