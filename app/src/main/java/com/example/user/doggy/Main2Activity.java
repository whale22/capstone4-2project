package com.example.user.doggy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Button btn = (Button)findViewById(R.id.gotofunc1);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), function1.class);
                startActivity(intent);
            }
        });

        Button btn2 = (Button)findViewById(R.id.gotofunc2);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), function2.class);
                startActivity(intent);
            }
        });

        Button btn3 = (Button)findViewById(R.id.gotofunc3);
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), function3.class);
                startActivity(intent);
            }
        });
    }
}