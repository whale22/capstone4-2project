package com.example.user.doggy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


                //3초 후 인트로 액티비티 제거
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Intent intent = new Intent(intro.this, MainActivity.class);
                        startActivity(intent);

                        finish();
            }
        }, 3000);
    }
}
