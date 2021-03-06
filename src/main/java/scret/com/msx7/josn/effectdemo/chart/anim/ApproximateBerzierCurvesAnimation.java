
package scret.com.msx7.josn.effectdemo.chart.anim;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;
import android.view.animation.Transformation;

public class ApproximateBerzierCurvesAnimation extends BaseBerzierCurvesAnim {
    float start;
    float end;
    PointF[] points;
    PointF[][] _points ;
    Paint paint;
    Canvas canvas;
    SurfaceHolder holder;

    private ApproximateBerzierCurvesAnimation(float start, float end, PointF[] points,
                                              Paint paint) {
        super();
        this.start = start;
        this.end = end;
        this.points = points;
        this.paint = paint;

        if (start < 0 || end > 1.0f) {
            throw new IllegalArgumentException(
                    "the params start、end must between 0.0f and 1.0f");
        }
    }

    public ApproximateBerzierCurvesAnimation(float start, float end, PointF[] points,
                                             Paint paint, SurfaceHolder holder) {
        this(start, end, points, paint);
        this.holder = holder;
    }

    public ApproximateBerzierCurvesAnimation(float start, float end, PointF[] points,
                                             Paint paint, SurfaceHolder holder, long durationMillis) {
        this(start, end, points, paint);
        this.holder = holder;
        setDuration(durationMillis);
    }

    public ApproximateBerzierCurvesAnimation(float start, float end, PointF[] points,
                                             Paint paint, Canvas canvas) {
        this(start, end, points, paint);
        this.canvas = canvas;
    }

    public ApproximateBerzierCurvesAnimation(float start, float end, PointF[] points,
                                             Paint paint, Canvas canvas, long durationMillis) {
        this(start, end, points, paint);
        setDuration(durationMillis);
    }

    protected void transformation(float interpolatedTime, Transformation t) {
        float scale = (end - start) * interpolatedTime + start;
        _points = createCurves(points, scale);
        if (holder != null) {
            Canvas canvas = holder.lockCanvas();
            drawCurves(points, _points, canvas);
            if (interpolatedTime >= 1.0f) {
                onAnimEnd(canvas);
            }
            holder.unlockCanvasAndPost(canvas);
        } else {
            drawCurves(points, _points, canvas);
            if (interpolatedTime >= 1.0f) {
                onAnimEnd(canvas);
            }
        }

    }

    void drawCurves(PointF[] origin, PointF[][] points, Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        int count = points.length;
        Path path = new Path();
        path.moveTo(origin[0].x, origin[0].y);

        Paint _paint = new Paint(paint);
        _paint.setStrokeWidth(4f);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.BLUE);

        for (int i = 0; i < count; i++) {
            path.lineTo(points[i][0].x, points[i][0].y);
            path.cubicTo(points[i][1].x, points[i][1].y, points[i][2].x, points[i][2].y, points[i][3].x, points[i][3].y);

            canvas.drawPoint(points[i][0].x, points[i][0].y, _paint);
            canvas.drawPoint(points[i][3].x, points[i][3].y, _paint);
            canvas.drawPoint(points[i][1].x, points[i][1].y, _paint);
            canvas.drawPoint(points[i][2].x, points[i][2].y, _paint);

        }

//        for (int i = 0; i < origin.length; i++) {
//            canvas.drawCircle(origin[i].x, origin[i].y, _paint.getStrokeWidth()/2, _paint);
//        }

        canvas.drawPath(path, paint);
    }
}
