package com.yuxliu.androidutils.utils.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;

import com.yuxliu.androidutils.utils.FileUtils;

import java.io.File;

/**
 * 截屏
 * Created by YuxLiu on 2021/8/26.
 */
public class ScreenshotUtils {
    /**
     * 截屏
     *
     * @param activity Current activity
     * @return Bitmap
     */
    public static Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        return activity.getWindow().getDecorView().getDrawingCache();
    }

    public static void doScreenshot(Activity activity) {
        Bitmap bitmap = captureScreen(activity);
        if (bitmap != null) {
            String dirPath = FileUtils.getExternalCacheDirectory(activity, "PIC").getAbsolutePath();
            String filename = dirPath + File.separator + System.currentTimeMillis() + ".png";
            FileUtils.saveFile(bitmap, filename);
        }
    }
}
