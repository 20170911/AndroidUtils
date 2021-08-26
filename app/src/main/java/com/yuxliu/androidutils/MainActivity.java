package com.yuxliu.androidutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.yuxliu.androidutils.service.ScreenRecorderService;
import com.yuxliu.androidutils.utils.screenshot.ScreenshotUtils;

public class MainActivity extends AppCompatActivity {
    private MediaProjectionManager mMediaProjectManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> {
            // 方法一：View截图不包含状态栏
            ScreenshotUtils.doScreenshot(this);

            // 方法二：系统截图，包含状态栏
            mMediaProjectManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            //启动截屏Activity【com.android.systemui.media.MediaProjectionPermissionActivity】
            startActivityForResult(mMediaProjectManager.createScreenCaptureIntent(), 200);
        });
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            Intent service = new Intent(this, ScreenRecorderService.class);
            service.putExtra("code", resultCode);
            service.putExtra("data", data);
            startForegroundService(service);
        }
    }
}