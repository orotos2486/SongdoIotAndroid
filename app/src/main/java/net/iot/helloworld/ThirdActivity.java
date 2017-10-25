package net.iot.helloworld;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }
    public void clickButton1(View view){
        Toast.makeText(ThirdActivity.this,"이것은 토스트 메세지입니다.", Toast.LENGTH_SHORT).show();
    }
    public void clickButton2(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ThirdActivity.this);
        dialog.setTitle("알림");
        dialog.setMessage("이것은 얼럿다이어로그입나다.");
        dialog.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
