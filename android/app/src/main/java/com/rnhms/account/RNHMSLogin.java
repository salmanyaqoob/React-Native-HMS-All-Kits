package com.rnhms.account;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.auth.api.signin.HuaweiIdSignIn;
import com.huawei.hms.auth.api.signin.HuaweiIdSignInClient;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.hwid.HuaweiIdSignInOptions;
import com.huawei.hms.support.api.hwid.SignInHuaweiId;

import org.json.JSONException;

import com.rnhms.Constants;

public class RNHMSLogin extends ReactContextBaseJavaModule {
    private ReactApplicationContext mContext;
    private Promise mPromise;
    private Activity activity;
    private HuaweiIdSignInClient mSignInClient;

    public RNHMSLogin(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mContext = reactContext;
        this.mContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "HMSLogin";
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

            Task<SignInHuaweiId> signInHuaweiIdTask = HuaweiIdSignIn.getSignedInAccountFromIntent(data);

            if (mPromise != null && requestCode == Constants.REQUEST_SIGN_IN_LOGIN) {
                //login success
                //get user message by getSignedInAccountFromIntent
                if (signInHuaweiIdTask.isSuccessful()) {
                    SignInHuaweiId huaweiAccount = signInHuaweiIdTask.getResult();

                    WritableMap map = Arguments.createMap();
                    map.putString("displayName", huaweiAccount.getDisplayName());
                    map.putString("idToken", huaweiAccount.getIdToken());
                    map.putString("email", huaweiAccount.getEmail());
                    map.putString("countryCode", huaweiAccount.getCountryCode());
                    map.putString("photoUriString", huaweiAccount.getPhotoUriString());

                    try {
                        map.putString("allData", huaweiAccount.toJson());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPromise.resolve(map);
                    Log.i(Constants.ACCOUNT_TAG, "signIn success " + huaweiAccount.getDisplayName());
                } else {
                    Log.i(Constants.ACCOUNT_TAG, "signIn failed: "
                            + ((ApiException) signInHuaweiIdTask.getException()).getStatusCode());
                }
            }
        }
    };

    // To export a method for JavaScript use, Java methods need to use the @reactmethod annotation
    @ReactMethod
    public void login(Promise promise) {
        this.mPromise = promise;

        this.activity = getCurrentActivity();

        HuaweiIdSignInOptions mSignInOptions = new HuaweiIdSignInOptions
                .Builder(HuaweiIdSignInOptions.DEFAULT_SIGN_IN).requestIdToken("").build();

        this.mSignInClient = HuaweiIdSignIn.getClient(this.activity, mSignInOptions);

        mSignInClient.revokeAccess();

        if (this.checkActivity(this.activity, promise)) {
            try {
                this.activity.startActivityForResult(this.mSignInClient.getSignInIntent(), Constants.REQUEST_SIGN_IN_LOGIN, null);
            } catch (Exception e) {
                promise.reject("START_ACTIVITY_ERROR", "START_ACTIVITY_ERROR");
                promise = null;
            }
        }
    }

    @ReactMethod
    public void logout() {
        if (this.checkActivity(this.activity, null)) {
            this.mSignInClient.signOut();
            Toast.makeText(getReactApplicationContext(), "Your HMS Account has logged out~~", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkActivity(Activity activity, Promise promise) {
        if (activity == null && promise != null) {
            promise.reject("E_ACTIVITY_DOES_NOT_EXIST", "Activity doesn't exist");
        }
        return activity != null;
    }

}
