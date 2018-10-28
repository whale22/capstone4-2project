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
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class function2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function2);

        Button btn = (Button)findViewById(R.id.back);


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private void show(){
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
                builder.setContentTitle("기타(사용자 자유등록)");
                String str = edit.getText().toString();
                builder.setContentText(str);
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
    public void createNotification(View view){
        show();
    }

}
