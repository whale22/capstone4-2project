package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class community extends AppCompatActivity {
    String[] LIST_MENU;
    static String bu;
    static int flag = 0;
    EditText title;
    EditText content;
    connectInfo ci = new connectInfo();
    BackgroundTask task;
    static int listsize=0;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        ListView cl = (ListView) findViewById(R.id.commuList);
        task = new community.BackgroundTask();
        task.execute(); // 서버와 자료 주고받기

        Button btn = (Button)findViewById(R.id.addbutton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), commuWrite.class);
                startActivity(intent);
            }
        });

    }
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            HttpPostData();
            return null;
        }

        protected void onPostExecute(Integer a) {
            //tv.setText(result);
            ListView cl = (ListView) findViewById(R.id.commuList);
            adapter = new ArrayAdapter(community.this, android.R.layout.simple_list_item_1, LIST_MENU);
            cl.setAdapter(adapter);
            cl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get TextView's Text.
                    String strText = (String) parent.getItemAtPosition(position);
                    commuContent.titleCon = strText;
                    startActivity(new Intent(getApplicationContext(), commuContent.class));
                    Toast.makeText(community.this, strText, Toast.LENGTH_SHORT).show();
                    // TODO : use strText
                }
            });
        }

    }


    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public void HttpPostData() {
        StringBuilder builder = new StringBuilder();
        try {

            Log.d("RESPONSE", "http://" + ci.getIP() + "/boardlist.php");
            String response = null;
            title = (EditText) findViewById(R.id.titleWrite);
            content = (EditText) findViewById(R.id.contentWrite);
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://" + ci.getIP() + "/boardlist.php");       // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            //http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            //--------------------------
            //   서버로 값 전송
            //--------------------------

            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            String str;
            str = reader.readLine();
            Log.d("RESPONSE", "what is bu : " + str);
            String[] boardInfo;
            if(str.contains("#")){
                boardInfo = str.split("#");
                listsize=boardInfo.length;
                LIST_MENU = new String[listsize];
                Log.d("RESPONSE", "what is str : " + boardInfo.length);
                for (int i = 0; i < boardInfo.length; i++)
                    LIST_MENU[i] = boardInfo[i].split(";")[1];
            }else {
                LIST_MENU = new String[1];
                Log.d("RESPONSE", "what is str : " + str.split(";")[1]);
                LIST_MENU[0]=str.split(";")[1];
            }
            Log.d("RESPONSE", "what is bu2 : " + LIST_MENU[0]);
            http.disconnect();
            /*while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것
            이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구
            }*/
        } catch (MalformedURLException e) {
            Log.d("RESPONSE", "TEX ");
            //
        } catch (IOException e) {
            e.printStackTrace();
            //
        } // try
    } // HttpPostData

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commuwrite:
                Log.d("RESPONSE", "onclicked");
                task = new community.BackgroundTask();
                task.execute(); // 서버와 자료 주고받기
                this.finish();
                break;
            case R.id.commucancel:
                this.finish();
        }
    }
}

