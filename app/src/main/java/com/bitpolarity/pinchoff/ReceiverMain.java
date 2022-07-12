package com.bitpolarity.pinchoff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

public class ReceiverMain extends AppCompatActivity {

    EditText editText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_main);
        editText = findViewById(R.id.recieveView);
     //   editText.setClickable(false);
        editText.setOnTouchListener(new MyScaleGesturesReciever(this));


    }
}