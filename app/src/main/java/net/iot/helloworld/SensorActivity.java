package net.iot.helloworld;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    SensorManager manager = null;
    List<Sensor> sensors = null;
    SensorListAdapter adapter = null;
    class SensorListAdapter  extends ArrayAdapter{
        public SensorListAdapter(Context context) {
            super(context, R.layout.list_blog_item,sensors);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_blog_item,null);
            }
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView descriptionView = (TextView)view.findViewById(R.id.description);
            TextView bloggernameText = (TextView)view.findViewById(R.id.bloggername);
            titleView.setText("센서 : " +sensors.get(position).getName());
            descriptionView.setText("제조사 : "+sensors.get(position).getVendor());
            bloggernameText.setText("버전 : " + sensors.get(position).getVersion());
            return view;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensors = manager.getSensorList(Sensor.TYPE_ALL);
        adapter = new SensorListAdapter(this);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}
