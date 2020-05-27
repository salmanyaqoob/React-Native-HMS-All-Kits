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


import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;

public class LatLngBoundsUtils {
  public static boolean boundsAreDifferent(LatLngBounds a, LatLngBounds b) {
    LatLng centerA = a.getCenter();
    double latA = centerA.latitude;
    double lngA = centerA.longitude;
    double latDeltaA = a.northeast.latitude - a.southwest.latitude;
    double lngDeltaA = a.northeast.longitude - a.southwest.longitude;

    LatLng centerB = b.getCenter();
    double latB = centerB.latitude;
    double lngB = centerB.longitude;
    double latDeltaB = b.northeast.latitude - b.southwest.latitude;
    double lngDeltaB = b.northeast.longitude - b.southwest.longitude;

    double latEps = latitudeEpsilon(a, b);
    double lngEps = longitudeEpsilon(a, b);

    return
        different(latA, latB, latEps) ||
            different(lngA, lngB, lngEps) ||
            different(latDeltaA, latDeltaB, latEps) ||
            different(lngDeltaA, lngDeltaB, lngEps);
  }

  private static boolean different(double a, double b, double epsilon) {
    return Math.abs(a - b) > epsilon;
  }

  private static double latitudeEpsilon(LatLngBounds a, LatLngBounds b) {
    double sizeA = a.northeast.latitude - a.southwest.latitude;
    double sizeB = b.northeast.latitude - b.southwest.latitude;
    double size = Math.min(Math.abs(sizeA), Math.abs(sizeB));
    return size / 2560;
  }

  private static double longitudeEpsilon(LatLngBounds a, LatLngBounds b) {
    double sizeA = a.northeast.longitude - a.southwest.longitude;
    double sizeB = b.northeast.longitude - b.southwest.longitude;
    double size = Math.min(Math.abs(sizeA), Math.abs(sizeB));
    return size / 2560;
  }
}
