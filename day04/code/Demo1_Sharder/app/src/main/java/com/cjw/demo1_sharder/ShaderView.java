package com.cjw.demo1_sharder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 演示Shader用法
 * Created by Administrator on 2017/10/24.
 */

public class ShaderView extends View {

    private IShader mShaderImpl;

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mShaderImpl = new ComposeShaderImpl();
        mShaderImpl.init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mShaderImpl.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShaderImpl.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * ComposeShader 组合渐变
     */
    private class ComposeShaderImpl implements IShader {

        Paint paint;
        int[] colorArr = {Color.RED, Color.GREEN, Color.BLUE};
        Bitmap srcBitmap;

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    context, R.drawable.heart);
            srcBitmap = bitmapDrawable.getBitmap();
        }

        @Override
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            canvas.drawRect(0, 0, width, height, paint);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            Shader gradient = new LinearGradient(0, 0, w / 2, h / 2, colorArr, null,
                    Shader.TileMode.CLAMP);

            BitmapShader shader = new BitmapShader(srcBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            ComposeShader composeShader = new ComposeShader(gradient, shader,
                    PorterDuff.Mode.DST_IN);
            paint.setShader(composeShader);
        }
    }

    /**
     * SweepGradient 梯度渐变
     */
    private class SweepGradientImpl implements IShader {

        Paint paint;
        int[] colorArr = {Color.RED, Color.GREEN, Color.BLUE};

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            Shader shader = new SweepGradient(w / 2, h / 2, colorArr, null);
            paint.setShader(shader);
        }
    }

    /**
     * RadialGradient 演示
     */
    private class RadialGradientImpl implements IShader {

        Paint paint;
        int[] colorArr = {Color.RED, Color.GREEN, Color.BLUE};

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            Shader shader = new RadialGradient(w / 2, h / 2, w / 2, colorArr,
                    null, Shader.TileMode.REPEAT);
            paint.setShader(shader);
        }
    }

    /**
     * LinearGradient 线性渐变演示
     */
    private class LinearGradientImpl implements IShader {

        Paint paint;
        int[] colorArr = {Color.RED, Color.GREEN, Color.BLUE};

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            canvas.drawRect(0, 0, width, height, paint);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            Shader gradient = new LinearGradient(0, 0, w, h, colorArr, null,
                    Shader.TileMode.CLAMP);
            paint.setShader(gradient);
        }
    }

    /**
     * ShapeDrawable 演示
     */
    private class ShapeDwImpl implements IShader {

        Paint paint;
        Bitmap srcBitmap;
        private ShapeDrawable sdw;

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    context, R.drawable.xyjy3);
            srcBitmap = bitmapDrawable.getBitmap();

            BitmapShader shader = new BitmapShader(srcBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

            // 指定为椭圆形状
            sdw = new ShapeDrawable(new OvalShape());
            sdw.getPaint().setShader(shader);
        }

        @Override
        public void onDraw(Canvas canvas) {
            // 在指定区域获取内切椭圆,然后进行绘制
            sdw.setBounds(0, 0, getWidth(), getHeight());
            sdw.draw(canvas);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {

        }
    }

    /**
     * TileMode 演示
     */
    private class TileModeImpl implements IShader {

        Paint paint;
        Bitmap srcBitmap;

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    context, R.drawable.xyjy3);
            srcBitmap = bitmapDrawable.getBitmap();

            // BitmapShader shader = new BitmapShader(srcBitmap,
            //         Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            // BitmapShader shader = new BitmapShader(srcBitmap,
            //         Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);

            BitmapShader shader = new BitmapShader(srcBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paint.setShader(shader);
        }

        @Override
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();

            canvas.drawRect(0, 0, width, height, paint);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {

        }
    }

    /**
     * 通过 BitmapShaderImpl 演示绘制圆形头像
     */
    private class BitmapShaderImpl implements IShader {

        Paint paint;
        Bitmap srcBitmap;

        @Override
        public void init(Context context) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    context, R.drawable.xyjy3);
            srcBitmap = bitmapDrawable.getBitmap();

            BitmapShader shader = new BitmapShader(srcBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
        }

        @Override
        public void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();

            int min = Math.min(width, height);

            canvas.drawCircle(width / 2, height / 2, min / 2, paint);
        }

        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh) {

        }
    }

    private interface IShader {
        void init(Context context);

        void onDraw(Canvas canvas);

        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

}
