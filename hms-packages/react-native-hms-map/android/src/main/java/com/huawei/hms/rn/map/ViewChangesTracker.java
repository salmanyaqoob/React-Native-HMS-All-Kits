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

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;

public class ViewChangesTracker {

    private static ViewChangesTracker instance;
    private Handler handler;
    private LinkedList<AirMapMarker> markers = new LinkedList<>();
    private boolean hasScheduledFrame = false;
    private Runnable updateRunnable;
    private final long fps = 40;
  
    private ViewChangesTracker() {
        handler = new Handler(Looper.myLooper());
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                hasScheduledFrame = false;
                update();
               
                if (markers.size() > 0) {
                    handler.postDelayed(updateRunnable, fps);
                }
            }
        };
    }
  
    static ViewChangesTracker getInstance() {
        if (instance == null) {
            synchronized (ViewChangesTracker.class) {
                instance = new ViewChangesTracker();
            }
        }
      
        return instance;
    }
  
    public void addMarker(AirMapMarker marker) {
        markers.add(marker);
       
        if (!hasScheduledFrame) {
            hasScheduledFrame = true;
            handler.postDelayed(updateRunnable, fps);
        }
    }
  
    public void removeMarker(AirMapMarker marker) {
        markers.remove(marker);
    }
  
    public boolean containsMarker(AirMapMarker marker) {
        return markers.contains(marker);
    }
  
    private LinkedList<AirMapMarker> markersToRemove = new LinkedList<>();
  
    public void update() {
        for (AirMapMarker marker : markers) {
            if (!marker.updateCustomForTracking()) {
                markersToRemove.add(marker);
            }
        }
       
        if (markersToRemove.size() > 0) {
            markers.removeAll(markersToRemove);
            markersToRemove.clear();
        }
    }
  
}
