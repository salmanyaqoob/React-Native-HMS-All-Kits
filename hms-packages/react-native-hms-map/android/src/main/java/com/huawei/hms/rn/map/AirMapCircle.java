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

import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;


public class AirMapCircle extends AirMapFeature {

    private CircleOptions circleOptions;
    private Circle circle;
  
    private LatLng center;
    private double radius;
    private int strokeColor;
    private int fillColor;
    private float strokeWidth;
    private float zIndex;
  
    public AirMapCircle(Context context) {
        super(context);
    }
  
    public void setCenter(LatLng center) {
        this.center = center;
        if (circle != null) {
            circle.setCenter(this.center);
        }
    }
  
    public void setRadius(double radius) {
        this.radius = radius;
        if (circle != null) {
            circle.setRadius(this.radius);
        }
    }
  
    public void setFillColor(int color) {
        this.fillColor = color;
        if (circle != null) {
            circle.setFillColor(color);
        }
    }
  
    public void setStrokeColor(int color) {
        this.strokeColor = color;
        if (circle != null) {
            circle.setStrokeColor(color);
        }
    }
  
    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        if (circle != null) {
            circle.setStrokeWidth(width);
        }
    }
  
    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (circle != null) {
            circle.setZIndex(zIndex);
        }
    }
  
    public CircleOptions getCircleOptions() {
        if (circleOptions == null) {
            circleOptions = createCircleOptions();
        }
        return circleOptions;
    }
  
    private CircleOptions createCircleOptions() {
        CircleOptions options = new CircleOptions();
        options.center(center);
        options.radius(radius);
        options.fillColor(fillColor);
        options.strokeColor(strokeColor);
        options.strokeWidth(strokeWidth);
        options.zIndex(zIndex);
        return options;
    }
  
    @Override
    public Object getFeature() {
        return circle;
    }
  
    @Override
    public void addToMap(HuaweiMap map) {
        circle = map.addCircle(getCircleOptions());
    }
  
    @Override
    public void removeFromMap(HuaweiMap map) {
        circle.remove();
    }
}
