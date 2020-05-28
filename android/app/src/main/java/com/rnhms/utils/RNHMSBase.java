package com.rnhms.utils;

import android.widget.Toast;
import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.huawei.hms.api.HuaweiApiAvailability;
import com.google.android.gms.common.GoogleApiAvailability;

import android.widget.Toast;
import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.huawei.hms.api.HuaweiApiAvailability;
import com.google.android.gms.common.GoogleApiAvailability;

public class RNHMSBase extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext;
    public RNHMSBase(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    // ReactContextBaseJavaModule requires derived class implement getName method. This function returns a string.
    // This string is used to tag the native module on the JavaScript side
    @Override
    public String getName() {
        return "HMSBase";
    }

    // Gets the application package name
    // To export a method for JavaScript use, Java methods need to use the @reactmethod annotation
    @ReactMethod
    public void getPackageName() {
        Toast.makeText(getReactApplicationContext(),"RNHMSBase has been called",Toast.LENGTH_LONG).show();
    }

    // Check is the device support HMS service
    // To export a method for JavaScript use, Java methods need to use the @reactmethod annotation
    @ReactMethod
    public void isHmsAvailable(Callback booleanCallback) {
        boolean isAvailable = false;
        Context context = getReactApplicationContext();
        if (null != context) {
            int result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context);
            isAvailable = (com.huawei.hms.api.ConnectionResult.SUCCESS == result);
        }
        Log.i("React", "isHmsAvailable: " + isAvailable);
        booleanCallback.invoke(isAvailable);
    }

    @ReactMethod
    public void isGmsAvailable(Callback booleanCallback) {
        boolean isAvailable = false;
        Context context = getReactApplicationContext();
        if (null != context) {
            int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
            isAvailable = (com.google.android.gms.common.ConnectionResult.SUCCESS == result);
        }
        Log.i("React",  "isGmsAvailable: " + isAvailable);
        booleanCallback.invoke(isAvailable);
    }

}