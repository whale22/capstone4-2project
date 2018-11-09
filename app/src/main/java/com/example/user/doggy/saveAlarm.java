package com.example.user.doggy;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationListener;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class saveAlarm extends AppCompatActivity implements OnClickListener {
        static String bu;
        static int flag = 0;
        TextView tv;
        connectInfo ci = new connectInfo();
        saveAlarm.BackgroundTask task;

        static int alarm_num = 0;
        static double mylocation_x = 0;
        static double mylocation_y = 0;

        String[] permission_list = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Location myLocation; //현재 사용자 위치


        LocationManager manager;//위치 정보를 관리하는 객체 추출

        GoogleMap map;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_function2);
            findViewById(R.id.addalarm).setOnClickListener(this);
            findViewById(R.id.back).setOnClickListener(this);
            findViewById(R.id.etc);

            //체크할 권한 배열
            String[] permission_list = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            // 현재 사용자 위치
            Location myLocation;

            // 위치 정보를 관리하는 매니저
            LocationManager manager;


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
                showMyLocation();
            }
            // 새롭게 위치를 측정한다.
            saveAlarm.GpsListener listener = new saveAlarm.GpsListener();

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
            mylocation_x = myLocation.getLatitude();
            mylocation_y = myLocation.getLongitude();
        }


//show 위치


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
                endPass();
            }

        }


        //------------------------------
        //   Http Post로 주고 받기
        //------------------------------
        public void HttpPostData() throws IOException {
            StringBuilder builder = new StringBuilder();
            try {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio1:
                        alarm_num = 1;
                        break;

                    case R.id.radio2:
                        alarm_num = 2;
                        break;

                    case R.id.radio3:
                        alarm_num = 3;
                        break;

                    case R.id.radio4:
                        alarm_num = 4;
                        break;

                    case R.id.radio5:
                        alarm_num = 5;
                        break;
                }
                String response = null;

                EditText dog_size = (EditText) findViewById(R.id.dogsize);
                EditText dog_color = (EditText) findViewById(R.id.dogcolor);
                EditText dog_type = (EditText) findViewById(R.id.dogtype);
                EditText information = (EditText) findViewById(R.id.etc);

                //--------------------------
                //   URL 설정하고 접속하기
                //--------------------------
                URL url = new URL("http://" + ci.getIP() + "/alarminsert.php");       // URL 설정
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
                buffer.append("alarm_type").append("=").append(alarm_num).append("&");           // 변수 구분은 '&' 사용
                buffer.append("location_x").append("=").append((float) mylocation_x).append("&");
                buffer.append("location_y").append("=").append((float) mylocation_y).append("&");
                buffer.append("dog_size").append("=").append(dog_size.getText().toString()).append("&");
                buffer.append("dog_color").append("=").append(dog_color.getText().toString()).append("&");
                buffer.append("dog_type").append("=").append(dog_type.getText().toString()).append("&");
                buffer.append("information").append("=").append(information.getText().toString());

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
                if (bu.equals("\"true\"")) {
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
                case R.id.back:
                    this.finish();
                    break;
                case R.id.addalarm:
                    checkPermission();
                    task = new saveAlarm.BackgroundTask();
                    task.execute();
                    break;
            }
        }


        public void endPass() {

            //정보가 있으면 다음으로 아니면 toast
            if (flag == 1) {
                Toast.makeText(saveAlarm.this, "알림정보가 입력되었습니다!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, login.class));
                flag = 0;
                this.finish();
            } else {
                Toast.makeText(saveAlarm.this, "입력 실패, 정보를 확인하세요!", Toast.LENGTH_SHORT).show();
            }
        }


    }


