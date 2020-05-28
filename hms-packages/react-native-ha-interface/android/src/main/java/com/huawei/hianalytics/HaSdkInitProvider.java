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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

/**
 * 由于content provider会在app最初建立时就创建，在此处初始化HA SDK可以捕获首次打开事件。
 */
public class HaSdkInitProvider extends ContentProvider {
    HiAnalyticsInstance instance;
    @Override
    public boolean onCreate() {
        Log.i("RNLifeCycle","HaSdkInitProvider onCreate");
        //HiAnalyticsTools.enableLog();
        instance= HiAnalytics.getInstance(this.getContext());
        instance.setAutoCollectionEnabled(true);
        instance.setAnalyticsEnabled(true);
        instance.regHmsSvcEvent();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public void shutdown() {
        Log.i("RNLifeCycle","HaSdkInitProvider shutdown");
        super.shutdown();
    }
}
