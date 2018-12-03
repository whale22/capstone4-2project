package com.example.user.doggy;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class detailed_route extends FragmentActivity implements OnMapReadyCallback {

    // 구글 맵 참조변수 생성
    private GoogleMap mMap;
    detailed_route.BackgroundTask task;
    Marker routemarker;
    route_model model = new route_model();

    ArrayList<route_model> routelist = new ArrayList<>();
    connectInfo ci = new connectInfo();
    static String bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_route);

        task = new detailed_route.BackgroundTask();
        task.execute();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

//        // 서울에 대한 위치 설정
//        LatLng seoul = new LatLng(37.52487, 126.92723);
//
//        // 구글 맵에 표시할 마커에 대한 옵션 설정
//        MarkerOptions makerOptions = new MarkerOptions();
//        makerOptions
//                .position(seoul)
//                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.");
//
//        // 마커를 생성한다.
//        mMap.addMarker(makerOptions);
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
//
//        //카메라를 서울 위치로 옮긴다.
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        LatLng position = null;
        for(route_model model : routelist){
            Log.d("RESPONSE : ", "sizeof :" + routelist.size());
            position = new LatLng(model.getLatitude(), model.getLongitude());
            Log.d("RESPONSE","check" + model.getLatitude());
            Log.d("RESPONSE","check" + model.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            Log.d("RESPONSE : ", "message : " + 111);
            markerOptions.position(position);
            Log.d("RESPONSE : ", "message : " + 222);
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.basic);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,50,50,false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            Log.d("RESPONSE : ", "message : " + 333);
            mMap.addMarker(markerOptions);

        }
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,17));
        Log.d("RESPONSE : ", "message : " + 444);



    }

//    private Marker addMarker(route_model routemodel) {
//
//
//        LatLng position = new LatLng(routemodel.getLatitude(), routemodel.getLongitude());
//        Log.d("RESPONSE : ", "Check Location:" + routemodel.getLatitude());
//        Log.d("RESPONSE : ", "Check Location:" + routemodel.getLongitude());
//
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        Log.d("RESPONSE : ", "message : " + 111);
//        markerOptions.position(position);
//        Log.d("RESPONSE : ", "message : " + 222);
//
//
//
//         return mMap.addMarker(markerOptions);
//
//    }



    class BackgroundTask extends AsyncTask<Integer, Integer, ArrayList> {
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<route_model> doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            //route_model model = new route_model();
            ArrayList<route_model> routelist = new ArrayList<>();
            try {
                routelist = HttpPostData();
            } catch(NullPointerException e){
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }

            return routelist;
        }

        protected void onPostExecute(ArrayList routelist2) {
            //tv.setText(result);
            //endPass();
            //addMarker(model);

            routelist = routelist2;
            Log.d("RESPONs : ","routelistsize : " + routelist2.size());
            callMap();
        }

    }

    public void callMap(){
        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // getMapAsync must be called on the main thread.
        Log.d("RESPONSE","callMap");
    }
    //------------------------------
    //   Http Post로 주고 받기
    //------------------------------
    public ArrayList<route_model> HttpPostData() {
        StringBuilder builder = new StringBuilder();



        try {

            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://"+ci.getIP()+"/routelalo.php");       // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            Log.d("RESPONSE", "http://"+ci.getIP()+"/routelalo.php");
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
            Log.d("RESPONSE","Name : " + ci.getName());
            buffer.append("random").append("=").append(ci.getRandom()).append("&");
            buffer.append("name").append("=").append(ci.getName());//받아온 경로이름 값 전달


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
            bu = str.toString();
            Log.d("RESPONSE","str : " + str);


            String id[] = str.split(";");
            Log.d("RESPONSE : ","length : " + id.length);
            for(int i= 0;i<id.length/2;i++){
                route_model model = new route_model();

                builder.append(str + "\n");

                Log.d("RESPONSE","id[0] : " + id[0+i*2]);
                Log.d("RESPONSE","id[1] : " + id[1+i*2]);

                if(i==0)
                {

                    id[0]=id[0].substring(1);
                    model.setLongitude(id[0]);
                    //Log.d("RESPONSE","err : " + id[0]);
                    model.setLatitude(id[1]);
                    //addMarker(model);
                    routelist.add(model);

                }
                else
                {
                    //route_model model = new route_model();
                    model.setLongitude(id[0 + i*2]);
                    model.setLatitude(id[1 + i*2]);

                    routelist.add(model);
                    Log.d("add" , "" + i);
                    //addMarker(model);

                }
//                str = reader.readLine();
//                if(str == null)
//                    break;

            }



        } catch (MalformedURLException e) {
            Log.d("RESPONSE", "TEX ");
            //
        } catch (IOException e) {
            e.printStackTrace();
            //
        } // try
        return routelist;
    } // HttpPostData
}