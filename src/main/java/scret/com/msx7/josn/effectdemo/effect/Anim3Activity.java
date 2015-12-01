package scret.com.msx7.josn.effectdemo.effect;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;

import scret.com.msx7.josn.effectdemo.R;
import scret.com.msx7.josn.effectdemo.chart.BerzierCurves;

/**
 * Created by xiaowei on 2015/11/3.
 */
public class Anim3Activity extends Activity {
    BerzierCurves curves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_3);
        curves = (BerzierCurves) findViewById(R.id.BerzierCurves);
        curves.setPoint(new PointF[]{
                new PointF(10, 10),
                new PointF(20, 20),
                new PointF(30, 30),
                new PointF(45, 70),
                new PointF(62, 63),
                new PointF(72, 90)});
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                curves.setAnimType(BerzierCurves.ANIM_APPTOXIMATE);
                break;
            case R.id.btn2:
                curves.setAnimType(BerzierCurves.ANIM_RISEANIMATION);
                break;
            case R.id.btn3:
                curves.setAnimType(BerzierCurves.ANIM_TRAKANIMATION);
                break;
        }
    }
}
