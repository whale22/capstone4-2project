package com.example.user.doggy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class function2 extends AppCompatActivity implements View.OnClickListener {
    static String bu;
    static int flag=0;
    TextView tv;
    connectInfo ci = new connectInfo();
    BackgroundTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function2);
        findViewById(R.id.addalarm).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.etc);
        if (android.os.Build.VERSION.SDK_INT > 9) { //oncreate 에서 바로 쓰레드돌릴려고 임시방편으로 넣어둔소스
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

    }

    private void show() throws ExecutionException, InterruptedException {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        EditText edit = (EditText)findViewById(R.id.etc);

        switch(radioGroup.getCheckedRadioButtonId()){
            case R.id.radio1:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.notify);
                builder.setLargeIcon(largeIcon);
                builder.setContentTitle("신고");
                builder.setContentText("근처에 비매너 행동을 하는 주인이 있습니다.");
                break;

            case R.id.radio2:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("유기견 발견");
                builder.setContentText("근처에 유기 반려견이 있는 것으로 추정됩니다.");
                break;

            case R.id.radio3:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap largeIcon2 = BitmapFactory.decodeResource(getResources(),R.drawable.caution);
                builder.setLargeIcon(largeIcon2);
                builder.setContentTitle("주의");
                builder.setContentText("근처에 공격형 반려견이 있습니다. 조심하세요.");
                break;

            case R.id.radio4:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("산책로 청결 불량");
                builder.setContentText("현재 산책로의 청결 상태가 좋지 않습니다.");
                break;

            case R.id.radio5:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap largeIcon3 = BitmapFactory.decodeResource(getResources(),R.drawable.etc);
                builder.setLargeIcon(largeIcon3);
                task = new function2.BackgroundTask();
                task.execute().get();

                String str = edit.getText().toString();
                builder.setContentTitle("기타 제보 알림");
                Log.d("RESPONSE", "what is ci : "+ci.getAlarm());
                builder.setContentText(ci.getAlarm());
                break;
        }
        //밑에 세가지는 알림에 뜰 내용!!
        /*builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알림 제목");
        builder.setContentText("알림 세부 텍스트");*/

        //액션정의
        Intent intent = new Intent(this,function2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //클릭이벤트 설정
        builder.setContentIntent(pendingIntent);
        //큰 아이콘
        /*Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);*/

        builder.setColor(Color.RED);//색상
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        long[] vibrate = {0,100,200,300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.0) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }*/
        manager.notify(1, builder.build());

    }
    public void createNotification(View view) throws ExecutionException, InterruptedException {
        show();
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


        }

    }




    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public void HttpPostData() throws IOException {
        StringBuilder builder = new StringBuilder();
        try {

            String response = null;
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://"+ci.getIP()+"/alarm.php");       // URL 설정
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

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            String str;
            str=reader.readLine();
            bu=str.toString();
            Log.d("RESPONSE", "what is bu : "+bu);
            ci.setAlarm(bu);
            if(bu.equals("")){
                ci.setAlarm("주의하실 사항이 없습니다!");
                Log.d("RESPONSE", "bu is true");
                flag=1;
            }
            Log.d("RESPONSE", "what is ci : "+ci.getAlarm());
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
            case R.id.back:
                this.finish();
                break;
            case R.id.addalarm:
                try {
                    show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}



