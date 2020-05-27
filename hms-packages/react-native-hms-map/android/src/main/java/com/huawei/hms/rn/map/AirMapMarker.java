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
2020.03.05-Changed methods updateMarkerIcon, fillMarkerOptions
2020.03.22-Changed code style and indentation issues are fixed
Huawei Technologies Co., Ltd.
*/

package com.huawei.hms.rn.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.view.View;
import android.widget.LinearLayout;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.animation.TypeEvaluator;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.hms.feature.dynamic.IObjectWrapper;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.util.LogM;

import javax.annotation.Nullable;

import static com.huawei.hms.maps.model.BitmapDescriptorFactory.sIBitmapDescriptorDelegate;

public class AirMapMarker extends AirMapFeature {

  private MarkerOptions markerOptions;
  private Marker marker;
  private int width;
  private int height;
  private String identifier;

  private LatLng position;
  private String title;
  private String snippet;

  private boolean anchorIsSet;
  private float anchorX;
  private float anchorY;

  private AirMapCallout calloutView;
  private View wrappedCalloutView;
  private final Context context;

  private float markerHue = BitmapDescriptorFactory.HUE_RED;
  private BitmapDescriptor iconBitmapDescriptor;
  private Bitmap iconBitmap;

  private float rotation = 0.0f;
  private boolean flat = false;
  private boolean draggable = false;
  private int zIndex = 0;
  private float opacity = 1f;

  private float calloutAnchorX;
  private float calloutAnchorY;
  private boolean calloutAnchorIsSet;

  private boolean tracksViewChanges = true;
  private boolean tracksViewChangesActive = false;
  private boolean hasViewChanges = true;

  private boolean hasCustomMarkerView = false;
  private final AirMapMarkerManager markerManager;
  private String imageUri;

  private final DraweeHolder<?> logoHolder;
  private DataSource<CloseableReference<CloseableImage>> dataSource;
  private final ControllerListener<ImageInfo> mLogoControllerListener =
      new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
            String id,
            @Nullable final ImageInfo imageInfo,
            @Nullable Animatable animatable) {
          CloseableReference<CloseableImage> imageReference = null;
          try {
            imageReference = dataSource.getResult();
            if (imageReference != null) {
              CloseableImage image = imageReference.get();
              if (image != null && image instanceof CloseableStaticBitmap) {
                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                if (bitmap != null) {
                  bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                  iconBitmap = bitmap;
                  iconBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                }
              }
            }
          } finally {
            dataSource.close();
            if (imageReference != null) {
              CloseableReference.closeSafely(imageReference);
            }
          }
          if (AirMapMarker.this.markerManager != null && AirMapMarker.this.imageUri != null) {
            AirMapMarker.this.markerManager.getSharedIcon(AirMapMarker.this.imageUri)
                .updateIcon(iconBitmapDescriptor, iconBitmap);
          }
          update(true);
        }
      };


  public Marker getMarker(){
    return this.marker;
  }

  public AirMapMarker(Context context, AirMapMarkerManager markerManager) {
    super(context);
    this.context = context;
    this.markerManager = markerManager;
    logoHolder = DraweeHolder.create(createDraweeHierarchy(), context);
    logoHolder.onAttach();
  }

  public AirMapMarker(Context context, MarkerOptions options, AirMapMarkerManager markerManager) {
    super(context);
    this.context = context;
    this.markerManager = markerManager;
    logoHolder = DraweeHolder.create(createDraweeHierarchy(), context);
    logoHolder.onAttach();

    position = options.getPosition();
    setAnchor(options.getAnchorU(), options.getAnchorV());
    setCalloutAnchor(options.getInfoWindowAnchorU(), options.getInfoWindowAnchorV());
    setTitle(options.getTitle());
    setSnippet(options.getSnippet());
    setRotation(options.getRotation());
    setFlat(options.isFlat());
    setDraggable(options.isDraggable());
    setZIndex(Math.round(options.getZIndex()));
    setAlpha(options.getAlpha());
    iconBitmapDescriptor = options.getIcon();
  }

  private GenericDraweeHierarchy createDraweeHierarchy() {
    return new GenericDraweeHierarchyBuilder(getResources())
        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
        .setFadeDuration(0)
        .build();
  }

  public void setCoordinate(ReadableMap coordinate) {
    position = new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude"));
    if (marker != null) {
      marker.setPosition(position);
    }
    update(false);
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
    update(false);
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setTitle(String title) {
    this.title = title;
    if (marker != null) {
      marker.setTitle(title);
    }
    update(false);
  }

  public void setSnippet(String snippet) {
    this.snippet = snippet;
    if (marker != null) {
      marker.setSnippet(snippet);
    }
    update(false);
  }

  public void setRotation(float rotation) {
    this.rotation = rotation;
    if (marker != null) {
      marker.setRotation(rotation);
    }
    update(false);
  }

  public void setFlat(boolean flat) {
    this.flat = flat;
    if (marker != null) {
      marker.setFlat(flat);
    }
    update(false);
  }

  public void setDraggable(boolean draggable) {
    this.draggable = draggable;
    if (marker != null) {
      marker.setDraggable(draggable);
    }
    update(false);
  }

  public void setZIndex(int zIndex) {
    this.zIndex = zIndex;
    if (marker != null) {
      marker.setZIndex(zIndex);
    }
    update(false);
  }

  public void setOpacity(float opacity) {
    this.opacity = opacity;
    if (marker != null) {
      marker.setAlpha(opacity);
    }
    update(false);
  }

  public void setMarkerHue(float markerHue) {
    this.markerHue = markerHue;
    update(false);
  }

  public void setAnchor(double x, double y) {
    anchorIsSet = true;
    anchorX = (float) x;
    anchorY = (float) y;
    if (marker != null) {
      marker.setAnchor(anchorX, anchorY);
    }
    update(false);
  }

  public void setCalloutAnchor(double x, double y) {
    calloutAnchorIsSet = true;
    calloutAnchorX = (float) x;
    calloutAnchorY = (float) y;
    if (marker != null) {
      marker.setInfoWindowAnchor(calloutAnchorX, calloutAnchorY);
    }
    update(false);
  }

  public void setTracksViewChanges(boolean tracksViewChanges) {
    this.tracksViewChanges = tracksViewChanges;
    updateTracksViewChanges();
  }

  private void updateTracksViewChanges() {
    boolean shouldTrack = tracksViewChanges && hasCustomMarkerView && marker != null;
    if (shouldTrack == tracksViewChangesActive) {
      return;
    }
    tracksViewChangesActive = shouldTrack;

    if (shouldTrack) {
      ViewChangesTracker.getInstance().addMarker(this);
    } else {
      ViewChangesTracker.getInstance().removeMarker(this);

      updateMarkerIcon();
    }
  }

  public boolean updateCustomForTracking() {
    if (!tracksViewChangesActive) {
      return false;
    }

    updateMarkerIcon();

    return true;
  }

  public void updateMarkerIcon() {
    if (marker == null) {
      return;
    }
    if (!hasCustomMarkerView) {
      hasViewChanges = false;
    }
    if (marker != null && getIcon() != null) {
      marker.setIcon(getIcon());
    }
  }

  public LatLng interpolate(float fraction, LatLng a, LatLng b) {
    double lat = (b.latitude - a.latitude) * fraction + a.latitude;
    double lng = (b.longitude - a.longitude) * fraction + a.longitude;
    return new LatLng(lat, lng);
  }

  public void animateToCoodinate(LatLng finalPosition, Integer duration) {
    TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
      @Override
      public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
        return interpolate(fraction, startValue, endValue);
      }
    };
    Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
    ObjectAnimator animator = ObjectAnimator.ofObject(
      marker,
      property,
      typeEvaluator,
      finalPosition);
    animator.setDuration(duration);
    animator.start();
  }

  public void setImage(String uri) {
    hasViewChanges = true;

    boolean shouldLoadImage = true;

    if (this.markerManager != null) {
      if (this.imageUri != null) {
        this.markerManager.getSharedIcon(this.imageUri).removeMarker(this);
        this.markerManager.removeSharedIconIfEmpty(this.imageUri);
      }
      if (uri != null) {
        AirMapMarkerManager.AirMapMarkerSharedIcon sharedIcon = this.markerManager.getSharedIcon(uri);
        sharedIcon.addMarker(this);
        shouldLoadImage = sharedIcon.shouldLoadImage();
      }
    }

    this.imageUri = uri;
    if (!shouldLoadImage) {
      return;
    }

    if (uri == null) {
      iconBitmapDescriptor = null;
      update(true);
    } else if (uri.startsWith("http://") || uri.startsWith("https://") ||
        uri.startsWith("file://") || uri.startsWith("asset://") || uri.startsWith("data:")) {
      ImageRequest imageRequest = ImageRequestBuilder
          .newBuilderWithSource(Uri.parse(uri))
          .build();

      ImagePipeline imagePipeline = Fresco.getImagePipeline();
      dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
      DraweeController controller = Fresco.newDraweeControllerBuilder()
          .setImageRequest(imageRequest)
          .setControllerListener(mLogoControllerListener)
          .setOldController(logoHolder.getController())
          .build();
      logoHolder.setController(controller);
    } else {
      iconBitmapDescriptor = getBitmapDescriptorByName(uri);
      if (iconBitmapDescriptor != null) {
          int drawableId = getDrawableResourceByName(uri);
          iconBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
          if (iconBitmap == null) {
              Drawable drawable = getResources().getDrawable(drawableId);
              iconBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
              drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
              Canvas canvas = new Canvas(iconBitmap);
              drawable.draw(canvas);
          }
      }
      if (this.markerManager != null && uri != null) {
        this.markerManager.getSharedIcon(uri).updateIcon(iconBitmapDescriptor, iconBitmap);
      }
      update(true);
    }
  }

  public void setIconBitmapDescriptor(BitmapDescriptor bitmapDescriptor, Bitmap bitmap) {
    this.iconBitmapDescriptor = bitmapDescriptor;
    this.iconBitmap = bitmap;
    this.hasViewChanges = true;
    this.update(true);
  }

  public void setIconBitmap(Bitmap bitmap) {
    this.iconBitmap = bitmap;
  }

  public MarkerOptions getMarkerOptions() {
    if (markerOptions == null) {
      markerOptions = new MarkerOptions();
    }

    fillMarkerOptions(markerOptions);
    return markerOptions;
  }

  @Override
  public void addView(View child, int index) {
    super.addView(child, index);
    if (!(child instanceof AirMapCallout)) {
      hasCustomMarkerView = true;
      updateTracksViewChanges();
    }
    update(true);
  }

  @Override
  public void requestLayout() {
    super.requestLayout();

    if (getChildCount() == 0) {
      if (hasCustomMarkerView) {
        hasCustomMarkerView = false;
        clearDrawableCache();
        updateTracksViewChanges();
        update(true);
      }

    }
  }

  @Override
  public Object getFeature() {
    return marker;
  }

  @Override
  public void addToMap(HuaweiMap map) {
    marker = map.addMarker(getMarkerOptions());
    updateTracksViewChanges();
  }

  @Override
  public void removeFromMap(HuaweiMap map) {
    if (marker == null) {
      return;
    }
    marker.remove();
    marker = null;
    updateTracksViewChanges();
  }

  private BitmapDescriptor getIcon() {
    if (hasCustomMarkerView) {
      if (iconBitmapDescriptor != null) {
        Bitmap viewBitmap = createDrawable();
        int width = Math.max(iconBitmap.getWidth(), viewBitmap.getWidth());
        int height = Math.max(iconBitmap.getHeight(), viewBitmap.getHeight());
        Bitmap combinedBitmap = Bitmap.createBitmap(width, height, iconBitmap.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(iconBitmap, 0, 0, null);
        canvas.drawBitmap(viewBitmap, 0, 0, null);
        return BitmapDescriptorFactory.fromBitmap(combinedBitmap);
      } else {
        return BitmapDescriptorFactory.fromBitmap(createDrawable());
      }
    } else if (iconBitmapDescriptor != null) {
      return iconBitmapDescriptor;
    } else {

      return BitmapDescriptorFactory.defaultMarker(this.markerHue);
    }
  }

  private MarkerOptions fillMarkerOptions(MarkerOptions options) {
    options.position(position);
    if (anchorIsSet) {
      options.anchor(anchorX, anchorY);
    }
    if (calloutAnchorIsSet) {
      options.infoWindowAnchor(calloutAnchorX, calloutAnchorY);
    }
    options.title(title);
    options.snippet(snippet);
    options.rotation(rotation);
    options.flat(flat);
    options.draggable(draggable);
    options.zIndex(zIndex);
    options.alpha(opacity);
    return options;
  }

  public void update(boolean updateIcon) {
    if (marker == null) {
      return;
    }

    if (updateIcon) {
      updateMarkerIcon();
    }

    if (anchorIsSet) {
      marker.setAnchor(anchorX, anchorY);
    } else {
      marker.setAnchor(0.5f, 1.0f);
    }

    if (calloutAnchorIsSet) {
      marker.setInfoWindowAnchor(calloutAnchorX, calloutAnchorY);
    } else {
      marker.setInfoWindowAnchor(0.5f, 0);
    }
  }

  public void update(int width, int height) {
    this.width = width;
    this.height = height;

    update(true);
  }

  private Bitmap mLastBitmapCreated = null;

  private void clearDrawableCache() {
    mLastBitmapCreated = null;
  }

  private Bitmap createDrawable() {
    int width = this.width <= 0 ? 100 : this.width;
    int height = this.height <= 0 ? 100 : this.height;
    this.buildDrawingCache();

    Bitmap bitmap = mLastBitmapCreated;

    if (bitmap == null ||
            bitmap.isRecycled() ||
            bitmap.getWidth() != width ||
            bitmap.getHeight() != height) {
      bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      mLastBitmapCreated = bitmap;
    } else {
      bitmap.eraseColor(Color.TRANSPARENT);
    }

    Canvas canvas = new Canvas(bitmap);
    this.draw(canvas);

    return bitmap;
  }

  public void setCalloutView(AirMapCallout view) {
    this.calloutView = view;
  }

  public AirMapCallout getCalloutView() {
    return this.calloutView;
  }

  public View getCallout() {
    if (this.calloutView == null) {
      return null;
    }

    if (this.wrappedCalloutView == null) {
      this.wrapCalloutView();
    }

    if (this.calloutView.getTooltip()) {
      return this.wrappedCalloutView;
    } else {
      return null;
    }
  }

  public View getInfoContents() {
    if (this.calloutView == null) {
      return null;
    }
    if (this.wrappedCalloutView == null) {
      this.wrapCalloutView();
    }

    if (this.calloutView.getTooltip()) {
      return null;
    } else {
      return this.wrappedCalloutView;
    }
  }

  private void wrapCalloutView() {
    if (this.calloutView == null || this.calloutView.getChildCount() == 0) {
      return;
    }

    LinearLayout ll = new LinearLayout(context);
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.setLayoutParams(new LinearLayout.LayoutParams(
        this.calloutView.width,
        this.calloutView.height,
        0f
    ));


    LinearLayout ll2 = new LinearLayout(context);
    ll2.setOrientation(LinearLayout.HORIZONTAL);
    ll2.setLayoutParams(new LinearLayout.LayoutParams(
        this.calloutView.width,
        this.calloutView.height,
        0f
    ));

    ll.addView(ll2);
    ll2.addView(this.calloutView);

    this.wrappedCalloutView = ll;
  }

  private int getDrawableResourceByName(String name) {
    return getResources().getIdentifier(
        name,
        "drawable",
        getContext().getPackageName());
  }

  private BitmapDescriptor getBitmapDescriptorByName(String name) {
    return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(name));
  }

}
