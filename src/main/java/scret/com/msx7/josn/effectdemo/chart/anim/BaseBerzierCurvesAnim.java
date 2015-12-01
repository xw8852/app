package scret.com.msx7.josn.effectdemo.chart.anim;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public abstract class BaseBerzierCurvesAnim extends Animation {
    AnimationListener listener;
    ILinePaint paint;


    /**
     * 待验证的公式
     *
     * @param origin
     * @param scale
     * @return
     */
    @SuppressLint("FloatMath")
    public PointF[][] createCurves(PointF[] origin, float scale) {
        int originCount = origin.length;
        PointF[][] points = new PointF[originCount - 1][4];
        for (int i = 0; i < originCount - 2; i++) {

            PointF pre = origin[i];
            PointF cur = origin[i + 1];
            PointF next = origin[i + 2];

            PointF[] _o1 = points[i];
            _o1[0] = pre;
            if (_o1[1] == null) _o1[1] = pre;
            if (_o1[2] == null) _o1[2] = cur;
            _o1[3] = cur;

            PointF[] _o2 = points[i + 1];
            _o2[0] = cur;
            if (_o2[1] == null) _o2[1] = cur;
            if (_o2[2] == null) _o2[2] = next;
            _o2[3] = next;

            float _K1 = (pre.y - cur.y) / (pre.x - cur.x);
            float _K2 = (next.y - cur.y) / (next.x - cur.x);
            if (_K1 == _K2) continue;
            /**
             *  p1p2与p2p3的夹角
             * <p>
             * cosb= ( (p1p2)^2  +(p2p3)^2-(p1p3)^2  )/( 2* p1p2*p2p3 )
             * <p>
             * 那么 左右2个控制点的连线的角度   (180-a)/2
             * <p>
             * cos（pi-a) = - cos a
             * <p>
             * 去正角 小于 90°  cos a/2 =  +/-  开根号(（ 1+cos a)/2 )
             */
            int len1 = (int) FloatMath.sqrt((pre.x - cur.x) * (pre.x - cur.x) + (pre.y - cur.y) * (pre.y - cur.y));
            int len2 = (int) FloatMath.sqrt((next.x - cur.x) * (next.x - cur.x) + (next.y - cur.y) * (next.y - cur.y));
            int len3 = (int) FloatMath.sqrt((next.x - pre.x) * (next.x - pre.x) + (next.y - pre.y) * (next.y - pre.y));
            //余弦定理
            float _cosb = (len1 * len1 + len2 * len2 - len3 * len3) / (2 * len1 * len2);
            //cos（pi-a) = - cos a
            float _cos2a = 0 - _cosb;
            //去正角 小于 90°  cos a/2 =  +/-  开根号(（ 1+cos a)/2 ) 即取正值
            float _cosa = FloatMath.sqrt((1 + _cos2a) / 2);
            float lenC = (scale * len1 / 4) * _cosa;

            // lenc = 控制點 到 point cur的距離   斜率 k1k2=-1;
            float _k2 = ((pre.y + next.y) / 2 - cur.y) / ((pre.x + next.x) / 2 - cur.x);
            // 2个 控制点的斜率
            float _k1 = -1 / Math.abs(_k2);



            float _c1x = cur.x + FloatMath.sqrt(lenC * lenC / (1 + _k1 * _k1));
            float _c1y = _k1 * (_c1x - cur.x) + cur.y;

            float _c2x = cur.x - FloatMath.sqrt(lenC * lenC / (1 + _k1 * _k1));
            float _c2y = _k1 * (_c2x - cur.x) + cur.y;
//
//            System.out.println(" K1 " + _k1 + " , " + ((_c1y - _c2y) / (_c1x - _c2x)));
//            System.out.println("　K2 " + _k2);

//            System.out.println(i + "____" + _c1x + "," + _c1y);
//            System.out.println(i + "____" + _c2x + "," + _c2y);
//
//            _c1y = cur.y + FloatMath.sqrt(power(lenC) * power(_k1) / (1 + power(_k1)));
//            _c1x = (_c1y - cur.y) / _k1 + cur.x;
//
//            _c2y = cur.y - FloatMath.sqrt(power(lenC) * power(_k1) / (1 + power(_k1)));
//            _c2x = (_c2y - cur.y) / _k1 + cur.x;
//
//            System.out.println(i + "____" + _c1x + "," + _c1y);
//            System.out.println(i + "____" + _c2x + "," + _c2y);
            if (_c1x < cur.x && _c1y < cur.y) {
                _o1[2] = new PointF(_c1x, _c1y);
                _o2[1] = new PointF(_c2x, _c2y);
            } else {
                _o2[1] = new PointF(_c1x, _c1y);
                _o1[2] = new PointF(_c2x, _c2y);
            }
//            L.d(" cosa " + _cosa);
//            L.d(" len1 " + len1);
//            L.d(" lenC " + lenC);
//            L.d(" Point " + cur);
//            L.d(" Point " + new PointF(_c1x, _c1y));
//            L.d(" Point " + new PointF(_c2x, _c2y));
//            L.d(" __________________ ");
//
//            System.out.println(i + "____" + _o1[0].toString());
//            System.out.println(i + "____" + _o1[1].toString());
//            System.out.println(i + "____" + _o1[2].toString());
//            System.out.println(i + "____" + _o1[3].toString());
//
//            System.out.println();
//            System.out.println();
//            int _nexti = (i + 1) % originCount;
//            int _prev = (i + 2) % originCount;
//            int _backi = (i + originCount - 1) % originCount;
//            PointF p1 = origin[i];
//            PointF p2 = origin[_nexti];
//            PointF p3 = origin[_prev];
//            PointF p0 = origin[_backi];
//
//            if (i + 2 >= originCount - 1) {
//                p3 = new PointF((p2.x - p1.x) * scale + p2.x, (p2.y - p1.y)
//                        * scale + p2.y);
//
//            }
//            if (i - 1 < 0) {
//                p0 = new PointF((p1.x - p2.x) * scale + p1.x, (p1.y - p2.y)
//                        * scale + p1.y);
//            }
//
//            float len1 = FloatMath.sqrt((p0.x - p1.x) * (p0.x - p1.x)
//                    + (p0.y - p1.y) * (p0.y - p1.y));
//            float len2 = FloatMath.sqrt((p1.x - p2.x) * (p1.x - p2.x)
//                    + (p1.y - p2.y) * (p1.y - p2.y));
//            float len3 = FloatMath.sqrt((p3.x - p2.x) * (p3.x - p2.x)
//                    + (p3.y - p2.y) * (p3.y - p2.y));
//            float k1 = len1 / (len1 + len2);
//            float k2 = len2 / (len2 + len3);
//            float xm1 = k1 * ((p1.x + p2.x) / 2.0f - (p1.x + p0.x) / 2.0f)
//                    + (p1.x + p0.x) / 2.0f;
//            float ym1 = k1 * ((p1.y + p2.y) / 2.0f - (p1.y + p0.y) / 2.0f)
//                    + (p1.y + p0.y) / 2.0f;
//            float xm2 = k2 * ((p2.x + p3.x) / 2.0f - (p2.x + p1.x) / 2.0f)
//                    + (p2.x + p1.x) / 2.0f;
//            float ym2 = k2 * ((p2.y + p3.y) / 2.0f - (p2.y + p1.y) / 2.0f)
//                    + (p2.y + p1.y) / 2.0f;
//            float _ctrl_x1 = xm1 + p1.x - xm1 + scale
//                    * ((p1.x + p2.x) / 2.0f - xm1);
//            float _ctrl_y1 = ym1 + p1.y - ym1 + scale
//                    * ((p1.y + p2.y) / 2.0f - ym1);
//
//            float _ctrl_x2 = xm2 + p2.x - xm2 + scale
//                    * ((p2.x + p1.x) / 2.0f - xm2);
//            float _ctrl_y2 = ym2 + p2.y - ym2 + scale
//                    * ((p2.y + p1.y) / 2.0f - ym2);
//            PointF[] arr = new PointF[4];
//            arr[0] = p1;
//            arr[1] = new PointF(_ctrl_x1, _ctrl_y1);
//            arr[2] = new PointF(_ctrl_x2, _ctrl_y2);
//            arr[3] = p2;
//            points[i] = arr;
        }
        return points;
    }

    public float add(float x, float y) {
        return x + y;
    }

    public float power(float x) {
        return x * x;
    }



    public ILinePaint getPaint() {
        return paint;
    }

    public void setPaint(ILinePaint paint) {
        this.paint = paint;
    }

    @Override
    public void setAnimationListener(AnimationListener listener) {
        super.setAnimationListener(listener);
        this.listener = listener;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        transformation(interpolatedTime, t);
    }

    public void onAnimEnd(Canvas canvas) {
        if (animEnd != null) {
            animEnd.animEnd(canvas);
        }
    }

    ApplyAnimEnd animEnd;

    public ApplyAnimEnd getAnimEnd() {
        return animEnd;
    }

    public void setAnimEnd(ApplyAnimEnd animEnd) {
        this.animEnd = animEnd;
    }

    protected abstract void transformation(float interpolatedTime, Transformation t);

    public static interface ApplyAnimEnd {
        public void animEnd(Canvas canvas);
    }

    public static interface ILinePaint {
        public Paint getLinePaint();
    }
}
