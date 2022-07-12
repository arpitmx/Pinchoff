package com.bitpolarity.pinchoff;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class MyScaleGesturesReciever implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale = false;
    ArrayList<Integer> zoomList;
    FirebaseFirestore db ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    Context c;
    Map<Integer,Boolean> fadeMap = new HashMap<>();



    public MyScaleGesturesReciever(Context c) {
        this.c = c;
        gestureScale = new ScaleGestureDetector(c, this);
        db = FirebaseFirestore.getInstance();
        zoomList = new ArrayList<>();
        myRef = database.getReference("message");
    }



    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        gestureScale.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        if (scaleFactor <1){
            scaleFactor = 1;
            //Log.d("TAG", "onScale: Zooming in");
            zoomList.add(1);
        } else {
           // Log.d("TAG", "onScale: Zooming out");
            zoomList.add(2);
        }
        scaleFactor = ((float)((int)(scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //

        return true;
    }



    void upDatePasted(){
        myRef.child("isPasted").setValue(1);
       // Toast.makeText(c, "Text picked!", Toast.LENGTH_SHORT).show();
    }

    void fetchText(){
        myRef.child("text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                ((EditText) view).setText(value);
                setAlphaAnimation(view);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setAlphaAnimation(View v) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(700);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        v.startAnimation(alphaAnimation);
    }


    void checkMax(ArrayList<Integer> zoomList){

        int c1= 0, c2= 0;
        for (int i : zoomList){
            if (i==1) c1++;
            else c2++;
        }
        if(c2>c1) {
            fetchText();
            vibrate();
           view.setClickable(true);
           view.setEnabled(true);
            upDatePasted();
            Log.d("TAG", "Zooming out");


        }
    }

    void vibrate(){
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(200);
        }
    }


    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        zoomList.clear();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        inScale = false;
        checkMax(zoomList);
        Log.d("TAG", zoomList.toString());


    }
}