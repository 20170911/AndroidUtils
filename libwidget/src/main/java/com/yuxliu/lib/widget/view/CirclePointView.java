package com.yuxliu.lib.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.yuxliu.lib.widget.R;

/**
 * 圆点
 * Created by yxliu on 2021/10/14.
 */
public class CirclePointView extends View {

    private static final boolean DEFAULT_IS_SOLID = false;
    private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEFAULT_STRIKE_WIDTH = 1;

    private int mWidth;
    private int mHeight;
    private int mCircleColor;

    private float mStrikeWidth;
    private boolean isSolid;

    private Paint mPaint;

    public CirclePointView(Context context) {
        this(context, null);
    }

    public CirclePointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttribute(context, attrs);
        init();
    }

    private void setAttribute(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CirclePointView);
            mCircleColor = array.getColor(R.styleable.CirclePointView_pointColor, DEFAULT_COLOR);
            isSolid = array.getBoolean(R.styleable.CirclePointView_isSolid, DEFAULT_IS_SOLID);
            mStrikeWidth = array.getDimension(R.styleable.CirclePointView_pointStrokeWidth, DEFAULT_STRIKE_WIDTH);
            array.recycle();
        }
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(isSolid ? Paint.Style.FILL : Paint.Style.STROKE);

        if (!isSolid) mPaint.setStrokeWidth(mStrikeWidth);

        mPaint.setColor(mCircleColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int widMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int defaultWidth = (int) getResources().getDisplayMetrics().density * 4;
        if (mWidth == 0 || widMode != MeasureSpec.EXACTLY) {
            mWidth = defaultWidth;
        }
        if (mHeight == 0 || heightMode != MeasureSpec.EXACTLY) {
            mHeight = defaultWidth;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int value = ((Math.min(mWidth, mHeight))) >> 1;
        if (isSolid || mStrikeWidth >= value) { // 画实心圆
            canvas.drawCircle(mWidth >>> 1, mHeight >>> 1, value, mPaint);
            return;
        }
        value = (value - (int) (mStrikeWidth / 2)); // 空心圆
        canvas.drawCircle(mWidth >>> 1, mHeight >>> 1, value, mPaint);
    }

    /**
     * 画实心圆
     */
    public void setSolid() {
        isSolid = true;
        mPaint.setStyle(Paint.Style.FILL);
        invalidate();
    }

    /**
     * 画空心圆，
     *
     * @param strikeWidth 圆环宽度。
     */
    public void setCircleWidth(float strikeWidth) {
        if (strikeWidth == 0) return;
        isSolid = false;
        mPaint.setStyle(Paint.Style.STROKE);
        mStrikeWidth = strikeWidth;
        mPaint.setStrokeWidth(strikeWidth);
        invalidate();
    }

    /**
     * 设置圆的颜色。
     *
     * @param circleColor 颜色。
     */
    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        mPaint.setColor(circleColor);
        invalidate();
    }
}
