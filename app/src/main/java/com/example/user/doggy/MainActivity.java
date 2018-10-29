package com.example.user.doggy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText input01;//전역으로 깔지 않으면 setOnclickLister 안에서 참조가 안됨
    connectInfo ci = new connectInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
        Button btn = (Button)findViewById(R.id.btn);
        //버튼과 ip입력칸 등록
        //Button button01 = (Button) findViewById(R.id.button01);
        input01 = (EditText)findViewById(R.id.input01);


        //button01의 클릭 이벤트. 클릭하면 input01에서 ip주소를 받아와 연결한다.
        /*button01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String addr = input01.getText().toString().trim();//ip주소 받기
                ConnectTread thread = new ConnectTread(addr);//통신용 스레드 생성
                thread.start();
            }
        });*/


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn:
                ci.setIP(input01.getText().toString());
                startActivity(new Intent(this, login.class));
                this.finish();
        }
    }

    class ConnectTread extends Thread{
        String hostname = null;

        public ConnectTread(String addr){
            hostname = addr;
        }

        public void run(){
            try{
                int port = 36420;//이 포트는 서버와 일치해야만 함
                Socket sock = new Socket(hostname, port);

                //쓰기 객체를 만들어 보내기
                ObjectOutputStream outstram = new ObjectOutputStream(sock.getOutputStream());
                outstram.writeObject("Hello AndroidTown on Android");//서버에서 받을 메시지
                outstram.flush();

                //스트림 객체로 데이터 읽기
                ObjectInputStream instram = new ObjectInputStream(sock.getInputStream());
                String obj = (String) instram.readObject();

                //로그로 출력
                Log.d("MainActivity", "서버에서 받은 메시지 : " + obj);

                sock.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}



