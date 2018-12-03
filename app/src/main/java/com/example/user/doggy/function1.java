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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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



public class function1 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    static String bu;
    static int flag=0;
    static double lat = 0;
    static double lng = 0;
    int alarm_num;
    String res, size, color, type, information;   // 서버에서 받아온 값들 넣을 변수
    TextView tv;
    connectInfo ci = new connectInfo();
    function1.BackgroundTask task;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function1);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        final TextView tv_out_mylocation = (TextView) findViewById(R.id.tv_out_mylocation);

        // 위치 정보를 관리하는 매니저
        LocationManager manager;

        Button btn_mylocation = (Button) findViewById(R.id.btn_mylocation);
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task = new function1.BackgroundTask();
                task.execute();
                tv_out_mylocation.setText("위도 : " + lat + "\n");
                tv_out_mylocation.append("경도 : " + lng);
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
                Intent intent = new Intent(getApplicationContext(), extrafunction.class);
                startActivity(intent);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) { //oncreate 에서 바로 쓰레드돌릴려고 임시방편으로 넣어둔소스
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

    }




    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lng = mLocation.getLongitude();
        } else {
            // Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

    }
// 비어있을때 푸쉬알림 안뜨게
    public void pushalarm(String str1) {
        String str;
        //<br />을 문자열에서 제거한다
        str = str1.replace("<br />","");
        Log.d("pushalarm", "push" + str+"push");
        String string1= "";
        if(str.equals(string1)) {

        } else{
            // split()을 이용해 ':'를 기준으로 문자열을 자른다.
            // split()은 지정한 문자를 기준으로 문자열을 잘라 배열로 반환한다.
            String alarm[] = str.split(":");
            Log.d("pushalarm", "push" + alarm[0]);
            //Log.d("pushalarm", "push" + str);
            String temp;
            for (int i = 0; i < alarm.length; i++) {
                Log.d("pushalarm", "array push" + alarm[i]+"push");
                temp = alarm[i];
                temp.substring(1);
                if (temp.equals("")) {
                    Log.d("null", "null" + alarm[i]+"push");

                } else {
                    addNotification(alarm[i], i);
                    Log.d("null", "not null" + alarm[i] + "push");
                }
            }
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




}

