package net.iot.helloworld;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){

        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1 : if(resultCode == RESULT_OK){
                String result = data.getStringExtra("edittext");
                EditText edittext = (EditText)findViewById(R.id.edittext);
                edittext.setText(result);
            }
        }
    }

    public void startSubactivity(View view){
        EditText editText = (EditText)findViewById(R.id.edittext);
        Intent intent = new Intent(MainActivity.this,SubActivity.class);
        intent.putExtra("edittext",editText.getText().toString());
        startActivityForResult(intent,1);
    }
    public void goToList(View view){
        Intent intent = new Intent(MainActivity.this,ListViewActivity.class);
        startActivity(intent);
    }
    public void goToScrollView(View view){
        Intent intent = new Intent(MainActivity.this,ScrollViewActivity.class);
        startActivity(intent);
    }
    public void startNaverOpenAPIActivity(View view){
        Intent intent = new Intent(MainActivity.this,NaverOpenAPIActivity.class);
        startActivity(intent);
    }

    public void startDaumOpenAPIActivity(View view){
        Intent intent = new Intent(MainActivity.this,DaumOpenAPIActivity.class);
        startActivity(intent);
    }

}
