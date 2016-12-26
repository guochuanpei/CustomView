package com.bwie.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义水波纹
 * <p>
 * Created by 惠普 on 2016/12/2.
 */

public class MyView extends View {

    public static String TAG = MyView.class.getSimpleName();
    public static int FLUSH_VIEW = 0;
    //定义圆形画笔
    private Paint paint;
    //定义圆形圆点坐标（X轴，Y轴）
    private int cX, cY;
    //设置内圆宽
    private int strokeWidth;
    //设置圆半径
    private int radius;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            flush();
            //重绘方法
            invalidate();
            if (paint.getAlpha() != 0) {
                handler.sendEmptyMessageDelayed(FLUSH_VIEW, 1000);
            }
        }
    };

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化，重置方法
    private void init() {
        //初始化画笔
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置画笔颜色
        paint.setColor(Color.RED);
        //设置内圆宽度
        paint.setStrokeWidth(strokeWidth);
        //设置空心
        paint.setStyle(Paint.Style.STROKE);
        //设置透明度
        paint.setAlpha(255);
        //因为是重置方法，所以执行完过后要归零
        strokeWidth = 0;
        radius = 0;
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cX, cY, radius, paint);
    }
    //触摸事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cX = (int) event.getX();
                cY = (int) event.getY();
                Log.i(TAG, cX + " " + cY);
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                cX = (int) event.getX();
                cY = (int) event.getY();
                Log.i(TAG, cX + " " + cY);
                break;
            default:
                break;
        }
        //当触摸时获得圆心后，重置,也就是初始化画笔，这是cX，cY已经被赋上值,
        init();
        //handler不断发送实现扩散效果
        handler.sendEmptyMessage(FLUSH_VIEW);
        //设置为true,响应触摸事件
        return true;
    }

    //实现动画效果所以要不断刷新
    private void flush() {
        //handler执行一次半径加一次
        radius += 20;
        //内圆宽设为半径的四分之一
        strokeWidth = radius / 4;
        paint.setStrokeWidth(strokeWidth);
        int alpha = paint.getAlpha() - 20;
        if (alpha < 20) {
            alpha = 0;
        }
        paint.setAlpha(alpha);
    }
}
