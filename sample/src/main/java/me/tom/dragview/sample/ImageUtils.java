package me.tom.dragview.sample;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {

    public static String saveToFile(Context context, Bitmap bitmap) {
        String filePath = FileUtils.getTempFilePath(context, System.currentTimeMillis() + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(new File(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return filePath;
        } catch (IOException e) {
            return null;
        }
    }
}
