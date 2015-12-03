package scret.com.msx7.josn.effectdemo.ui.qqpop;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import scret.com.msx7.josn.effectdemo.L;
import scret.com.msx7.josn.effectdemo.effect.anim1.AnimInterpolator.Quart;

/**
 * Created by xiaowei on 2015/12/1.
 */
public class pop extends View {

    public pop(Context context) {
        super(context);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    public pop(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    public pop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    @TargetApi(23)
    public pop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                secondCenter = new PointF(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                secondCenter = new PointF(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                secondCenter = null;

                break;
        }
        requestLayout();
        postInvalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (secondCenter != null) {
            float width = getMeasuredWidth();
            float height = getMeasuredHeight();
            float len = (float) Math.hypot(secondCenter.x - center.x, secondCenter.y - center.y);
            float secondeRadius = getSecondRadius(secondCenter.x, secondCenter.y);
            if (len + secondeRadius <= width / 2) {
                return;
            }
            width = (Math.abs(secondCenter.x - center.x) + secondeRadius) * 2;
            height = (Math.abs(secondCenter.y - center.y) + secondeRadius) * 2;
            setMeasuredDimension((int) width, (int) height);

        }
    }

    PointF secondCenter;

    PointF center;
    float radius;
    Paint paint;
    float minRadius;
    float maxRadius;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (center == null) {
            center = new PointF((getRight() + getLeft()) / 2, (getTop() + getBottom()) / 2);
            radius = getResources().getDisplayMetrics().density * 10;
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            maxRadius = radius * 12;
            minRadius = getResources().getDisplayMetrics().density * 6;
        }
        paint.setColor(Color.RED);
        drawDefault(canvas);
        if (secondCenter != null) {
            float secondRadius = getSecondRadius(secondCenter.x, secondCenter.y);
            canvas.drawCircle(secondCenter.x, secondCenter.y, secondRadius, paint);
            //取中心园的一个参考点，计算2圆中心线与2圆相交的点  此处去圆正右边的点，为参考点。
            PointF cankao = new PointF(center.x + radius, center.y);

            float angle = (float) (Math.acos(getCosA(cankao, center, secondCenter)) * 180 / Math.PI);
            if (secondCenter.y < center.y) angle = 0 - angle;
            //中心圆的相交点
            PointF cp1 = getRotatePoint(angle, cankao, center);
            L.d("  -------------- "  );
            //中心园的2个点
            float a = 90 - (float) Math.asin(minRadius / radius);
            PointF p1 = getRotatePoint(a, cp1, center);
            PointF p2 = getRotatePoint(-a, cp1, center);
            L.d("  ---------aaaaaa  " +a );
            //第二个园的相交点 此处参考取左正方的点
            cankao = new PointF(secondCenter.x - secondRadius, secondCenter.y);

            angle = (float) (Math.acos(getCosA(cankao, secondCenter, center)) * 180 / Math.PI);
            if (secondCenter.y < center.y) angle = 0 - angle;
            PointF cp2 = getRotatePoint(angle, cankao, secondCenter);


            //第二个园的2个点
            a = 90 - (float) Math.asin(minRadius / secondRadius);
            PointF p4 = getRotatePoint(a, cp2, secondCenter);
            PointF p3 = getRotatePoint(-a, cp2, secondCenter);

            Path path = new Path();
            path.moveTo(p1.x, p1.y);
            if (Math.abs(a) > 60) {
                path.quadTo(cp1.x, cp1.y, p3.x, p3.y);
            } else
                path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, p3.x, p3.y);
            path.lineTo(p4.x, p4.y);
            if (Math.abs(a) > 60) {
                path.quadTo(cp1.x, cp1.y, p2.x, p2.y);
            } else
                path.cubicTo(cp2.x, cp2.y, cp1.x, cp1.y, p2.x, p2.y);
            path.lineTo(p1.x, p1.y);
            L.d("  ----------aaa2222 " +a );
            L.d(p1 + "   , " + p2);
            L.d(p3 + "   , " + p4);
            paint.setStrokeWidth(4f);
            canvas.drawPath(path, paint);
//            paint.setColor(Color.BLUE);
//            canvas.drawLine(p1.x, p1.y, p3.x, p3.y, paint);
//            paint.setColor(Color.GREEN);
//
//            canvas.drawLine(p2.x, p2.y, p4.x, p4.y, paint);

        }
    }

    void drawDefault(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    /**
     * 限定最大值与最小值
     * 最大值= radius
     * 最小值  4dp
     *
     * @param x
     * @param y
     * @return
     */
    float getSecondRadius(float x, float y) {
        float len = (float) Math.hypot(x - center.x, y - center.y);
        if (len - 2 * minRadius - radius < radius) return minRadius;
        float _raduis = minRadius + radius * Quart.easeInOut(len / maxRadius, 0, 1, 1);
        _raduis = Math.min(_raduis, radius);
        return _raduis;
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

    public PointF getRotatePoint(float angle, PointF pointF, PointF center) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, center.x, center.y);
        float[] dsts = new float[2];
        matrix.mapPoints(dsts, new float[]{pointF.x, pointF.y});
        return new PointF(dsts[0], dsts[1]);
    }
}
