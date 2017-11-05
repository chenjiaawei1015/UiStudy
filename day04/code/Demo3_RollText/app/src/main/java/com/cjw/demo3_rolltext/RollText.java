package com.cjw.demo3_rolltext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * 滚动的文字
 * Created by Administrator on 2017/11/5.
 */

public class RollText extends AppCompatTextView implements IHandleMessage {

    /**
     * 彩色文字显示的长度
     */
    public static final int LENGTH_ROLL = 4;

    public static final int MESSAGE_CODE_UPDATE = 1;
    /**
     * 刷新时间
     */
    public static final int DELAY_TIME = 20;

    private TextPaint mTextPaint;

    private Matrix mMatrix;

    private WeakHandler<RollText> mWeakHandler;

    /**
     * 偏移值
     */
    private int mTranslateX;

    /**
     * 文本的宽度
     */
    private float mTextWidth;

    /**
     * 滚动文字的宽度
     */
    private float mScrollWidth;

    public RollText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mWeakHandler = new WeakHandler<>(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWeakHandler.sendEmptyMessageDelay(MESSAGE_CODE_UPDATE, DELAY_TIME);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTextPaint = getPaint();

        String text = getText().toString();
        mTextWidth = mTextPaint.measureText(text);

        mScrollWidth = mTextWidth / text.length() * LENGTH_ROLL;

        LinearGradient gradient = new LinearGradient(0, 0, mScrollWidth, 0,
                new int[]{Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE},
                null, Shader.TileMode.CLAMP);
        mMatrix.reset();
        mTextPaint.setShader(gradient).setLocalMatrix(mMatrix);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_CODE_UPDATE:
                mMatrix.reset();
                // 每次偏移 10
                if (mTranslateX + 10 + mScrollWidth < mTextWidth) {
                    mTranslateX = mTranslateX + 10;
                } else {
                    mTranslateX = 0;
                }
                mMatrix.setTranslate(mTranslateX, 0);
                mTextPaint.getShader().setLocalMatrix(mMatrix);
                invalidate();
                break;

            default:
                break;
        }
    }
}
