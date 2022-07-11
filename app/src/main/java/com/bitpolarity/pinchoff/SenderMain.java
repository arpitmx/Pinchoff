package com.bitpolarity.pinchoff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;
import android.widget.Toast;

public class SenderMain extends AppCompatActivity {

    TextView txt1,txt2;
    ScaleGestureDetector scaleGestureDetector;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        MyScaleGestures myScaleGestures = new MyScaleGestures(this);
        txt1.setOnTouchListener(myScaleGestures);
        txt2.setOnTouchListener(myScaleGestures);


    }





}