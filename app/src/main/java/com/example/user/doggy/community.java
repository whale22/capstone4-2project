package com.example.user.doggy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class community extends AppCompatActivity {
    static final String[] LIST_MENU = {"대형견 모임", "서울시 동작구 흑석로 산책 모임", "반려견 수제 간식 나눔 모임"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView cl = (ListView) findViewById(R.id.commuList);
        cl.setAdapter(adapter) ;
        cl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;
                commuContent.titleCon=strText;
                startActivity(new Intent(getApplicationContext(),commuContent.class));
                Toast.makeText(community.this, strText, Toast.LENGTH_SHORT).show();
                // TODO : use strText
            }
        }) ;


    }
}
