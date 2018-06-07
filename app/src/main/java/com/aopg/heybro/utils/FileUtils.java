package com.aopg.heybro.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ${chenyn} on 16/3/14.
 */
public class FileUtils {
    public static final String OUTPUT_PATH = Environment.getExternalStorageDirectory().getPath() + "/JMessageDemo/files/";

    public static String writeFileInAssetsToSDCard(Context mContext, String fileNameInAssets) {
        String outPutPath = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            File outputDir = new File(OUTPUT_PATH);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, fileNameInAssets);
            in = mContext.getAssets().open(fileNameInAssets);
            out = new FileOutputStream(outputFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            outPutPath = outputFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outPutPath;
    }
}