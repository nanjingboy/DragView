package me.tom.dragview.sample;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static String getTempFilePath(Context context, String fileName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(FileUtils.getTempDirPath(context));
        buffer.append("/");
        buffer.append(fileName);
        return buffer.toString();
    }

    public static String getTempDirPath(Context context) {
        File cache;
        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            buffer.append("/Android/data/");
            buffer.append(context.getPackageName());
            buffer.append("/cache/");
            cache = new File(buffer.toString());
        } else {
            // Use internal storage
            cache = context.getCacheDir();
        }
        cache.mkdirs();
        return cache.getAbsolutePath();
    }
}