package com.yuxliu.androidutils.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * Created by yxliu on 2021/10/14.
 */
public class ActivityForResult {

    private Activity mActivity;
    private IResultListener mListener;

    private ActivityForResult() {
    }

    public ActivityForResult(Activity activity) {
        mActivity = activity;
    }

    public interface IResultListener {
        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }

    public void startActivityForResult(Intent intent, int requestCode, IResultListener listener) {
        mListener = listener;
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mListener != null) {
            mListener.onActivityResult(requestCode, resultCode, data);
        }
    }
}
