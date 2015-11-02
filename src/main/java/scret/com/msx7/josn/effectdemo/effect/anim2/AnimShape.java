package scret.com.msx7.josn.effectdemo.effect.anim2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Josn on 2015/10/31.
 */
public class AnimShape extends FrameLayout {

    public AnimShape(Context context) {
        super(context);
    }

    public AnimShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint paintRect;
    Paint paintCircle;

    void init() {
        paintRect = new Paint();
        paintRect.setColor(Color.RED);
        paintRect.setStyle(Paint.Style.FILL);
//        paintRect.setStrokeWidth(4f);

        paintCircle = new Paint();
        paintCircle.setColor(Color.RED);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(4f);
    }

    int raduis,centerX,centerY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         raduis = getWidth() / 2;
         centerX = (getLeft() + getRight()) / 2;
         centerY = (getTop() + getBottom()) / 2;
        System.out.println(centerX + "-------    "+raduis);
        System.out.println(centerY + "AAAAAAAAAAAAAAA");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(centerX - raduis, centerY - raduis, centerX + raduis, centerY + raduis);
        canvas.drawRect(rect, paintRect);
    }
}
