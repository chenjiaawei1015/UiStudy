package com.cjw.demo1_paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Paint 基本使用
 * Created by Administrator on 2017/10/12.
 */

public class PaintView extends View {

    private IPaintStrategy mPaintStrategy;


    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaintStrategy = new PathDashPathEffectStrategyImpl();
        mPaintStrategy.initPaint(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaintStrategy.onDraw(canvas);
    }

    private interface IPaintStrategy {

        void initPaint(Context context);

        void onDraw(Canvas canvas);
    }

    private class PathDashPathEffectStrategyImpl implements IPaintStrategy {

        private Paint paint;

        @Override
        public void initPaint(Context context) {
            paint = new Paint();
        }

        @Override
        public void onDraw(Canvas canvas) {
            Path path = new Path();
            path.moveTo(100, 600);
            path.lineTo(400, 150);
            path.lineTo(700, 900);
            // canvas.drawPath(path, paint);

            canvas.translate(0, 200);

            /**
             * 利用以另一个路径为单位,延着路径盖章.相当于PS的印章工具
             */
            PathDashPathEffect effect = new PathDashPathEffect(getStampPath(), 35, 0,
                    PathDashPathEffect.Style.MORPH);
            paint.setPathEffect(effect);
            canvas.drawPath(path, paint);
        }

        private Path getStampPath() {
            Path path = new Path();
            path.moveTo(0, 20);
            path.lineTo(10, 0);
            path.lineTo(20, 20);
            path.close();

            path.addCircle(0, 0, 3, Path.Direction.CCW);

            return path;
        }
    }

    private class DiscretePathEffectStrategyImpl implements IPaintStrategy {

        private Paint paint;
        private Path path;

        @Override
        public void initPaint(Context context) {
            paint = new Paint();

            path = new Path();
            // 定义路径的起点
            path.moveTo(0, 0);

            // 定义路径的各个点
            for (int i = 0; i <= 40; i++) {
                double value = RandomUtils.getDouble(0, 1);
                path.lineTo(i * 30, (float) (value * 150));
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
            /**
             * 把原有的路线,在指定的间距处插入一个突刺
             * 第一个这些突出的“杂点”的间距,值越小间距越短,越密集
             * 第二个是突出距离
             */
            canvas.translate(0, 200);
            paint.setPathEffect(new DiscretePathEffect(2, 5));
            canvas.drawPath(path, paint);

            canvas.translate(0, 200);
            paint.setPathEffect(new DiscretePathEffect(6, 5));
            canvas.drawPath(path, paint);


            canvas.translate(0, 200);
            paint.setPathEffect(new DiscretePathEffect(6, 15));
            canvas.drawPath(path, paint);
        }
    }

    private class DashPathEffectStrategyImpl implements IPaintStrategy {

        private Paint paint;
        private Path path;

        @Override
        public void initPaint(Context context) {
            paint = new Paint();

            path = new Path();
            // 定义路径的起点
            path.moveTo(0, 0);

            // 定义路径的各个点
            for (int i = 0; i <= 40; i++) {
                double value = RandomUtils.getDouble(0, 1);
                path.lineTo(i * 30, (float) (value * 150));
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.translate(0, 100);
            paint.setPathEffect(new DashPathEffect(new float[]{15, 20, 15, 15}, 0));
            canvas.drawPath(path, paint);
        }
    }

    /**
     * PathEffect
     */
    private class PathEffectStrategyImpl implements IPaintStrategy {

        private Paint paint;
        private Path path;

        @Override
        public void initPaint(Context context) {
            paint = new Paint();
            path = new Path();
            paint.setStrokeWidth(50);
        }

        @Override
        public void onDraw(Canvas canvas) {
            path.moveTo(100, 600);
            path.lineTo(400, 300);
            path.lineTo(700, 600);

            canvas.drawPath(path, paint);
            paint.setColor(Color.RED);
            paint.setPathEffect(new CornerPathEffect(100));
            canvas.drawPath(path, paint);

            paint.setPathEffect(new CornerPathEffect(200));
            paint.setColor(Color.YELLOW);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * StrokeJoin
     * 连接处的显示
     */
    private class StrokeJoinStrategyImpl implements IPaintStrategy {

        private Paint paint;
        private Path path;

        @Override
        public void initPaint(Context context) {
            paint = new Paint();
            path = new Path();
        }

        @Override
        public void onDraw(Canvas canvas) {
            path.reset();

            paint.setStrokeWidth(40);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);

            path.moveTo(100, 100);
            path.lineTo(450, 100);
            path.lineTo(100, 300);
            paint.setStrokeJoin(Paint.Join.MITER);
            canvas.drawPath(path, paint);

            path.moveTo(100, 400);
            path.lineTo(450, 400);
            path.lineTo(100, 600);
            paint.setStrokeJoin(Paint.Join.BEVEL);
            canvas.drawPath(path, paint);

            path.moveTo(100, 700);
            path.lineTo(450, 700);
            path.lineTo(100, 900);
            paint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 线帽
     */
    private class StrokeCapStrategyImpl implements IPaintStrategy {

        private Paint paint;

        @Override
        public void initPaint(Context context) {
            paint = new Paint();
            paint.setStrokeWidth(50);
        }

        @Override
        public void onDraw(Canvas canvas) {
            paint.setStrokeCap(Paint.Cap.BUTT);
            canvas.drawLine(100, 100, 400, 100, paint);

            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(100, 200, 400, 200, paint);

            paint.setStrokeCap(Paint.Cap.SQUARE);
            canvas.drawLine(100, 300, 400, 300, paint);
        }
    }
}
