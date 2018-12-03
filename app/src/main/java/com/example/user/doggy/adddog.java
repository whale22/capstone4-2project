package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

public class adddog extends AppCompatActivity implements OnClickListener {
    static String bu;
    static int flag=0;
    TextView tv;
    connectInfo ci = new connectInfo();
    adddog.BackgroundTask task;
    String myid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddog);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//액션바 숨기기
        findViewById(R.id.cancelButton).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        //tv= findViewById(R.id.resultText);
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
            //tv.setText(result);
            endPass();
        }

    }




    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public void HttpPostData() {
        StringBuilder builder = new StringBuilder();
        try {

            Log.d("RESPONSE", "http://"+ci.getIP()+"/infoinsert.php");
            String response = null;
            EditText dog_name = (EditText)findViewById(R.id.dognameText);
            EditText dog_age = (EditText)findViewById(R.id.ageText);
            EditText dog_sex = (EditText)findViewById(R.id.sexText);
            EditText dog_type = (EditText)findViewById(R.id.typeText);
            EditText dog_weight = (EditText)findViewById(R.id.weightText);
            EditText dog_size = (EditText)findViewById(R.id.sizeText);
            EditText dog_character = (EditText)findViewById(R.id.characterText);
            myid = ci.getUserID();
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://"+ci.getIP()+"/dogadd.php");       // URL 설정

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

            StringBuffer buffer = new StringBuffer();
            //buffer.append("id").append("=").append(myId).append("&");                 // php 변수에 값 대입
            //buffer.append("pword").append("=").append(myPWord).append("&");   // php 변수 앞에 '$' 붙이지 않는다
            buffer.append("id").append("=").append(myid).append("&");
            buffer.append("dog_name").append("=").append(dog_name.getText().toString()).append("&");
            buffer.append("dog_age").append("=").append(dog_age.getText().toString()).append("&");
            buffer.append("dog_sex").append("=").append(dog_sex.getText().toString()).append("&");
            buffer.append("dog_type").append("=").append(dog_type.getText().toString()).append("&");
            buffer.append("dog_weight").append("=").append(dog_weight.getText().toString()).append("&");
            buffer.append("dog_size").append("=").append(dog_size.getText().toString()).append("&");
            buffer.append("dog_character").append("=").append(dog_character.getText().toString());

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
            if(bu.length()>=5){
                Log.d("RESPONSE", "bu is true");
                flag=1;
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                startActivity(new Intent(this,extrafunction.class));
                break;
            case R.id.send:
                task = new adddog.BackgroundTask();
                task.execute(); // 서버와 자료 주고받기
                //tv.setText(bu);
                break;
        }
    }
    public void endPass(){

        //정보가 있으면 다음으로 아니면 toast
        if(flag==1){
            Toast.makeText(adddog.this, "반려견 추가가 완료되었습니다!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,extrafunction.class));
            flag=0;
            this.finish();
        }else{
            Toast.makeText(adddog.this, "반려견 추가 실패, 정보를 확인하세요!", Toast.LENGTH_SHORT).show();
        }
    }
}
