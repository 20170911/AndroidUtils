package com.yuxliu.lib.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.yuxliu.lib.widget.R;

/**
 * 环形进度条
 * Created by yxliu on 2021/9/28.
 */
public class CircleProgressBar extends View {

    public static final int STROKE = 0;
    public static final int FILL = 1;

    // 矩形
    private Rect mRect;
    // 画笔
    private Paint mPaint;
    // 圆环宽度
    private int mCircleWidth;
    // 圆环颜色
    private int mCircleColor;
    // 加载进度圆弧扫过的颜色
    private int mLoadedColor;
    // 百分比文本颜色
    private int mTextColor;
    // 百分比文本大小
    private int mTextSize;
    // 进度
    private int mProgress;
    // 最大进度
    private int mProgressMax;
    // 类型：0代表空心  1代表实心
    private int mProgressType;
    // 是否显示百分比文本
    private boolean mIsShowText;
    // 是否使用双色
    private boolean mUseLoadedColor;
    // 是否是逆时针
    private boolean mUseClockwise;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttribute(context, attrs, defStyleAttr);
        init();
    }

    private void setAttribute(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.CircleProgressBar_circleColor) {
                mCircleColor = ta.getColor(index, Color.GREEN);
            } else if (index == R.styleable.CircleProgressBar_circleWidth) {
                mCircleWidth = ta.getDimensionPixelSize(index
                        , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX
                                , 10, getResources().getDisplayMetrics()));
            } else if (index == R.styleable.CircleProgressBar_textColor) {
                mTextColor = ta.getColor(index, Color.BLACK);
            } else if (index == R.styleable.CircleProgressBar_textSize) {
                mTextSize = ta.getDimensionPixelSize(index
                        , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX
                                , 30, getResources().getDisplayMetrics()));
            } else if (index == R.styleable.CircleProgressBar_isShowText) {
                mIsShowText = ta.getBoolean(index, false);
            } else if (index == R.styleable.CircleProgressBar_loadedColor) {
                mLoadedColor = ta.getColor(index, Color.GRAY);
            } else if (index == R.styleable.CircleProgressBar_progressVal) {
                mProgress = ta.getInt(index, 0);
            } else if (index == R.styleable.CircleProgressBar_progressMax) {
                mProgressMax = ta.getInt(index, 100);
            } else if (index == R.styleable.CircleProgressBar_progressType) {
                mProgressType = ta.getInt(index, 0);
            } else if (index == R.styleable.CircleProgressBar_useLoadedColor) {
                mUseLoadedColor = ta.getBoolean(index, true);
            } else if (index == R.styleable.CircleProgressBar_useClockwise) {
                mUseClockwise = ta.getBoolean(index, true);
            }
        }
        ta.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mRect = new Rect();

        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds("%", 0, "%".length(), mRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆环
        // 获取圆形坐标
        int centre = getWidth() >> 1;
        // 获取半径
        int radius = centre - (mCircleWidth >> 1);

        // 设置空心
        mPaint.setStyle(Paint.Style.STROKE);
        // 设置圆角
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置抖动
        mPaint.setDither(true);
        // 设置画笔宽度
        mPaint.setStrokeWidth(mCircleWidth);
        // 设置画笔颜色
        mPaint.setColor(mCircleColor);

        if (mUseLoadedColor) {
            // 先画圆环
            canvas.drawCircle(centre, centre, radius, mPaint);
            // 再画圆弧，进度
            // 设置进度颜色
            mPaint.setColor(mLoadedColor);
        }
        mPaint.setStrokeWidth(mCircleWidth);

        float sweepAngle = mProgress * 360 / mProgressMax;
        if (!mUseClockwise) {
            // 正数表示顺时针，负数逆时针
            sweepAngle = -sweepAngle;
        }

        switch (mProgressType) {
            case STROKE:
                mPaint.setStyle(Paint.Style.STROKE);
                // 用于定义的圆弧的形状和大小的界限
                @SuppressLint("DrawAllocation") RectF ovalStroke =
                        new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
                // startAngle：从-90°开始，也就是钟表的12点钟位置。
                // sweepAngle：圆弧扫过的角度
                // useCenter：设置我们的圆弧在绘画的时候，是否经过圆形，当Paint.Style.STROKE时，true无效果
                canvas.drawArc(ovalStroke, -90, sweepAngle, false, mPaint);
                break;

            case FILL:
                mPaint.setStyle(Paint.Style.FILL);
                // 用于定义的圆弧的形状和大小的界限
                @SuppressLint("DrawAllocation") RectF ovalFill =
                        new RectF(centre - radius - (mCircleWidth >> 1), centre - radius - (mCircleWidth >> 1),
                        centre + radius + (mCircleWidth >> 1), centre + radius + (mCircleWidth >> 1));
                canvas.drawArc(ovalFill, -90, sweepAngle, true, mPaint);
                break;
        }

        // 如果不显示文本，直接return
        if (!mIsShowText) return;
        // 计算圆弧进度获取文本内容
        int percentContext = (int) ((float) mProgress / (float) mProgressMax * (float) 100);
        // 设置绘制文本画笔颜色
        mPaint.setColor(mTextColor);
        // 设置绘制文本画笔风格
        mPaint.setStyle(Paint.Style.FILL);
        // 设置绘制文本画笔宽度，可添加自定义属性
        mPaint.setStrokeWidth(3);
        // 测量文本宽度
        float measureTextWidth = mPaint.measureText(percentContext + "%");
        canvas.drawText(percentContext + "%", centre - measureTextWidth / 2, centre + (mRect.height() >> 1), mPaint);
    }

    public synchronized void setProgress(final int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    public synchronized int getProgress() {
        return mProgress;
    }

    public void setProgressMax(final int progressMax) {
        mProgressMax = progressMax;
    }

    public int getProgressMax() {
        return mProgressMax;
    }

    public void setCircleColor(@ColorRes int resId) {
        mCircleColor = resId;
    }

    public void setCircleWidth(int width) {
        mCircleWidth = width;
    }
}
