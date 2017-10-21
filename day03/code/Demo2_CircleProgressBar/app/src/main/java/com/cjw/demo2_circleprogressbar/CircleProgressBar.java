package com.cjw.demo2_circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * 圆形进度条
 * Created by Administrator on 2017/10/20.
 */

public class CircleProgressBar extends View {

    private static final int MESSAGE_CODE_UPDATE_PROGRESS = 1;

    // 圆环最大的数值
    private int mMaxValue;
    // 圆环颜色
    private int mRoundColor;
    // 进度圆环颜色
    private int mRoundProgressColor;
    // 文本颜色
    private int mTextColor;
    // 文本大小
    private float mTextSize;
    // 线宽
    private float mRoundWidth;
    // 文字是否显示
    private boolean mIsTextShow;

    // 当前进度
    private int mCurrentProgress;
    // 最终进度
    private int mLastProgress;

    // 绘制默认圆的画笔
    private Paint mCircleRoundPaint;
    // 绘制文本的画笔
    private Paint mTextPaint;
    // 绘制圆弧的画笔
    private Paint mArcPaint;
    // 圆弧矩形
    private final RectF mArcRectF;

    private Handler mCircleViewHandler;


    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.CircleProgressBar);
        mMaxValue = typedArray.getInteger(R.styleable.CircleProgressBar_max, 100);
        mRoundColor = typedArray.getColor(R.styleable.CircleProgressBar_roundColor, Color.RED);
        mRoundProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_roundProgressColor,
                Color.BLUE);
        mRoundWidth = typedArray.getDimension(R.styleable.CircleProgressBar_roundWidth, 10);

        mTextColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.GREEN);
        mTextSize = typedArray.getDimension(R.styleable.CircleProgressBar_textSize, 55);
        mIsTextShow = typedArray.getBoolean(R.styleable.CircleProgressBar_textShow, true);
        typedArray.recycle();

        mCircleRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleRoundPaint.setStrokeWidth(mRoundWidth);
        mCircleRoundPaint.setColor(mRoundColor);
        mCircleRoundPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStrokeWidth(mRoundWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setColor(mRoundProgressColor);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mArcRectF = new RectF();

        mCircleViewHandler = new CircleViewHandler(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        // 绘制圆环
        float roundCenterX = 1.0f * width / 2;
        float roundCenterY = 1.0f * height / 2;
        float radius = 1.0f * width / 2 - mRoundWidth / 2;
        canvas.drawCircle(roundCenterX, roundCenterY, radius, mCircleRoundPaint);

        // 绘制文本
        if (mIsTextShow) {
            String text = String.valueOf(mCurrentProgress);
            float textWidth = mTextPaint.measureText(text);

            float textX = roundCenterX - textWidth / 2;

            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            float textY = roundCenterX - (fm.top + fm.bottom) / 2;
            canvas.drawText(text, textX, textY, mTextPaint);
        }

        // 绘制圆弧
        mArcRectF.top = mRoundWidth / 2;
        mArcRectF.bottom = width - mRoundWidth / 2;
        mArcRectF.left = mRoundWidth / 2;
        mArcRectF.right = height - mRoundWidth / 2;

        float sweepAngle = mCurrentProgress * 1.0f / mMaxValue * 360;
        canvas.drawArc(mArcRectF, -90f, sweepAngle, false, mArcPaint);

        if (mCurrentProgress != mLastProgress) {
            mCircleViewHandler.sendEmptyMessageDelayed(MESSAGE_CODE_UPDATE_PROGRESS, 20);
        }
    }

    public void updateProgress(int progress) {
        mLastProgress = progress;
        mCircleViewHandler.sendEmptyMessage(MESSAGE_CODE_UPDATE_PROGRESS);
    }

    private void handleMessage(Message msg) {
        mCircleViewHandler.removeMessages(MESSAGE_CODE_UPDATE_PROGRESS);

        if (mCurrentProgress == mLastProgress) {
            return;
        }

        int temp = mLastProgress - mCurrentProgress;
        if (temp > 0) {
            mCurrentProgress++;
        } else {
            mCurrentProgress--;
        }

        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }

        mCircleViewHandler.sendEmptyMessageDelayed(MESSAGE_CODE_UPDATE_PROGRESS, 20);
    }

    // 防止内存泄露,自定义Handler
    private static class CircleViewHandler extends Handler {

        WeakReference<CircleProgressBar> progressRef;

        CircleViewHandler(CircleProgressBar progressBar) {
            this.progressRef = new WeakReference<>(progressBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CircleProgressBar circleProgressBar = progressRef.get();
            if (circleProgressBar != null) {
                circleProgressBar.handleMessage(msg);
            }
        }
    }
}
