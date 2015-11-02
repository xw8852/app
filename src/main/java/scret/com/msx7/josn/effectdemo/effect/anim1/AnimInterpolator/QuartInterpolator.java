package scret.com.msx7.josn.effectdemo.effect.anim1.AnimInterpolator;

import android.view.animation.Interpolator;

/**
 * Created by Josn on 2015/10/31.
 */
public class QuartInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return Quart.easeInOut(input, 0, 1, 1);
    }
}
