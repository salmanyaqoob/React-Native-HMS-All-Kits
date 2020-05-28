/**
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 

   Licensed under the Apache License, Version 2.0 (the "License");

   you may not use this file except in compliance with the License.

   You may obtain a copy of the License at

 

     http://www.apache.org/licenses/LICENSE-2.0

 

   Unless required by applicable law or agreed to in writing, software

   distributed under the License is distributed on an "AS IS" BASIS,

   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

   See the License for the specific language governing permissions and

   limitations under the License.
*/

package com.huawei.hianalytics;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.analytics.HiAnalytics;

/**
 * 用于在MainActivity销毁时注销HA SDK的service，防止内存泄漏。
 */
public class LifeCycleListener implements Application.ActivityLifecycleCallbacks {
    Application application;
    static LifeCycleListener instance;
    private LifeCycleListener(Application application){
        this.application=application;
        application.registerActivityLifecycleCallbacks(this);
    }

    public static void init(Application application){
        if (null==instance){
            instance=new LifeCycleListener(application);
        }else{
            Log.i( "RNLifeCycle","LifeCycleListener already initiated");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i( "RNLifeCycle","onActivityCreated:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i( "RNLifeCycle","onActivityStarted:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i( "RNLifeCycle","onActivityResumed:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i( "RNLifeCycle","onActivityPaused:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i( "RNLifeCycle","onActivityStopped:"+activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.i( "RNLifeCycle","onActivitySaveInstanceState:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i( "RNLifeCycle","onActivityDestroyed:"+activity.getLocalClassName());
        if (activity.getLocalClassName().equals("MainActivity")){
            HiAnalytics.getInstance(activity.getApplicationContext()).unRegHmsSvcEvent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i( "RNLifeCycle","finishAndRemoveTask");
                activity.finishAndRemoveTask();
            }
        }
    }
}
