package scret.com.msx7.josn.effectdemo.effect.anim1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.Transformation;

import scret.com.msx7.josn.effectdemo.R;
import scret.com.msx7.josn.effectdemo.effect.anim1.AnimInterpolator.QuartInterpolator;

/**
 * Created by Josn on 2015/10/31.
 */
public class CircleRectShape extends View {
    public CircleRectShape(Context context) {
        super(context);
        init();
    }

    CircleAngleAnimation animation;

    public CircleRectShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRectShape);
        int duration = typedArray.getInt(R.styleable.CircleRectShape_CircleAnimDuration, 0);
        angle = typedArray.getInt(R.styleable.CircleRectShape_CircleStartAngle, 120);
        int endAngle = typedArray.getInt(R.styleable.CircleRectShape_CircleEndAngle, 0);
        init();
        animation = new CircleAngleAnimation(this, endAngle);
        animation.setDuration(duration);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(animation);
            }
        }, 300);
    }

    public CircleRectShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRectShape);
        int duration = typedArray.getInt(R.styleable.CircleRectShape_CircleAnimDuration, 0);
        angle = typedArray.getInt(R.styleable.CircleRectShape_CircleStartAngle, 120);
        int endAngle = typedArray.getInt(R.styleable.CircleRectShape_CircleEndAngle, 0);
        init();
        animation = new CircleAngleAnimation(this, endAngle);
        animation.setDuration(duration);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(animation);
            }
        }, 300);
    }


    private static final int START_ANGLE_POINT = 180;

    private Paint paint;
    private RectF rect;

    void init() {
        final int strokeWidth = 40;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(Color.RED);

        //size 200x200 example
        rect = new RectF(strokeWidth, strokeWidth, 200 + strokeWidth, 200 + strokeWidth);


    }

    private float angle = 120;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public static class CircleAngleAnimation extends Animation {

        private CircleRectShape circle;

        private float oldAngle;
        private float newAngle;

        public CircleAngleAnimation(CircleRectShape circle, int newAngle) {
            this.oldAngle = circle.getAngle();
            this.newAngle = newAngle;
            this.circle = circle;
            setInterpolator(new QuartInterpolator());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation transformation) {
            float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);

            circle.setAngle(angle);
            circle.requestLayout();
        }
    }
}
