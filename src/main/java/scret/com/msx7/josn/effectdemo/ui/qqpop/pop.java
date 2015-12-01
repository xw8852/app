package scret.com.msx7.josn.effectdemo.ui.qqpop;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by xiaowei on 2015/12/1.
 */
public class pop extends TextView {

    public pop(Context context) {
        super(context);
    }

    public pop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public pop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(23)
    public pop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
