package com.yuxliu.androidutils.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by yxliu on 2021/10/14.
 */
public class BaseActivity extends AppCompatActivity {

    protected ActivityForResult mActivityForResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mActivityForResult != null) {
            mActivityForResult.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void startActivityForResult(@Nullable Intent data, int requestCode, ActivityForResult.IResultListener listener) {
        if (mActivityForResult != null) {
            mActivityForResult.startActivityForResult(data, requestCode, listener);
        }
    }

    private void init() {
        mActivityForResult = new ActivityForResult(this);
    }

    private void release() {
        mActivityForResult = null;
    }
}
