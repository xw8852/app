package scret.com.msx7.josn.effectdemo.effect.anim2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.Arrays;

import scret.com.msx7.josn.effectdemo.R;

/**
 * Created by Josn on 2015/10/31.
 */
public class AnimShape extends LinearLayout {


    public AnimShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context.obtainStyledAttributes(attrs, R.styleable.AnimShape));
    }

    public AnimShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context.obtainStyledAttributes(attrs, R.styleable.AnimShape, defStyleAttr, 0));
    }

    Paint paintRect;
    Paint paintCircle;

    float radius;
    float radius_LT;
    float radius_LB;
    float radius_RT;
    float radius_RB;
    int color;
    int storkColor;
    float strokeWidth;
    float[] outerRadii;
    GradientDrawable drawable;

    void init(TypedArray array) {
        radius = array.getDimension(R.styleable.AnimShape_radius, 0);
        radius_LT = array.getDimension(R.styleable.AnimShape_radius_LT, 0);
        radius_LB = array.getDimension(R.styleable.AnimShape_radius_LB, 0);
        radius_RT = array.getDimension(R.styleable.AnimShape_radius_RT, 0);
        radius_RB = array.getDimension(R.styleable.AnimShape_radius_RB, 0);
        color = array.getColor(R.styleable.AnimShape_solide, Color.BLACK);
        storkColor = array.getColor(R.styleable.AnimShape_strokeColor, 0);
        strokeWidth = array.getDimension(R.styleable.AnimShape_strokeWidth, 0);
        outerRadii = new float[]{radius_LT, radius_LT, radius_RT, radius_RT, radius_LB, radius_LB, radius_RB, radius_RB};

        drawable = new GradientDrawable();
        if (radius <= 0 &&
                (radius_LB != radius
                        || radius_LT != radius
                        || radius_RB != radius
                        || radius_RT != radius)) {
            drawable.setCornerRadii(outerRadii);
        } else {
            drawable.setCornerRadius(radius);
            Arrays.fill(outerRadii, radius);
        }
        drawable.setColor(color);
        drawable.setStroke((int) strokeWidth, storkColor);
    }


    public void setRadius(float[] radius) {
        drawable.setCornerRadii(radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * {@inheritDoc}
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildCount() > 0 && drawable != null) {
            Rect rect = new Rect();
            getChildAt(0).getHitRect(rect);
            drawable.setBounds(0, 0, rect.width(), rect.height());
            canvas.translate(rect.left, rect.top);
            drawable.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }


}
