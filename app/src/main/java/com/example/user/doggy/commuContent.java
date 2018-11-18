package com.example.user.doggy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class commuContent extends AppCompatActivity {
    static String titleCon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_content);
        TextView title = (TextView) findViewById(R.id.commutitle);
        TextView content = (TextView) findViewById(R.id.commucontent);
        TextView comment = (TextView) findViewById(R.id.commuComment);
        EditText commentWrite = (EditText) findViewById(R.id.commuCommentWrite);
        title.setText(titleCon);
        content.setText("부산시 내 대형견과 함께하고 있는 분들을 찾습니다! \n교육정보나 힘든 점, 산책시 주의할 것들을 이야기하고 싶어요!");
        comment.setText("temp : 저는 골든 리트리버를 기르고 있어요! 부산은 아니지만!");
    }
}
