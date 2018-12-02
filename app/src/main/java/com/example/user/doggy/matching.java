package com.example.user.doggy;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;



public class matching extends AppCompatActivity {
    static String bu;
    static int flag=0;
    int alarm_num;
    String res, name, dogname, dogage, dogsex, dogtype, dogweight, size, dogcharacter;   // 서버에서 받아온 값들 넣을 변수
    TextView tv;
    connectInfo ci = new connectInfo();
    public String id;
    matching.BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        id = ci.getUserID();
        Log.d("RESPONSE", "what is id : "+id);


        task = new matching.BackgroundTask();
        task.execute();


        Button btn_match = (Button) findViewById(R.id.sendButton);
        btn_match.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), messMain.class);
                startActivity(intent);
            }
        });


        Button btn = (Button) findViewById(R.id.cancelButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), openMap.class);
                startActivity(intent);
            }
        });





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
            try {



                HttpPostData();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void HttpPostData() throws IOException {
        try{

            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://"+ci.getIP()+"/matchinfo.php");       // URL 설정
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
            buffer.append("id").append("=").append(id);

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
            StringBuilder builder = new StringBuilder();
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

            final String alarm;
            alarm  = ci.getAlarm();
            Log.d("RESPONSE", "what is res : "+res);

            Log.d("RESPONSE", "what is ci : "+ci.getAlarm());

            if(alarm != NULL){
                Log.d("RESPONSE", "what is alarm : "+alarm);
                final String information[] = alarm.split(":");
                Log.d("RESPONSE", "what is id : "+id);

                new Thread(new Runnable() {
                    @Override public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                final TextView textView1=(TextView)findViewById(R.id.idText);
                                textView1.setText(id);
                                final TextView textView2=(TextView)findViewById(R.id.nameText);
                                textView2.setText(information[0]);
                                final TextView textView3=(TextView)findViewById(R.id.dognameText);
                                textView3.setText(information[1]);
                                final TextView textView4=(TextView)findViewById(R.id.ageText);
                                textView4.setText(information[2]);
                                final TextView textView5=(TextView)findViewById(R.id.sexText);
                                textView5.setText(information[3]);
                                final TextView textView6=(TextView)findViewById(R.id.typeText);
                                textView6.setText(information[4]);
                                final TextView textView7=(TextView)findViewById(R.id.weightText);
                                textView7.setText(information[5]);
                                final TextView textView8=(TextView)findViewById(R.id.sizeText);
                                textView8.setText(information[6]);
                                final TextView textView9=(TextView)findViewById(R.id.characterText);
                                textView9.setText(information[7]);



                            }
                        });
                    }}).start();






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









}

