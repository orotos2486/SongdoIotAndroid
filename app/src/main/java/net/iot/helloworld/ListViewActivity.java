package net.iot.helloworld;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    class Item {
        int image; String title; int price;
        Item(int image, String title, int price) {
            this.image = image; this.title = title;
            this.price = price;
        }
    }
    ArrayList<Item> itemList = new ArrayList<Item>();
    class MyAdapter extends ArrayAdapter {
        public MyAdapter( Context context) {
            super(context, R.layout.list_item, itemList);
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, null);
            } else {
                view = convertView;
            }
            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView priceView = (TextView)view.findViewById(R.id.price);
            imageView.setImageResource(itemList.get(position).image);
            titleView.setText(itemList.get(position).title);
            priceView.setText(itemList.get(position).price+"");
            return view;
        }
    }
    public void makeCustomListView() {
        itemList.add(
                new Item(R.drawable.cat1,"cat1",50000));
        itemList.add(
                new Item(R.drawable.cat2,"cat2",40000));
        itemList.add(
                new Item(R.drawable.cat3,"cat3",30000));
        MyAdapter myAdapter = new MyAdapter(ListViewActivity.this);
        ListView listView2 = (ListView)findViewById(R.id.listview2);
        listView2.setAdapter(myAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        String items[] = {
                "Hong", "Kim", "Park", "Jeong",
                "Lee", "Song", "So", "Seung",
                "Han", "Ha"
        };
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        ListViewActivity.this,
                        android.R.layout.simple_list_item_1,
                        items);
        ListView listView =
                (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);

        makeCustomListView();
    }
}