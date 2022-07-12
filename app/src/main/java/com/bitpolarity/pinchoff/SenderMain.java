package com.bitpolarity.pinchoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SenderMain extends AppCompatActivity {

    TextView txt1,txt2;
    FirebaseFirestore db ;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        MyScaleGestures myScaleGestures = new MyScaleGestures(this,txt1,txt2);
        Bundle extras = getIntent().getExtras();

        String q1 = "The journey of a thousand miles begins with one step.";
        String q2 = "Life is what happens when you’re busy making other plans";


        if (extras!=null) {
            String et1= extras.getString("t1",q1);
            String et2 = extras.getString("t2",q2 );
           // Toast.makeText(this, et1+ "\n " + et2, Toast.LENGTH_SHORT).show();


            txt1.setText(et1);
            txt2.setText(et2);


            if (et1.equals("")) {
                txt1.setText(q1);
            }
            if (et2.equals("")) {
                txt2.setText(q2);
            }



        }
       txt1.setOnTouchListener(myScaleGestures);
        txt2.setOnTouchListener(myScaleGestures);



    }




}