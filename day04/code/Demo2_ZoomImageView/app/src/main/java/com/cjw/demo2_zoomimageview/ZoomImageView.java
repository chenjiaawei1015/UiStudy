package com.cjw.demo2_zoomimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 放大镜效果
 * Created by Administrator on 2017/11/2.
 */

public class ZoomImageView extends View {

    // 放大系数
    private static final int SCALE = 3;
    // 半径
    public static final int RADIUS = 50;

    private Bitmap mBitmap;

    private final ShapeDrawable mDrawable;

    private Paint mPaint;

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xyjy);

        Matrix matrix = new Matrix();
        matrix.setScale(SCALE, SCALE);

        Bitmap scaleBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), matrix, true);

        BitmapShader bitmapShader = new BitmapShader(scaleBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setShader(bitmapShader);
        mDrawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        mDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        Matrix matrix = new Matrix();
        matrix.setTranslate(RADIUS - x * SCALE, RADIUS - y * SCALE);
        mDrawable.getPaint().getShader().setLocalMatrix(matrix);

        mDrawable.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
        invalidate();
        return true;
    }
}
