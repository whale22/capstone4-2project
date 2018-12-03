package com.example.user.doggy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;


import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class route_list extends AppCompatActivity {

    private static final String TAG = "route_list";
    private AppCompatActivity mActivity;

    static String bu;
    String res;
    static int flag = 0;
    ListView listview;
    //ArrayList<String> items = new ArrayList<String>();
    listadapter adapter;
    connectInfo ci = new connectInfo();
    route_list.BackgroundTask task;
    ArrayList<route_model> routelist = new ArrayList<>();
    route_model model = new route_model();

    //경로이름 보내기 위한 변수
    private String Data;
    private ArrayList<String> ArrData = new ArrayList<>();
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        adapter = new listadapter(this,R.layout.item_list,routelist);
        // listview 생성 및 adapter 지정.
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        task = new route_list.BackgroundTask();
        task.execute();

//        btn = (Button)findViewById((R.id.boo));
//        btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(getApplicationContext(),detailed_route.class);
//                startActivity(intent);
//            }
//
//        });


        Log.d(TAG, "onCreate");
        mActivity = this;

        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                ListView listview = (ListView) parent;
//                String item = (String) listview.getItemAtPosition(position);
//                Toast.makeText(route_list.this, item, Toast.LENGTH_LONG).show();

                route_model model = (route_model)parent.getItemAtPosition(position);
                String name = model.getName();

                Log.d("RESPONSE", "what is name : "+ name);

                ci.setRandom(1);

                //아마 listview 모든 내용 긁어올 것 split 필요
//                String route_name[] = item.split(";");
//                route_model model = new route_model();
//                model.setName(route_name[0]);
//
                // name = model.getName();
//
//                ci.setName(name);

//               Toast.makeText(route_list.this,ci.getName(),Toast.LENGTH_LONG).show();

                //지도 띄우는 화면으로 이동
                Intent next = new Intent(getApplicationContext(),detailed_route.class);
                startActivity(next);
            }
        });



    }


    class BackgroundTask extends AsyncTask<Integer, Void, ArrayList<route_model>> {
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<route_model> doInBackground (Integer... arg0) {
            try {
                try {
                    URL url = new URL("http://" + ci.getIP() + "/routeinfo.php");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    Log.d("RESPONSE","http://"+ci.getIP()+"/routeinfo.php");

                    http.setDoInput(true);
                    http.setDoOutput(true);
                    http.setRequestMethod("POST");


                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    str = reader.readLine();
                    bu = str.toString();
                    Log.d("RESPONSE","str" + str);


                    String id[] = str.split(";");

                    for(int i =0;i<id.length/4;i++){
                        builder.append(str + "\n");
                        Log.d("RESPONSE","It is Okay");

                        Log.d("RESPONSE","id[0] : " + id[0]);
                        Log.d("RESPONSE","id[1] : " + id[1]);
                        Log.d("RESPONSE","id[2] : " + id[2]);
                        Log.d("RESPONSE","id[3] : " + id[3]);




                        route_model model = new route_model();
                        model.setName(id[0 + i*4]);
                        model.setTime(id[1 + i*4]);
                        model.setDatetime(id[2 + i*4]);
                        model.setMemo(id[3 + i*4]);

//                        String name = model.getName();
//                        ci.setName(name);

                        routelist.add(model);

                        str = reader.readLine();
                        Log.d("RESPONSE","str " + str);

                    }



                } catch (MalformedURLException e) {
                    Log.d("RESPONSE", "TEX");
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return routelist;
        }

        protected void onPostExecute(ArrayList<route_model> a) {
            super.onPostExecute(a);
            Log.d("RESPONSE","routelist : " +routelist.isEmpty());
            adapter.setList(a);
            adapter.notifyDataSetChanged();
            //runOnUiThread(new Runnable() {
            //@Override
            // public void run() {
            // adapter.notifyDataSetChanged();
            // }
            //});
        }
    }


}//route_list

