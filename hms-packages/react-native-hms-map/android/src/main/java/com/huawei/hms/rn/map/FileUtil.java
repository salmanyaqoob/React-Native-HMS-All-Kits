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

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.facebook.common.logging.FLog;
import com.facebook.react.common.ReactConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileUtil extends AsyncTask<String, Void, InputStream> {

    private final String NAME = "FileUtil";
    private final String TEMP_FILE_SUFFIX = "temp";
  
    private Exception exception;
    private Context context;
  
    public FileUtil(Context context) {
        super();
      
        this.context = context;
    }
  
    protected InputStream doInBackground(String... urls) {
        try {
            Uri fileContentUri = Uri.parse(urls[0]);
          
            if (fileContentUri.getScheme().startsWith("http")) {
                return getDownloadFileInputStream(context, fileContentUri);
            }
            return context.getContentResolver().openInputStream(fileContentUri);
        } catch (Exception e) {
            this.exception = e;
            FLog.e(
                ReactConstants.TAG,
                "Could not retrieve file for contentUri " + urls[0],
                e);
            return null;
        }
    }
  
    private InputStream getDownloadFileInputStream(Context context, Uri uri)
        throws IOException {
        final File outputDir = context.getApplicationContext().getCacheDir();
        final File file = File.createTempFile(NAME, TEMP_FILE_SUFFIX, outputDir);
        file.deleteOnExit();
      
        final URL url = new URL(uri.toString());
        final InputStream is = url.openStream();
        try {
            final ReadableByteChannel channel = Channels.newChannel(is);
            try {
                final FileOutputStream stream = new FileOutputStream(file);
                try {
                    stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                    return new FileInputStream(file);
                } finally {
                    stream.close();
                }
            } finally {
                channel.close();
            }
        } finally {
            is.close();
        }
    }
}
