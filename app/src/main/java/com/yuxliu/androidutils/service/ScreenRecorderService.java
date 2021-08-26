package com.yuxliu.androidutils.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yuxliu.androidutils.MainActivity;
import com.yuxliu.androidutils.R;
import com.yuxliu.androidutils.utils.FileUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ScreenRecorderService extends Service {
    public static final String TAG = ScreenRecorderService.class.getSimpleName();
    private MediaProjection mMediaProjection;
    private ImageReader imageReader;
    private VirtualDisplay mDisplay;

    public ScreenRecorderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 必须创建通知
        createNotificationChannel();
        // 接收参数
        Intent resultData = intent.getParcelableExtra("data");
        int resultCode = intent.getIntExtra("code", -1);
        // 获取系统屏幕的宽、高、DPI
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int dpi = metrics.densityDpi;

        MediaProjectionManager manager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaProjection = manager.getMediaProjection(resultCode, Objects.requireNonNull(resultData));
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
        mDisplay = mMediaProjection.createVirtualDisplay("screenshot"
                , width
                , height
                , dpi
                , DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR
                , imageReader.getSurface()
                , null
                , null);

        new Handler(Looper.myLooper()).postDelayed(() -> {
            // 截图的屏幕
            Bitmap bitmap = takeScreenShot();
            // 保存到本地
            String dirPath = FileUtils.getExternalCacheDirectory(ScreenRecorderService.this, "PIC").getAbsolutePath();
            String filename = dirPath + File.separator + System.currentTimeMillis() + ".png";
            FileUtils.saveFile(bitmap, filename);
            Log.d(TAG, filename);
        }, 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class); //点击后跳转的界面，可以设置跳转数据

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("SMI InstantView") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("is running......") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap takeScreenShot() {
        Bitmap bitmap = null;
        try {
            Image image = imageReader.acquireLatestImage();
            int width = image.getWidth();
            int height = image.getHeight();
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageReader != null) {
                imageReader.close();
            }
            if (mMediaProjection != null) {
                mMediaProjection.stop();
            }
            if (mDisplay != null) {
                mDisplay.release();
            }
        }
        return bitmap;
    }
}