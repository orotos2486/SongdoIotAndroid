package net.iot.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Intent intent = getIntent();
        String edittext  = intent.getStringExtra("edittext");
        EditText editText = (EditText)findViewById(R.id.edittext);
        editText.setText(edittext);
    }

    public void startThirdactivity(View view) {
        Intent intent = new Intent(SubActivity.this, ThirdActivity.class);
        startActivity(intent);


    }

    public void startCallactivity(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01012345678"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void startWebactivity(View view){
        Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(intent);
    }
    public void finishActivity(View view){
        EditText edittext = (EditText)findViewById(R.id.edittext);
        Intent intent = new Intent();
        intent.putExtra("edittext",edittext.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
