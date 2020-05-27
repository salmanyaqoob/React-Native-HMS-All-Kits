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
2020.03.22-Changed code style and indentation issues are fixed
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Polygon;
import com.huawei.hms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class AirMapPolygon extends AirMapFeature {

    private PolygonOptions polygonOptions;
    private Polygon polygon;

    private List<LatLng> coordinates;
    private List<List<LatLng>> holes;
    private int strokeColor;
    private int fillColor;
    private float strokeWidth;
    private boolean geodesic;
    private boolean tappable;
    private float zIndex;

    public AirMapPolygon(Context context) {
        super(context);
    }

    public void setCoordinates(ReadableArray coordinates) {
        this.coordinates = new ArrayList<>(coordinates.size());
        for (int i = 0; i < coordinates.size(); i++) {
            ReadableMap coordinate = coordinates.getMap(i);
            this.coordinates.add(i,
                new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
        }
        if (polygon != null) {
            polygon.setPoints(this.coordinates);
        }
    }

    public void setHoles(ReadableArray holes) {
        if (holes == null) { 
            return; 
        }
  
        this.holes = new ArrayList<>(holes.size());
      
        for (int i = 0; i < holes.size(); i++) {
            ReadableArray hole = holes.getArray(i);
          
            if (hole.size() < 3) { 
                continue; 
            }
          
            List<LatLng> coordinates = new ArrayList<>();
            for (int j = 0; j < hole.size(); j++) {
                ReadableMap coordinate = hole.getMap(j);
                coordinates.add(new LatLng(
                    coordinate.getDouble("latitude"),
                    coordinate.getDouble("longitude")));
            }
          
            if (coordinates.size() == 3) {
                coordinates.add(coordinates.get(0));
            }
          
            this.holes.add(coordinates);
        }
  
        if (polygon != null) {
            polygon.setHoles(this.holes);
        }
    }

    public void setFillColor(int color) {
        this.fillColor = color;
        if (polygon != null) {
            polygon.setFillColor(color);
        }
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
        if (polygon != null) {
            polygon.setStrokeColor(color);
        }
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        if (polygon != null) {
            polygon.setStrokeWidth(width);
        }
    }

    public void setTappable(boolean tapabble) {
        this.tappable = tapabble;
        if (polygon != null) {
            polygon.setClickable(tappable);
        }
    }

    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
        if (polygon != null) {
            polygon.setGeodesic(geodesic);
        }
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (polygon != null) {
            polygon.setZIndex(zIndex);
        }
    }

    public PolygonOptions getPolygonOptions() {
        if (polygonOptions == null) {
            polygonOptions = createPolygonOptions();
        }
        return polygonOptions;
    }

    private PolygonOptions createPolygonOptions() {
        PolygonOptions options = new PolygonOptions();
        options.addAll(coordinates);
        options.fillColor(fillColor);
        options.strokeColor(strokeColor);
        options.strokeWidth(strokeWidth);
        options.geodesic(geodesic);
        options.zIndex(zIndex);

        if (this.holes != null) {
            for (int i = 0; i < holes.size(); i++) {
                options.addHole(holes.get(i));
            }
        }

        return options;
    }

    @Override
    public Object getFeature() {
        return polygon;
    }

    @Override
    public void addToMap(HuaweiMap map) {
        polygon = map.addPolygon(getPolygonOptions());
        polygon.setClickable(this.tappable);
    }
  
    @Override
    public void removeFromMap(HuaweiMap map) {
        polygon.remove();
    }
}
