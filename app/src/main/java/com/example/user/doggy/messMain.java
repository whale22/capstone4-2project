package com.example.user.doggy;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class messMain extends AppCompatActivity {
    connectInfo ci = new connectInfo();
    private String html = "";
    private Handler mHandler;
    private int messNum=1;
    private Socket socket;
    private String sendMessage="";

    String return_msg ;
    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    private String ip = "180.66.202.170";
    // IP
    private int port = 36420;
// PORT번호

    DataInputStream is;
    DataOutputStream os;
    boolean isConnected=true;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_main);
        mHandler = new Handler();

        checkUpdate.start();

        Button btn = (Button) findViewById(R.id.Button01);
        Button btn2 = (Button) findViewById(R.id.chatExit);
        final TextView tv = (TextView) findViewById(R.id.TextView01);

        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                RadioGroup radioGroup = findViewById(R.id.messRadio);

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.mr1:
                        messNum = 1;
                        break;

                    case R.id.mr2:
                        messNum = 2;
                        break;

                    case R.id.mr3:
                        messNum = 3;
                        break;

                    case R.id.mr4:
                        messNum = 4;
                        break;

                    case R.id.mr5:
                        messNum = 5;
                        break;

                    case R.id.mr6:
                        messNum = 5;
                        break;
                }
                switch(messNum){
                    case 1 : return_msg="안녕하세요."; break;
                    case 2 : return_msg="산책 같이 하실래요?"; break;
                    case 3 : return_msg="좋아요."; break;
                    case 4 : return_msg="아니요."; break;
                    case 5 : return_msg="도착지점에서 뵐게요."; break;
                    case 6 : return_msg="산책 즐겁게 하세요!"; break;
                } if(os==null) return;   //서버와 연결되어 있지 않다면 전송불가..


                sendMessage=ci.getUserID()+" : "+return_msg; //보낼 메세지

                //네트워크 작업이므로 Thread 생성

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        try {
                            os.writeUTF(sendMessage);  //서버로 메세지 보내기.UTF 방식으로(한글 전송가능...)
                            os.flush();        //다음 메세지 전송을 위해 연결통로의 버퍼를 지워주는 메소드..

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }//run method..

                }).start(); //Thread 실행..
                //PrintWriter out = new PrintWriter(networkWriter, true);
                //out.println(return_msg);
                tv.append(ci.getUserID()+" : "+return_msg+"\n");
            }
        });

        btn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                sendMessage="exit"; //보낼 메세지

                //PrintWriter out = new PrintWriter(networkWriter, true);
                //out.println(return_msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //종료 시
    }


    private Thread checkUpdate = new Thread() {

        public void run() {
            try {
                String line;
                Log.w("ChattingStart", "Start Thread");
                while (true) {
                    Log.w("Chatting is running", "chatting is running");
                    line = networkReader.readLine();
                    html = line;
                    mHandler.post(showUpdate);
                }
            } catch (Exception e) {

            }
        }
    };

    private Runnable showUpdate = new Runnable() {

        public void run() {
            Toast.makeText(messMain.this, "Coming word: " + html, Toast.LENGTH_SHORT).show();
        }

    };

    public void setSocket(String ip, int port) throws IOException {

        try {
            socket = new Socket(ip, port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

}
