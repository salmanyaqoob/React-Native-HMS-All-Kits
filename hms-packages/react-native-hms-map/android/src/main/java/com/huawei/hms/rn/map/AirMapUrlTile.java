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
import com.huawei.hms.maps.model.TileOverlay;
import com.huawei.hms.maps.model.TileOverlayOptions;
import com.huawei.hms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class AirMapUrlTile extends AirMapFeature {

    class AIRMapUrlTileProvider extends UrlTileProvider {
        private String urlTemplate;
      
        public AIRMapUrlTileProvider(int width, int height, String urlTemplate) {
            super(width, height);
            this.urlTemplate = urlTemplate;
        }
      
        @Override
        public synchronized URL getTileUrl(int x, int y, int zoom) {
      
            if (AirMapUrlTile.this.flipY == true) {
                y = (1 << zoom) - y - 1;
            }
          
            String s = this.urlTemplate
                .replace("{x}", Integer.toString(x))
                .replace("{y}", Integer.toString(y))
                .replace("{z}", Integer.toString(zoom));
            URL url = null;
          
            if(AirMapUrlTile.this.maximumZ > 0 && zoom > maximumZ) {
                return url;
            }
          
            if(AirMapUrlTile.this.minimumZ > 0 && zoom < minimumZ) {
                return url;
            }
          
            try {
                url = new URL(s);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
            return url;
        }
      
        public void setUrlTemplate(String urlTemplate) {
            this.urlTemplate = urlTemplate;
        }
    }
  
    private TileOverlayOptions tileOverlayOptions;
    private TileOverlay tileOverlay;
    private AIRMapUrlTileProvider tileProvider;
  
    private String urlTemplate;
    private float zIndex;
    private float maximumZ;
    private float minimumZ;
    private boolean flipY;
  
    public AirMapUrlTile(Context context) {
        super(context);
    }
  
    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
        if (tileProvider != null) {
            tileProvider.setUrlTemplate(urlTemplate);
        }
        if (tileOverlay != null) {
            tileOverlay.clearTileCache();
        }
    }
  
    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
        if (tileOverlay != null) {
            tileOverlay.setZIndex(zIndex);
        }
    }
  
    public void setMaximumZ(float maximumZ) {
        this.maximumZ = maximumZ;
        if (tileOverlay != null) {
            tileOverlay.clearTileCache();
        }
    }
  
    public void setMinimumZ(float minimumZ) {
        this.minimumZ = minimumZ;
        if (tileOverlay != null) {
            tileOverlay.clearTileCache();
        }
    }
  
    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
        if (tileOverlay != null) {
            tileOverlay.clearTileCache();
        }
    }
  
    public TileOverlayOptions getTileOverlayOptions() {
        if (tileOverlayOptions == null) {
            tileOverlayOptions = createTileOverlayOptions();
        }
        return tileOverlayOptions;
    }
  
    private TileOverlayOptions createTileOverlayOptions() {
        TileOverlayOptions options = new TileOverlayOptions();
        options.zIndex(zIndex);
        this.tileProvider = new AIRMapUrlTileProvider(256, 256, this.urlTemplate);
        options.tileProvider(this.tileProvider);
        return options;
    }
  
    @Override
    public Object getFeature() {
        return tileOverlay;
    }
  
    @Override
    public void addToMap(HuaweiMap map) {
        this.tileOverlay = map.addTileOverlay(getTileOverlayOptions());
    }
  
    @Override
    public void removeFromMap(HuaweiMap map) {
        tileOverlay.remove();
    }
}
