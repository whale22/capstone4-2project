package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
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

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class usermodify extends AppCompatActivity implements OnClickListener {
    static String bu;
    static int flag = 0;
    TextView tv;
    connectInfo ci = new connectInfo();
    usermodify.BackgroundTask task;
    public String myID;
    String res, size, color, type, information;   // 서버에서 받아온 값들 넣을 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermodify);
        findViewById(R.id.cancelButton).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        //tv= findViewById(R.id.resultText);

        myID = ci.getUserID();


        task = new usermodify.BackgroundTask();
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
            //tv.setText(result);
            //endPass();
        }

    }


    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public void HttpPostData() {
        StringBuilder builder = new StringBuilder();

        try {

            Log.d("RESPONSE", "http://" + ci.getIP() + "/showuserinfo.php");
            String response = null;


            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://" + ci.getIP() + "/showuserinfo.php");       // URL 설정

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
            buffer.append("id").append("=").append(myID);
            Log.d("RESPONSE", "myid " + myID);

            Log.d("RESPONSE", "The response2 is: " + buffer.toString());
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            Log.d("RESPONSE", "The response3 is: " + buffer.toString());
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            outStream.close();
            writer.close();

            final EditText user_id = (EditText) findViewById(R.id.idText);
            final EditText user_password = (EditText) findViewById(R.id.passwordText);
            final EditText user_name = (EditText) findViewById(R.id.nameText);
            final EditText user_address = (EditText) findViewById(R.id.addressText);
            final EditText dog_name = (EditText) findViewById(R.id.dognameText);
            final EditText dog_age = (EditText) findViewById(R.id.ageText);
            final EditText dog_sex = (EditText) findViewById(R.id.sexText);
            final EditText dog_type = (EditText) findViewById(R.id.typeText);
            final EditText dog_weight = (EditText) findViewById(R.id.weightText);
            final EditText dog_size = (EditText) findViewById(R.id.sizeText);
            final EditText dog_character = (EditText) findViewById(R.id.characterText);
            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            String str;
            str = reader.readLine();
            bu = str.toString();
            Log.d("RESPONSE", "what is bu : " + bu);
            if (bu.length() >= 5) {
                Log.d("RESPONSE", "bu is true");
            } else
                Log.d("RESPONSE", "bu is false");
            //str=reader.readLine();
            //if(str!=null) bu=str.toString();
            Log.d("RESPONSE", "The response4 is: " + bu);
            http.disconnect();
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구
            }
            res = builder.toString();
            // ((TextView)(findViewById(R.id.textView2))).setText(res);
            ci.setAlarm(bu);

            final String match_id;
            match_id = ci.getAlarm();
            Log.d("RESPONSE", "what is res : " + res);

            Log.d("RESPONSE", "what is ci : " + ci.getAlarm());

            if (match_id != NULL) {
                Log.d("RESPONSE", "what is alarm : " + match_id);
                final String information[] = match_id.split(":");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                    user_id.setText(myID);
                                    user_password.setText(information[0]);
                                    user_name.setText(information[1]);
                                    user_address.setText(information[2]);
                                    dog_name.setText(information[3]);
                                    dog_age.setText(information[4]);
                                    dog_sex.setText(information[5]);
                                    dog_type.setText(information[6]);
                                    dog_weight.setText(information[7]);
                                    dog_size.setText(information[8]);
                                    dog_character.setText(information[9]);


                            }
                        });
                    }
                }).start();

                ci.setAlarm(NULL);
            }


        } catch (MalformedURLException e) {
            Log.d("RESPONSE", "TEX ");
            //
        } catch (IOException e) {
            e.printStackTrace();
            //
        } // try
    } // HttpPostData


    public void HttpPostData2() {
        StringBuilder builder = new StringBuilder();
        try {

            Log.d("RESPONSE", "http://" + ci.getIP() + "/updateuserinfo.php");
            String response = null;
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://" + ci.getIP() + "/updateuserinfo.php");       // URL 설정

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

            final EditText user_id = (EditText) findViewById(R.id.idText);
            final EditText user_password = (EditText) findViewById(R.id.passwordText);
            final EditText user_name = (EditText) findViewById(R.id.nameText);
            final EditText user_address = (EditText) findViewById(R.id.addressText);
            final EditText dog_name = (EditText) findViewById(R.id.dognameText);
            final EditText dog_age = (EditText) findViewById(R.id.ageText);
            final EditText dog_sex = (EditText) findViewById(R.id.sexText);
            final EditText dog_type = (EditText) findViewById(R.id.typeText);
            final EditText dog_weight = (EditText) findViewById(R.id.weightText);
            final EditText dog_size = (EditText) findViewById(R.id.sizeText);
            final EditText dog_character = (EditText) findViewById(R.id.characterText);

            StringBuffer buffer = new StringBuffer();
            //buffer.append("id").append("=").append(myId).append("&");                 // php 변수에 값 대입
            //buffer.append("pword").append("=").append(myPWord).append("&");   // php 변수 앞에 '$' 붙이지 않는다
            buffer.append("id").append("=").append(myID).append("&");
            buffer.append("pass").append("=").append(user_password.getText().toString()).append("&");
            buffer.append("name").append("=").append(user_name.getText().toString()).append("&");
            buffer.append("address").append("=").append(user_address.getText().toString()).append("&");
            buffer.append("dog_name").append("=").append(dog_name.getText().toString()).append("&");
            buffer.append("dog_age").append("=").append(dog_age.getText().toString()).append("&");
            buffer.append("dog_sex").append("=").append(dog_sex.getText().toString()).append("&");
            buffer.append("dog_type").append("=").append(dog_type.getText().toString()).append("&");
            buffer.append("dog_weight").append("=").append(dog_weight.getText().toString()).append("&");
            buffer.append("dog_size").append("=").append(dog_size.getText().toString()).append("&");
            buffer.append("dog_character").append("=").append(dog_character.getText().toString());

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
            String str;
            str = reader.readLine();
            bu = str.toString();
            Log.d("RESPONSE", "what is bu : " + bu);
            if (bu.length() >= 5) {
                Log.d("RESPONSE", "bu is true");
                flag = 1;
            } else
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
                HttpPostData2();
                endPass();
                //tv.setText(bu);
                break;
        }
    }
    public void endPass(){

        //정보가 있으면 다음으로 아니면 toast
        if(flag==1){
            Toast.makeText(usermodify.this, "회원정보 수정이 완료되었습니다!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,extrafunction.class));
            flag=0;
            this.finish();
        }else{
            Toast.makeText(usermodify.this, "회원정보 수정 실패, 정보를 확인하세요!", Toast.LENGTH_SHORT).show();
        }
    }
}
