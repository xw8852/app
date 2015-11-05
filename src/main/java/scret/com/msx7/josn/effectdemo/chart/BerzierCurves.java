package scret.com.msx7.josn.effectdemo.chart;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import scret.com.msx7.josn.effectdemo.chart.anim.ApproximateBerzierCurvesAnimation;
import scret.com.msx7.josn.effectdemo.chart.anim.BaseBerzierCurvesAnim;
import scret.com.msx7.josn.effectdemo.chart.anim.BaseBerzierCurvesAnim.ApplyAnimEnd;
import scret.com.msx7.josn.effectdemo.chart.anim.RiseBerzierCurvesAnimation;
import scret.com.msx7.josn.effectdemo.chart.anim.TrackBerzierCurvesAnimation;


public class BerzierCurves extends SurfaceView implements Callback {
    public static final int ANIM_APPTOXIMATE = 0x01;
    public static final int ANIM_RISEANIMATION = 0x02;
    public static final int ANIM_TRAKANIMATION = 0x03;

    private float scale = 1f;
    int animType;
    PointF[] origin;
    BerzierDataSetObserver mObserver;
    DataSetObservable observable = new DataSetObservable();
    boolean closed;
    SurfaceHolder _Holder = getHolder();
    boolean isCreated;

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        mObserver.onChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("   _____      minX " + event.getX() + "," + event.getY());
        return super.onTouchEvent(event);
    }

    public int getAnimType() {
        return animType;
    }

    public void setAnimType(int animType) {
        this.animType = animType;
        if (origin != null)
            observable.notifyChanged();
    }

    public BerzierCurves(Context context, AttributeSet attrs) {
        super(context, attrs);
        _Holder = getHolder();
        setZOrderOnTop(true);
        _Holder.addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    public BerzierCurves(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _Holder = getHolder();
        setZOrderOnTop(true);
        _Holder.addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    public BerzierCurves(Context context) {
        super(context);
        _Holder = getHolder();
        setZOrderOnTop(true);
        _Holder.addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        int _pHeight;
        int _pWidth;
        if (origin != null) {

        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = getDefaultSize(0, widthMeasureSpec);
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = getResources().getDisplayMetrics().widthPixels;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = getDefaultSize(0, widthMeasureSpec);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = getResources().getDisplayMetrics().heightPixels;
        }
        System.out.println("widthMode:" + widthMode);
        System.out.println("heightMode:" + heightMode);
        System.out.println("widthSize:" + widthSize);
        System.out.println("heightSize:" + heightSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean isClosed) {
        this.closed = isClosed;
    }

    public void setPoint(PointF... points) {
        this.origin = points;
        if (mObserver == null) {
            mObserver = new BerzierDataSetObserver();
            registerDataSetObserver(mObserver);
        }
        if (isCreated)
            observable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        observable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        observable.unregisterObserver(observer);

    }

    public PointF[] getPoint() {
        if (origin == null) return null;
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int top = getTop();
        int left = getLeft();
        int bottom = getBottom();
        /**
         * 从左下角开始 预留10%左右的距离来预留原点
         */
        float origin_x = 0.1f * width;
        float origin_y = 0.9f * height;
        float minX = origin[0].x, minY = origin[0].y, maxX = origin[0].x, maxY = origin[0].y;
        for (int i = 0; i < origin.length; i++) {
            minX = Math.min(origin[i].x, minX);
            minY = Math.min(origin[i].y, minY);
            maxX = Math.max(origin[i].x, maxX);
            maxY = Math.max(origin[i].y, maxY);
        }
        Rect rect = new Rect();
        getHitRect(rect);
        System.out.println("   _____      minX " + rect);
        System.out.println("   _____      minX " + minX);
        System.out.println("   _____       minY " + minY);
        System.out.println("   _____      maxX " + maxX);
        System.out.println("   _____      maxY " + maxY);

        float r_minX = origin_x + 0.1f * width;
        float r_minY = origin_y - 0.1f * height;
        float r_maxX = getRight() - 0.1f * width;
        float r_maxY = getTop() + 0.1f * height;
        float dx = (r_maxX - r_minX) / (maxX - minX);
        float dy = (r_minY - r_maxY) / (maxY - minY);
        System.out.println("   _____      r_minX " + r_minX);
        System.out.println("   _____       r_minY " + r_minY);
        System.out.println("   _____      r_maxX " + r_maxX);
        System.out.println("   _____      r_maxY " + r_maxY);
        System.out.println("   _____      dx " + dx);
        System.out.println("   _____      dy " + dy);
        PointF[] _arr = new PointF[origin.length];
        for (int i = 0; i < origin.length; i++) {
            PointF pf = origin[i];
            _arr[i] = new PointF(((pf.x - minX) * dx + r_minX), (r_minY - (pf.y - minY) * dy));
            System.out.println("   _____       " + pf.toString());
            System.out.println("   _____       " + _arr[i]);
        }
        System.out.println("_____" + getMeasuredWidth() + "," + getMeasuredHeight());
        return _arr;
    }

    private class BerzierDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            BaseBerzierCurvesAnim anim;
            switch (animType) {
                case ANIM_APPTOXIMATE:
                    anim = new ApproximateBerzierCurvesAnimation(0.0f, getScale(),
                            getPoint(), getLinePaint(), getHolder(), 1000);
                    break;
                case ANIM_RISEANIMATION:
                    anim = new RiseBerzierCurvesAnimation(
                            RiseBerzierCurvesAnimation.TYPE_Y, getPoint(),
                            getLinePaint(), getHolder(), getScale(), 1000, 1000);
                    break;
                case ANIM_TRAKANIMATION:
                    anim = new TrackBerzierCurvesAnimation(getPoint(), getLinePaint(),
                            getScale(), getHolder(), 3000);
                    break;
                default:
                    anim = new ApproximateBerzierCurvesAnimation(getScale(),
                            getScale(), getPoint(), getLinePaint(), getHolder(), 0l);
                    break;
            }
            anim.setAnimEnd(animEnd);
            startAnimation(anim);
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }

    }

    ApplyAnimEnd animEnd = new ApplyAnimEnd() {

        @Override
        public void animEnd(Canvas canvas) {
//            int count = origin.length;
//            PointF[] ps = getPoint();
//            for (int i = 0; i < count; i++) {
//                canvas.drawCircle(ps[i].x, ps[i].y, 12, getPointPaint());
//            }
        }
    };

    Paint getPointPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    Paint getLinePaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        Path circle = new Path();
        circle.addCircle(0, 0, 5, Path.Direction.CCW);
//        circle.addRect(0, 0, 8, 8, Path.Direction.CCW);
        paint.setPathEffect(new PathDashPathEffect(circle,2, 1, PathDashPathEffect.Style.ROTATE));
//        paint.setPathEffect(new CornerPathEffect(20f));
        return paint;
    }

    void traparentBackgroud(Canvas canvas) {
        Paint clearPaint = new Paint();
        clearPaint.setAntiAlias(true);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Rect rect = new Rect();
        getHitRect(rect);
        canvas.drawRect(rect, clearPaint);
    }

    float bezierNfuncX(float t, PointF[] arr) {
        if (arr.length == 2) {
            return (1 - t) * arr[0].x + t * arr[1].x;
        }
        PointF[] arr1 = new PointF[arr.length - 1];
        System.arraycopy(arr, 0, arr1, 0, arr.length - 1);
        PointF[] arr2 = new PointF[arr.length - 1];
        System.arraycopy(arr, 1, arr2, 0, arr.length - 1);
        return (1 - t) * bezierNfuncX(t, arr1) + t * bezierNfuncX(t, arr2);
    }

    float bezierNfuncY(float t, PointF[] arr) {
        if (arr.length == 2) {
            return (1 - t) * arr[0].y + t * arr[1].y;
        }
        PointF[] arr1 = new PointF[arr.length - 1];
        System.arraycopy(arr, 0, arr1, 0, arr.length - 1);
        PointF[] arr2 = new PointF[arr.length - 1];
        System.arraycopy(arr, 1, arr2, 0, arr.length - 1);
        return (1 - t) * bezierNfuncY(t, arr1) + t * bezierNfuncY(t, arr2);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCreated = true;

        if (origin != null) {
            observable.notifyChanged();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
