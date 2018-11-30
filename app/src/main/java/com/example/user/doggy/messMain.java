package com.example.user.doggy;
import java.io.BufferedReader;  //우와 많다 ㅎㅎ..
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class messMain extends Activity {    //메인 activity 시작!

    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    EditText input;         //화면구성
    Button button, button2;          //화면구성
    TextView output;        //화면구성
    //
    String data = null;
    connectInfo ci = new connectInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_main);
//start
        //input = (EditText) findViewById(R.id.input); // 글자입력칸을 찾는다.
        button = (Button) findViewById(R.id.buttonsend); // 버튼을 찾는다.
        button2 = (Button) findViewById(R.id.exitsend);
        output = (TextView) findViewById(R.id.output); // 글자출력칸을 찾는다.
        output.setMovementMethod(new ScrollingMovementMethod());
// 버튼을 누르는 이벤트 발생, 이벤트 제어문이기 때문에 이벤트 발생 때마다 발동된다. 시스템이 처리하는 부분이 무한루프문에
//있더라도 이벤트가 발생하면 자동으로 실행된다.
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //String data=null; //글자입력칸에 있는 글자를 String 형태로 받아서 data에 저장
//버튼이 클릭되면 소켓에 데이터를 출력한다.
                RadioGroup radioGroup = findViewById(R.id.messRadio);
                int messNum = 0;
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
                        messNum = 6;
                        break;
                }
                switch (messNum) {
                    case 1:
                        data = "안녕하세요.";
                        break;
                    case 2:
                        data = "산책 같이 하실래요?";
                        break;
                    case 3:
                        data = "좋아요.";
                        break;
                    case 4:
                        data = "아니요.";
                        break;
                    case 5:
                        data = "도착지점에서 뵐게요.";
                        break;
                    case 6:
                        data = "산책 즐겁게 하세요!";
                        break;
                } //서버와 연결되어 있지 않다면 전송불가..

                Log.w("NETWORK", " " + data);
                if (data != null) { //만약 데이타가 아무것도 입력된 것이 아니라면
                    out.println(ci.getUserID() + " : " + data); //data를   stream 형태로 변형하여 전송.  변환내용은 쓰레드에 담겨 있다.
                }
            }
        });

        Thread worker = new Thread() {    //worker 를 Thread 로 생성
            public void run() { //스레드 실행구문
                try {
//소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                    socket = new Socket("180.66.202.170", 8888); //소켓생성
                    out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여                                                                                                                       //전송한다.
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream(), "UTF-8")); //데이터 수신시 stream을 받아들인다.


                } catch (IOException e) {
                    e.printStackTrace();
                }

//소켓에서 데이터를 읽어서 화면에 표시한다.
                try {
                    while (true) {
                        data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장
                        output.post(new Runnable() {
                            public void run() {
                                output.append(data + "\n"); //글자출력칸에 서버가 보낸 메시지를 받는다.
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();  //onResume()에서 실행.
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}