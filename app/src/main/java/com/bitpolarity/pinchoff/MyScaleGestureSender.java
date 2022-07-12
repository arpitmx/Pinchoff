package com.bitpolarity.pinchoff;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class MyScaleGestures  implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale = false;
    ArrayList<Integer> zoomList;
    Context c;
    int res;
    FirebaseFirestore db ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    Map<Integer,Boolean> fadeMap = new HashMap<>();
    TextView t1, t2;
    Post post;


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

    public MyScaleGestures (Context c,TextView t1, TextView t2){
        this.c = c;
        gestureScale = new ScaleGestureDetector(c, this);
        db =  FirebaseFirestore.getInstance();
        myRef = database.getReference("message");
        zoomList = new ArrayList<>();
        fadeMap.put(R.id.txt1,false);
        fadeMap.put(R.id.txt2, false);
        this.t1 = t1;
        this.t2 = t2;
        post = new Post();
      //  getUpdatesIfPasted();

    }

    void getUpdatesIfPasted(){
        String ts1= t1.getText().toString();
        String ts2 = t2.getText().toString();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Post post = dataSnapshot.getValue(Post.class);
               if (post.Text.equals(ts1) && post.isPasted ==1){
                    setFade(t1.getId());
               } else{
                    setFade(t2.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
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

    void setFade(int id){
        int txt1 = R.id.txt1;
        int txt2 = R.id.txt2;


        if (id==txt1 && Boolean.FALSE.equals(fadeMap.get(txt2))) {
            ((TextView)view).setAlpha(0.5f);
            fadeMap.put(id,true);
        }
        else if (id== txt2 && Boolean.FALSE.equals(fadeMap.get(txt1))){
            ((TextView)view).setAlpha(0.5f);
            fadeMap.put(id,true);
        }else if (id== txt1 && Boolean.TRUE.equals(fadeMap.get(txt2))){
            ((TextView)view).setAlpha(0.5f);
           t2.setAlpha(1f);
           fadeMap.put(id,true);
           fadeMap.put(t2.getId(),false);
        }else {
            ((TextView)view).setAlpha(0.5f);
            t1.setAlpha(1f);
            fadeMap.put(id,true);
            fadeMap.put(t1.getId(),false);
        }
    }

    void addData(String text){

        post.setText(text);
        post.setIsPasted(0);
        myRef.setValue(post);
       // Toast.makeText(c, "Text picked!", Toast.LENGTH_SHORT).show();
       setFade(view.getId());

    }

    void checkMax(ArrayList<Integer> zoomList){

        int c1= 0, c2= 0;
        for (int i : zoomList){
            if (i==1) c1++;
            else c2++;
        }
        if (c1>c2) {
            Log.d("TAG", "Zooming in");
            String text = (((TextView)view).getText().toString());
            vibrate();
            addData(text);

            Log.d("TAG", "checkMax: "+text);
            //Toast.makeText(getApplicationContext(), "Text copied", Toast.LENGTH_SHORT).show();
        }else {
            Log.d("TAG", "Zooming out");
          //  Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show()
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