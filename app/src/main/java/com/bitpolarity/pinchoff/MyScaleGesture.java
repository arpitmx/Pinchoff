package com.bitpolarity.pinchoff;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

class MyScaleGestures  implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale = false;
    ArrayList<Integer> zoomList;
    Context c;
    int res;

    public MyScaleGestures (Context c){
        this.c = c;
        gestureScale = new ScaleGestureDetector(c, this);
        zoomList = new ArrayList<>();
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
        //view.setScaleX(scaleFactor);
       // view.setScaleY(scaleFactor);
        return true;
    }

    void checkMax(ArrayList<Integer> zoomList){

        int c1= 0, c2= 0;
        for (int i : zoomList){
            if (i==1) c1++;
            else c2++;
        }
        if (c1>c2) {
            Log.d("TAG", "Zooming in");
            Toast.makeText(c, "Text picked!", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Text copied", Toast.LENGTH_SHORT).show();
        }else {
            Log.d("TAG", "Zooming out");
          //  Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show();


        }
    }



    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        zoomList.clear();

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) { inScale = false;
        checkMax(zoomList);
        Log.d("TAG", zoomList.toString());


    }
}