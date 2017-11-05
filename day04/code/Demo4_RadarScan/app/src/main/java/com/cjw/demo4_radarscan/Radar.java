package com.cjw.demo4_radarscan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 雷达图
 * Created by Administrator on 2017/11/5.
 */

public class Radar extends View implements IHandleMessage {

    public static final int MESSAGE_CODE_UPDATE = 1;
    public static final int DELAY_TIME = 20;

    private Paint mPaint;

    private int[] mColorArr = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.GRAY};

    private WeakHandler mWeakHandler;

    private int mRotateAngle;
    private final Matrix mMatrix;

    public Radar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWeakHandler = new WeakHandler<>(this);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        canvas.translate(width / 2, height / 2);
        canvas.rotate(mRotateAngle);
        canvas.drawCircle(0, 0, width / 2, mPaint);

        mWeakHandler.sendEmptyMessageDelay(MESSAGE_CODE_UPDATE, DELAY_TIME);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        SweepGradient gradient = new SweepGradient(0, 0, mColorArr, null);
        mPaint.setShader(gradient);
    }

    @Override
    public void handleMessage(Message msg) {
        mWeakHandler.removeMessage(msg.what);
        switch (msg.what) {
            case MESSAGE_CODE_UPDATE:
                mRotateAngle += 3;
                mRotateAngle = mRotateAngle % 360;
                invalidate();
                break;

            default:
                break;
        }
    }
}
