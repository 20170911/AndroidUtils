package com.yuxliu.androidutils.utils.screenshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;

import com.yuxliu.androidutils.activity.BaseActivity;
import com.yuxliu.androidutils.service.ScreenRecorderService;
import com.yuxliu.androidutils.utils.FileUtils;

import java.io.File;

import static android.app.Activity.RESULT_OK;

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

    public static void sysScreenshot(BaseActivity activity, int requestCode) {
        MediaProjectionManager manager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //启动截屏Activity【com.android.systemui.media.MediaProjectionPermissionActivity】
        activity.startActivityForResult(manager.createScreenCaptureIntent(), requestCode, (requestCode1, resultCode, data) -> {
            if (resultCode == RESULT_OK && requestCode1 == requestCode) {
                Intent service = new Intent(activity, ScreenRecorderService.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                activity.startService(service);
            }
        });
    }
}
