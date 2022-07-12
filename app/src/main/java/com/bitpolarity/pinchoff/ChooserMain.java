package com.bitpolarity.pinchoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChooserMain extends AppCompatActivity {

    Button sender;
    Button r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        sender = findViewById(R.id.button);
        r = (Button) findViewById(R.id.button2);
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);

        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooserMain.this, SenderMain.class);
                i.putExtra("t1",et1.getText().toString());
                i.putExtra("t2",et2.getText().toString());
                startActivity(i);
            }
        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooserMain.this, ReceiverMain.class));
            }
        });
    }
}