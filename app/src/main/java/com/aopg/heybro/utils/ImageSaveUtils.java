package com.aopg.heybro.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by 王伟健 on 2018-06-22.
 */

public class ImageSaveUtils {

    public static String saveImageToExternalStorage(String imageName,Bitmap bitmap) throws Exception {
        //注意小米手机必须这样获得public绝对路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        String fileName = "heybro";
        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File currentFile = new File(appDir, imageName+".jpg");
        FileOutputStream fos = new FileOutputStream(currentFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        return currentFile.getAbsolutePath();
    }
}
