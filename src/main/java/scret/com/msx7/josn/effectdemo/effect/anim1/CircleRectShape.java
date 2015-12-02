package scret.com.msx7.josn.effectdemo.effect.anim1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import scret.com.msx7.josn.effectdemo.R;

/**
 * Created by Josn on 2015/10/31.
 */
public class CircleRectShape extends View {

    public CircleRectShape(Context context) {
        super(context);
        init();
    }

    private float startAngle = 120;
    private float AngleLength = 120;
    private float _angle = 0;

    //    CircleAngleAnimation animation;
    Drawable drawable;
    int drawableHeight;

    public CircleRectShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRectShape);
        int duration = typedArray.getInt(R.styleable.CircleRectShape_CircleAnimDuration, 0);
        startAngle = typedArray.getInt(R.styleable.CircleRectShape_CircleStartAngle, 120);
        AngleLength = typedArray.getInt(R.styleable.CircleRectShape_AngleLength, 0);

        drawable = typedArray.getDrawable(R.styleable.CircleRectShape_CircleDrawable);
        drawableHeight = typedArray.getDimensionPixelSize(R.styleable.CircleRectShape_DrawableHeight, 40);
        ArcWidth = typedArray.getDimensionPixelSize(R.styleable.CircleRectShape_ArcWidth, ArcWidth);
        init();


    }

    public CircleRectShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRectShape);
        int duration = typedArray.getInt(R.styleable.CircleRectShape_CircleAnimDuration, 0);
        startAngle = typedArray.getInt(R.styleable.CircleRectShape_CircleStartAngle, 150);
        AngleLength = typedArray.getInt(R.styleable.CircleRectShape_AngleLength, 240);
        drawable = typedArray.getDrawable(R.styleable.CircleRectShape_CircleDrawable);
        drawableHeight = typedArray.getDimensionPixelSize(R.styleable.CircleRectShape_DrawableHeight, 40);
        init();

    }

    /**
     * 圆弧宽度
     */
    int ArcWidth = 80;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(drawableHeight + ArcWidth + getPaddingLeft() + getPaddingRight(),
                drawableHeight + ArcWidth + getPaddingTop() + getPaddingBottom());

    }

    float lastY;
    float lastX;
    PointF dst;
    PointF center;
    PointF dividerPoint;
    float dividerAngle;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (dst == null) {
                    /**
                     * 分割点,求最上方中点位置,
                     * 默认起始点:最右边中点位置
                     * 利用matrix旋转,求出对应旋转startAngle之后的起始点
                     */
                    PointF _point = new PointF(getRight() - getLeft(), (getBottom() - getTop()) / 2);
                    Matrix matrix = new Matrix();
                    center = new PointF((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2);
                    matrix.postRotate(startAngle, center.x, center.y);
                    float[] dsts = new float[2];
                    matrix.mapPoints(dsts, new float[]{_point.x, _point.y});
                    dst = new PointF(dsts[0], dsts[1]);
                    dividerPoint = new PointF((getRight() - getLeft()) / 2, 0);
                    dividerAngle = (float) (Math.acos(getCosA(dst, center, dividerPoint)) * 180 / Math.PI);
                }
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                /**
                 * 在分割点，左边的，直接求值，
                 * 在分割点右边的分2部分求值
                 */
                PointF point = new PointF(event.getX(), event.getY());
                if (point.y > dst.y && point.x < dividerPoint.x) {
                    _angle = 0;
                } else if (point.x > dividerPoint.x) {
                    float cosa = getCosA(dividerPoint, center, point);
                    _angle = dividerAngle + (float) (Math.acos(cosa) * 180 / Math.PI);
                } else {
                    float cosa = getCosA(dst, center, point);
                    _angle = (float) (Math.acos(cosa) * 180 / Math.PI);
                }


                invalidate();

                break;
        }

        return true;
    }

    /**
     * p1            p3
     * <p/>
     * a
     * <p/>
     * p2
     * 求p1p2与p2p3之间余弦夹角的值
     */
    public float getCosA(PointF p1, PointF p2, PointF p3) {
        float lena = getLenth(p1, p2);
        float lenb = getLenth(p2, p3);
        float lenc = getLenth(p1, p3);
        return (lena * lena + lenb * lenb - lenc * lenc) / (2 * lena * lenb);
    }

    public float getLenth(PointF p1, PointF p2) {
        return (float) Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }


    private Paint paint;
    private RectF rect;
    float dlen;

    void init() {
        dlen = getResources().getDisplayMetrics().density;
        final int strokeWidth = 20;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(Color.RED);

        Path circle = new Path();
//        circle.addRect(0, 0, 8, 20, Path.Direction.CCW);
        circle.addRoundRect(new RectF(0f, 0f, strokeWidth / 4, strokeWidth), strokeWidth / 4, strokeWidth / 4, Path.Direction.CW);
//        circle.addCircle(0,0,8, Path.Direction.CCW);
//        circle.addRect(0, 0, 8, 8, Path.Direction.CCW);
        paint.setPathEffect(new PathDashPathEffect(circle, strokeWidth, strokeWidth, PathDashPathEffect.Style.ROTATE));
        //size 200x200 example

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect = new RectF(
                getPaddingLeft() + ArcWidth / 4,
                getPaddingTop() + ArcWidth / 4,
                getMeasuredWidth() - getPaddingRight() - ArcWidth / 4,
                getMeasuredHeight() - getPaddingBottom() - ArcWidth / 4);

        Paint _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(20);
        _paint.setColor(Color.BLUE);
        if (dividerPoint != null)
            canvas.drawPoint(dividerPoint.x + getLeft(), dividerPoint.y + getTop(), _paint);
        if (center != null)
            canvas.drawPoint(center.x + getLeft(), center.y + getTop(), _paint);

        drawGreyArc(canvas);
        if (_angle <= 0) _angle = 0;
        _angle = Math.min(_angle, AngleLength);
        drawLightArc(canvas, _angle);
        drawDrawable(canvas, _angle);
    }

    void drawDrawable(Canvas canvas, float angle) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawableHeight, drawableHeight);
            canvas.rotate(startAngle + 90 + angle, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            canvas.translate(ArcWidth / 2 + getPaddingLeft(), ArcWidth / 2 + getPaddingTop());
            drawable.draw(canvas);
        }
    }

    /**
     * 画默认灰色刻度
     *
     * @param canvas
     */
    void drawGreyArc(Canvas canvas) {
        paint.setColor(Color.GRAY);
        canvas.drawArc(rect, startAngle, AngleLength, false, paint);
    }

    /**
     * 画旋转刻度
     *
     * @return
     */
    void drawLightArc(Canvas canvas, float angle) {
        if (_angle <= 0) return;
        paint.setColor(Color.RED);
        canvas.drawArc(rect, startAngle, angle, false, paint);
    }


}
