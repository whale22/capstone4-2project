package com.example.user.doggy;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import java.util.concurrent.ExecutionException;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;



public class function1 extends AppCompatActivity {
    static String bu;
    static int flag=0;
    static double lat = 0;
    static double lng = 0;
    int alarm_num;
    String res, size, color, type, information;   // 서버에서 받아온 값들 넣을 변수
    TextView tv;
    connectInfo ci = new connectInfo();
    function1.BackgroundTask task;

    String[] permission_list = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    Location myLocation; //현재 사용자 위치

    TextView tv_out_mylocation;

    LocationManager manager;//위치 정보를 관리하는 객체 추출

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function1);

        //체크할 권한 배열
        String[] permission_list = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        // 현재 사용자 위치
        Location myLocation;

        tv_out_mylocation = (TextView) findViewById(R.id.tv_out_mylocation);

        // 위치 정보를 관리하는 매니저
        LocationManager manager;

        Button btn_mylocation = (Button) findViewById(R.id.btn_mylocation);
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPermission();
                task = new function1.BackgroundTask();
                task.execute();
                Log.d("latitude", "latitude : "+lat);
            }
        });

        Button btn_map = (Button) findViewById(R.id.map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), openMap.class);
                startActivity(intent);
            }
        });


        Button btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) { //oncreate 에서 바로 쓰레드돌릴려고 임시방편으로 넣어둔소스
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }


    }





    public void checkPermission() {
        boolean isGrant = false;
        for (String str : permission_list) {
            if (ContextCompat.checkSelfPermission(this, str) == PackageManager.PERMISSION_GRANTED) {
            } else {
                isGrant = false;
                break;
            }
        }
        if (isGrant == false) {
            ActivityCompat.requestPermissions(this, permission_list, 0);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant = true;
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                isGrant = false;
                break;
            }
        }
        // 모든 권한을 허용했다면 사용자 위치를 측정한다.
        if (isGrant == true) {
            getMyLocation();
        }
    }


    //현재 위치 가져오는 함수
    public void getMyLocation() {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 권한이 모두 허용되어 있을 때만 동작하도록 한다.
        int chk1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int chk2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (chk1 == PackageManager.PERMISSION_GRANTED && chk2 == PackageManager.PERMISSION_GRANTED) {
            myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        // 새롭게 위치를 측정한다.
        GpsListener listener = new GpsListener();

        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, listener);
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }

    }

    class GpsListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // 현재 위치 값을 저장한다.
            myLocation = location;
            // 위치 측정을 중단한다.
            manager.removeUpdates(this);
            // 지도를 현재 위치로 이동시킨다.
            showMyLocation();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }
    }

    public void showMyLocation() {
        // LocationManager.GPS_PROVIDER 부분에서 null 값을 가져올 경우를 대비하여 장치
        if (myLocation == null) {
            return;
        }
        // 현재 위치값을 추출한다.
        lat = myLocation.getLatitude();
        lng = myLocation.getLongitude();
        Log.d("location", "location"+lat);
        tv_out_mylocation.setText("위도 : " + lat + "\n");
        tv_out_mylocation.append("경도 : " + lng);
    }

    public void pushalarm(String str1) {
        String str;
        //<br />을 문자열에서 제거한다
        str = str1.replace("<br />","");
        Log.d("pushalarm", "push" + str);

        // split()을 이용해 ':'를 기준으로 문자열을 자른다.
        // split()은 지정한 문자를 기준으로 문자열을 잘라 배열로 반환한다.
        String alarm[] = str.split(":");
        Log.d("pushalarm", "push" + alarm[0]);
        //Log.d("pushalarm", "push" + str);

        for (int i = 0; i < alarm.length; i++) {
            Log.d("pushalarm", "push" + alarm[i]);
            if(alarm[i].equals("") ){
            }
            else
            addNotification(alarm[i],i);
        }
    }


    public void addNotification(String str,int notifyId)
    {

        NotificationManager notificationManager= (NotificationManager)function1.this.getSystemService(function1.this.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(function1.this.getApplicationContext(),function1.class);

        Notification.Builder builder1 = new Notification.Builder(getApplicationContext());



        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);//현재 액티비티를 최상으로 올리고, 최상의 액티비티를 제외한 모든 액티비티없앤다.



        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( function1.this,0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent는 일회용 인텐트 같은 개념입니다.



        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.notify);

        builder1.setLargeIcon(largeIcon);



        builder1.setSmallIcon(R.drawable.notify).setTicker("HETT").setWhen(System.currentTimeMillis())

                .setContentText(str)

                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);



        notificationManager.notify(notifyId, builder1.build()); // Notification send


    }


    public static void main(String args[])
    {
        // 특정 문자가 반복될 경우 : '-' 가 반복된다.
        String birthday = "2016-11-15";

        // split()을 이용해 '-'를 기준으로 문자열을 자른다.
        // split()은 지정한 문자를 기준으로 문자열을 잘라 배열로 반환한다.
        String date[] = birthday.split(".");

        for(int i=0 ; i<date.length ; i++)
        {
            System.out.println("date["+i+"] : "+date[i]);
        }
    }



// .setContentTitle("푸쉬 제목")

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
            StringBuffer buffer = new StringBuffer();
            //buffer.append("id").append("=").append(myId).append("&");                 // php 변수에 값 대입
            //buffer.append("pword").append("=").append(myPWord).append("&");   // php 변수 앞에 '$' 붙이지 않는다
            buffer.append("latitude").append("=").append((float)lat).append("&");           // 변수 구분은 '&' 사용
            buffer.append("longitude").append("=").append((float)lng);

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

            String alarm;
            alarm  = ci.getAlarm();
            Log.d("RESPONSE", "what is res : "+res);

            Log.d("RESPONSE", "what is ci : "+ci.getAlarm());

            if(alarm != NULL){
                Log.d("RESPONSE", "what is alarm : "+alarm);
                pushalarm(alarm);
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







/*
    public void alarm(int alarm_num) throws ExecutionException, InterruptedException {
        Notification.Builder builder = new Notification.Builder(this);


        switch (alarm_num) {
            case 1:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notify);
                builder.setLargeIcon(largeIcon);
                builder.setContentTitle("신고");
                builder.setContentText("근처에 비매너 행동을 하는 주인이 있습니다.");
                break;

            case 2:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("산책로 청결 불량");
                builder.setContentText("현재 산책로의 청결 상태가 좋지 않습니다.");
                break;

            case 3:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap largeIcon2 = BitmapFactory.decodeResource(getResources(), R.drawable.caution);
                builder.setLargeIcon(largeIcon2);
                builder.setContentTitle("주의");
                builder.setContentText("근처에 공격형 반려견이 있습니다. 조심하세요.");
                break;

            case 4:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("유기견 발견");
                builder.setContentText("근처에"+size+" "+color+" 의 "+type+" 종의 유기 반려견이 있는 것으로 추정됩니다.");
                break;

            case 5:
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Bitmap largeIcon3 = BitmapFactory.decodeResource(getResources(), R.drawable.etc);
                builder.setLargeIcon(largeIcon3);
                builder.setContentTitle("기타 제보 알림");
                builder.setContentText(information);
             //   Log.d("RESPONSE", "what is ci : " + ci.getAlarm());
            //    builder.setContentText(ci.getAlarm());
                break;
        }


    }

*/



}

