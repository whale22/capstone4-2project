package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class commentWrite extends AppCompatActivity implements View.OnClickListener {
    static int flag = 0;
    EditText commentWrite;
    connectInfo ci = new connectInfo();
    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);
        findViewById(R.id.ccw_button).setOnClickListener(this);
        findViewById(R.id.cc_button).setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) { //oncreate 에서 바로 쓰레드돌릴려고 임시방편으로 넣어둔소스
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
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




    public void HttpPostData() {
        StringBuilder builder = new StringBuilder();
        try {
            String response = null;
            commentWrite = (EditText) findViewById(R.id.ccwedit);
            StringBuffer buffer = new StringBuffer();
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://"+ci.getIP()+"/commentinsert.php");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            //http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            buffer.append("id").append("=").append(ci.getUserID()).append("&");           // 변수 구분은 '&' 사용
            buffer.append("title").append("=").append(commuContent.titleCon).append("&");         // 변수 구분은 '&' 사용
            buffer.append("comment").append("=").append(commentWrite.getText().toString());
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            Log.d("RESPONSE", "The response3 is: " +buffer.toString());
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            outStream.close();
            writer.close();

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
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
            case R.id.ccw_button:
                task = new commentWrite.BackgroundTask();
                task.execute(); // 서버와 자료 주고받기
                startActivity(new Intent(this, commuContent.class));
                this.finish();
                break;
            case R.id.cc_button:
                this.finish();
        }
    }
}
