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

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.hms.analytics.type.HAEventType;
import com.huawei.hms.analytics.type.HAParamType;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HaInterfaceModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final HiAnalyticsInstance instance;
    private final String TAG="RNHASDK";
    private Map<String, Object> haParamTypeMap;

    public HaInterfaceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        Log.i( "RNLifeCycle","Module construct");
        LifeCycleListener.init((Application) reactContext.getApplicationContext());
        this.reactContext = reactContext;
        this.instance=HiAnalytics.getInstance(reactContext);
        haParamTypeMap=getHAParams();
    }

    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return getHAEventType(getHAParams(constants));
    }

    private Map<String, Object> getHAParams(Map<String, Object> map){
        Map<String, Object> transMap=map;
        Field[] fields = HAParamType.class.getDeclaredFields();
        if(fields != null){
            for(Field fie : fields){
                try {
                    transMap.put(fie.getName(),fie.get(fie.getName()));
                    Log.i(TAG,fie.getName()+": "+fie.get(fie.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return transMap;
    }



    /**
    打开SDK的控制台日志，默认是debug级别
     */
    @ReactMethod
    public void enableLog(){
        Log.i("TAG","enable log, default level.");
        HiAnalyticsTools.enableLog();
    }

    /**
    打开SDK的控制台日志,支持传入日志的级别
    @param level 日志的级别，取值包括“debug”、“info”、“warn”和“error”，区分大小写
     */
    @ReactMethod
    public void enableLogWithLevel(String level) {
        if (!level.equals("debug") && !level.equals("info") && !level.equals("warn") && !level.equals("error")) {
            Log.i("TAG", "enable log, parameter is wrong.");
            return;
        }

        switch (level) {
            case "debug":
                Log.i("TAG", "enable log, level: debug.");
                HiAnalyticsTools.enableLog(3);
                break;
            case "info":
                Log.i("TAG", "enable log, level: info.");
                HiAnalyticsTools.enableLog(4);
                break;
            case "warn":
                Log.i("TAG", "enable log, level: warn.");
                HiAnalyticsTools.enableLog(5);
                break;
            case "error":
                Log.i("TAG", "enable log, level: error.");
                HiAnalyticsTools.enableLog(6);
                break;
            default:
                HiAnalyticsTools.enableLog(3);
                break;
        }
    }
	    private Map<String, Object> getHAParams(){
        Map<String, Object> transMap=new HashMap<>();
        Field[] fields = HAParamType.class.getDeclaredFields();
        if(fields != null){
            for(Field fie : fields){
                try {
                    transMap.put(fie.getName(),fie.get(fie.getName()));
                    Log.i(TAG,fie.getName()+": "+fie.get(fie.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return transMap;
    }
	
	private Map<String, Object> getHAEventType(Map<String, Object> map){
        Map<String, Object> transMap=map;
        Field[] fields = HAEventType.class.getDeclaredFields();
        if(fields != null){
            for(Field fie : fields){
                try {
                    transMap.put(fie.getName(),fie.get(fie.getName()));
                    Log.i(TAG,fie.getName()+": "+fie.get(fie.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return transMap;
    }

    @Override
    public String getName() {
        return "HaInterface";
    }

    /**
     * 上报自定义事件到AGC
     * @param event 事件名称
     * @param contentMap 事件自定义参数。map中可以存放类型boolean,double, String以及Map。由于js中的
     *                   Array类型可以存放多种数据类型，与Java中不同，所以暂不支持该数据类型。
     */
    @ReactMethod
    public void onEvent(String event,ReadableMap contentMap){
        Log.i( TAG,"onEvent:"+event);
        Bundle bundle=new Bundle();
        saveMapToBundle(contentMap,bundle);
        instance.onEvent(event,bundle);
    }

    private String transToHaParamType(String key){
        if (null!=haParamTypeMap.get(key)){
            Log.i(TAG,"find HAParamType: "+key+", transform to: "+haParamTypeMap.get(key));
            return (String)haParamTypeMap.get(key);
        }else{
            return key;
        }
    }

    /**
     * 将react-native的ReadableMap转化为bundle用于HA SDK的onEvent接口上报。
     * @param map
     * @param bundle
     */
    private void saveMapToBundle(ReadableMap map,Bundle bundle) {
        if (null != map) {
            ReadableMapKeySetIterator keySetIterator = map.keySetIterator();
            while (keySetIterator.hasNextKey()) {
                String key = keySetIterator.nextKey();
                switch (map.getType(key)) {
                    case Null:
                        //do nothing
                        break;
                    case Boolean:
                        bundle.putBoolean(transToHaParamType(key), map.getBoolean(key));
                        break;
                    case Number:
                        bundle.putDouble(transToHaParamType(key), map.getDouble(key));
                        break;
                    case String:
                        bundle.putString(transToHaParamType(key), map.getString(key));
                        break;
                    case Map:
                        //not supported in AGC
                        break;
                    case Array:
                        //not supported in JAVA
                        break;
                    default: break;
                }
            }
        }
    }
    /**
     * 设置当前页面的名称，可以在非Activity跳转时获取不同页面的对应名称。
     * @param screenName 当前页面的名称
     * @param screenClassOverride 默认为MainActivity，可以重命名
     */
    @ReactMethod
    public void setCurrentScreen(String screenName,String screenClassOverride){
        Log.i(TAG,"setCurrentScreen: screenName:"+screenName+" currentScreen:"+screenClassOverride);
        instance.setCurrentActivity(getCurrentActivity(),screenName,screenClassOverride);
    }

    /**
     * 设置用户ID,在分析的服务侧通过此标识进行关联用户数据.使用userID需要遵守相关隐私规范.
     * @param id
     */
    @ReactMethod
    public void setUserId(String id){
        Log.d(TAG,"Set user id, at " + new Date().toString());
        instance.setUserId(id);
    }

    /**
     * 设置用户属性。用户属性值将在整个应用程序生命周期和会话期间保持不变。
     * @param name
     * @param value
     */
    @ReactMethod
    public void setUserProfile(String name, String value){
        Log.d(TAG, "Set user profile, key: " + name + ", value: " + value);
        instance.setUserProfile(name, value);
    }

    /**
     * 清除本地缓存的所有采集数据，包括发送失败的缓存数据
     */
    @ReactMethod
    public void clearCachedData(){
        Log.d(TAG, "Clear cache data at " + new Date().toString());
        instance.clearCachedData();
    }

    /**
     * AGC服务中获取App Instance ID
     * @return
     */
    @ReactMethod
    public void getAAID(final Promise promise){
        final Task<String> myTask = instance.getAAID();

        myTask.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                final String successStr = s;
                promise.resolve(successStr);
                Log.d(TAG, "Get AAID success!");
            }
        });

        myTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                final String failureStr = "";
                promise.resolve(failureStr);
                Log.e(TAG, "Get AAID failure!");
            }
        });
    }

    /**
     * 设置重新开始新Session的最短时间间隔。用于指定App在后台运行多长后，再次切回前台时，需要生成新的会话。默认30秒。
     * @param milliseconds
     */
    @ReactMethod
    public void setMinActivitySessions(int milliseconds){
        Log.d(TAG, "Set minimum session refresh time: " + milliseconds);
        instance.setMinActivitySessions(milliseconds);
    }

    /**
     * 设置Session超时时长。App一直在前台运行，当两个相邻事件的时间间隔超过此方法设置的阈值时，将生成一个新的会话。默认30分钟。
     * @param milliseconds
     */
    @ReactMethod
    public void setSessionDuration(int milliseconds){
        Log.d(TAG, "Set session duration time: " + milliseconds);
        instance.setSessionDuration(milliseconds);
    }

}
