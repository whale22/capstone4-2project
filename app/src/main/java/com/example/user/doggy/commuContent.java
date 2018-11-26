package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class commuContent extends AppCompatActivity implements View.OnClickListener {
    static String titleCon;
    TextView title;
    TextView content;
    TextView comment;
    EditText commentWrite;
    connectInfo ci = new connectInfo();
    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_content);
        title = (TextView) findViewById(R.id.commutitle);
        title.setText(titleCon);
        findViewById(R.id.ccwbutton).setOnClickListener(this);
        task = new commuContent.BackgroundTask();
        task.execute(); // 서버와 자료 주고받기

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




    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public void HttpPostData() {
        StringBuilder builder = new StringBuilder();
        try {
            String response = null;
            content = (TextView) findViewById(R.id.commucontent);
            comment = (TextView) findViewById(R.id.commuComment);
            StringBuffer buffer = new StringBuffer();
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
                URL url = new URL("http://" + ci.getIP() + "/boardinfo.php");       // URL 설정

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

                //buffer.append("id").append("=").append(myId).append("&");                 // php 변수에 값 대입
                //buffer.append("pword").append("=").append(myPWord).append("&");   // php 변수 앞에 '$' 붙이지 않는다
                buffer.append("title").append("=").append(titleCon);

                Log.d("RESPONSE", "The response2 is: " + buffer.toString());
                OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                Log.d("RESPONSE", "The response3 is: " + buffer.toString());
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
                final String str;
                str = reader.readLine();
                Log.d("RESPONSE", "what is bu : " + str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String boardInfo;
                        if (str.contains("%")) {
                            boardInfo = str.split("%")[0];
                            Log.d("RESPONSE", "what is bu : " + boardInfo.split("&")[1]);
                            content.setText(boardInfo.split("&")[0] + "\n" + boardInfo.split("&")[1]);
                            String com = str.split("%")[1];
                            String comt;
                            while (com.contains("#")) {
                                comt = com.split("#")[0];
                                comment.append(comt.split(";")[0] + " : " + comt.split(";")[1] + " " + comt.split(";")[2] + "\n");
                                com=com.substring(com.indexOf("#")+1);
                                Log.d("RESPONSE", "what is com : " + com);
                            }
                            comment.append(com.split(";")[0] + " : " + com.split(";")[1] + " " + com.split(";")[2]);
                        } else {
                            comment.setText("작성된 댓글이 없습니다.");
                        }
                    }
                });

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
            case R.id.ccwbutton:
                startActivity(new Intent(this, commentWrite.class));
                break;
        }
    }
}

