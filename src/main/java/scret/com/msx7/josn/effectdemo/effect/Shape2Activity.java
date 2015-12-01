package scret.com.msx7.josn.effectdemo.effect;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import scret.com.msx7.josn.effectdemo.R;
import scret.com.msx7.josn.effectdemo.effect.anim2.AnimShape;

/**
 * Created by Josn on 2015/10/31.
 */
public class Shape2Activity extends Activity {
    AnimShape animShape;
    EditText lt;
    EditText lb;
    EditText rb;
    EditText rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_2);
        animShape = (AnimShape) findViewById(R.id.shape);
        lt = (EditText) findViewById(R.id.lt);
        lb = (EditText) findViewById(R.id.lb);
        rb = (EditText) findViewById(R.id.rb);
        rt = (EditText) findViewById(R.id.rt);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                s1 = s1.replaceAll("[^0-9]{1,}", "");



            }
        };
        lt.addTextChangedListener(textWatcher);
        rb.addTextChangedListener(textWatcher);
        lb.addTextChangedListener(textWatcher);
        rb.addTextChangedListener(textWatcher);
    }

    public void onClick(View v) {
        float _lt = Float.parseFloat(lt.getText().toString().trim());
        float _lb = Float.parseFloat(lb.getText().toString().trim());
        float _rt = Float.parseFloat(rt.getText().toString().trim());
        float _rb = Float.parseFloat(rb.getText().toString().trim());
        _lt = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _lt, getResources().getDisplayMetrics());
        _lb = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _lb, getResources().getDisplayMetrics());
        _rt = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _rt, getResources().getDisplayMetrics());
        _rb = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _rb, getResources().getDisplayMetrics());
        float[] radius = new float[]{_lt, _lt, _rt, _rt, _lb, _lb, _rb, _rb};
        animShape.setRadius(radius);
    }

}
