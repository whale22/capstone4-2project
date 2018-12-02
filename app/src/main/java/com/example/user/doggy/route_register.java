package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class route_register extends AppCompatActivity {

    static String bu;
    connectInfo ci = new connectInfo();
    BackgroundTask task;

    static String name;
    static int time;
    static String memo;
    static int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_register);

        Button btn_register = (Button) findViewById(R.id.register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task = new route_register.BackgroundTask();
                task.execute();
                Intent intent = new Intent(getApplicationContext(),route_list.class);
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
        }

    }


    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public void HttpPostData() {
        StringBuilder builder = new StringBuilder();
        try {

            Log.d("RESPONSE", "http://"+ci.getIP()+"/route.php");
            String response = null;

            EditText name = (EditText)findViewById(R.id.name);
            EditText memo = (EditText)findViewById(R.id.memo);
            EditText time = (EditText)findViewById(R.id.time);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String str_Datetime = sdfNow.format(date);
            String timestamp = str_Datetime.substring(0,13);
            Log.d("RESPONSE","TIMESTAMP : " + str_Datetime);

            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://"+ci.getIP()+"/route.php");       // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            //http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            StringBuffer buffer = new StringBuffer();
            //buffer.append("id").append("=").append(myId).append("&");                 // php 변수에 값 대입
            //buffer.append("pword").append("=").append(myPWord).append("&");   // php 변수 앞에 '$' 붙이지 않는다
            buffer.append("random").append("=").append(ci.getRandom()).append("&");
            buffer.append("name").append("=").append(name.getText().toString()).append("&");
            buffer.append("time").append("=").append(time.getText().toString()).append("&");   // 변수 구분은 '&' 사용
            buffer.append("datetime").append("=").append(str_Datetime).append("&");
            //Log.d("RESPONSE : ","timestamp length is " + timestamp.length());
            buffer.append("memo").append("=").append(memo.getText().toString());

            Log.d("RESPONSE", "The response2 is: " +buffer.toString());
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            Log.d("RESPONSE", "The response3 is: " +buffer.toString());
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            outStream.close();
            writer.close();
            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            String str;
            str=reader.readLine();
            bu=str.toString();
            Log.d("RESPONSE", "what is bu : "+bu);
            if(bu.startsWith("\"true\"")){
                Log.d("RESPONSE", "bu is true");
                flag=1;
                String[] spID=str.split(":");
                Log.d("RESPONSE", spID[1]);
                ci.setUserID(spID[1]);
            }

            else
                Log.d("RESPONSE", "bu is false");
            Log.d("RESPONSE", "The response4 is: " + bu);
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

}
