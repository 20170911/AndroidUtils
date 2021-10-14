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

import com.yuxliu.androidutils.activity.BaseActivity;
import com.yuxliu.androidutils.service.ScreenRecorderService;
import com.yuxliu.androidutils.utils.screenshot.ScreenshotUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> {
            ScreenshotUtils.sysScreenshot(this, 1);
        });
    }
}