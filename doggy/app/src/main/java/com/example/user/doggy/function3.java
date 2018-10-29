package com.example.user.doggy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class function3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function3);

        Button btn = (Button)findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
    }
});
    }
}
