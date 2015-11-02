package scret.com.msx7.josn.effectdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import scret.com.msx7.josn.effectdemo.effect.AnimShapeRectCircle;
import scret.com.msx7.josn.effectdemo.effect.Shape2Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, AnimShapeRectCircle.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, Shape2Activity.class));
                break;
        }
    }
}
