package com.lazylibs.webviewer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class LazyLoadingView extends View {

    public LazyLoadingView(Context context) {
        super(context);
    }

    public LazyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LazyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LazyLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int rotatTime = 1000;
    private float paintStroke = 25f;
    private RectF arcRectF = new RectF();
    private Paint paint;
    private float progress;
    private boolean isStart;

    private void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.1f, 1f);
        valueAnimator.setDuration(rotatTime);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        // 旋转动画
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        rotationAnimator.setDuration(rotatTime);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ValueAnimator.RESTART);

        final AnimatorSet rotateAnimationSet = new AnimatorSet();
        rotateAnimationSet.playTogether(valueAnimator);
        rotateAnimationSet.playTogether(rotationAnimator);
        rotateAnimationSet.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 初始化参数
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(paintStroke);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(Color.RED);

            int width = getWidth();
            int height = getHeight();
            float centerX = width / 2f;
            float centerY = height / 2f;
            float radius = Math.min(width, height) / 2f - paintStroke;
            arcRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        }
        float sweep = 75 * progress;
        //第一个圆弧
        canvas.drawArc(arcRectF, 0, sweep, false, paint);
        //第二个圆弧
        canvas.drawArc(arcRectF, 90, sweep, false, paint);
        //第三个圆弧
        canvas.drawArc(arcRectF, 180, sweep, false, paint);
        //第四个圆弧
        canvas.drawArc(arcRectF, 270, sweep, false, paint);
        if (!isStart) {
            startAnim();
            isStart = true;
        }
    }
}
