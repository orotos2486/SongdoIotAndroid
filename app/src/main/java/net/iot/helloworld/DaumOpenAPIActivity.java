package net.iot.helloworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static net.iot.helloworld.R.id.listview;

public class DaumOpenAPIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_open_api);
    }
    public void sendRequest(View view){
        EditText keywordText = (EditText)findViewById(R.id.keyword);
        new LoadDaumBookInfo().execute(keywordText.getText().toString());
    }
    class LoadDaumBookInfo extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(DaumOpenAPIActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuffer response = new StringBuffer();
            try {
                String text = URLEncoder.encode(params[0], "UTF-8");
                String apiURL = "https://apis.daum.net/search/book?apikey=" + "85ac72bf4d97eef7a3de4fafb5407f5c&q="
                        + text + "&output=json"; // json 결과
                //https://apis.daum.net/search/book?apikey=85ac72bf4d97eef7a3de4fafb5407f5c&q=다음카카오&output=json
                //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            return response.toString();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("다음 서적 검색 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            //Toast.makeText(DaumOpenAPIActivity.this,s,Toast.LENGTH_LONG).show();
            //파싱 - 데이터 구조가 다르기때문에 제각각의 데이터 구조와 맞게 바꿔야 한다.
            try {
                JSONObject json = new JSONObject(s);
                JSONArray item = json.getJSONObject("channel").getJSONArray("item");
                //다음채널은 첫번째가 채널키가 있기떄문에 이렇게 해야한다.
                for (int i = 0; i < item.length(); i++) {
                    JSONObject obj = item.getJSONObject(i);
                    String author = obj.getString("author");
                    Log.i("author",author);
                    String category = obj.getString("category");
                    String cover_s_url = obj.getString("cover_s_url");
                    String description = obj.getString("description");
                    String link = obj.getString("link");
                    String list_price = obj.getString("list_price");
                    String pub_date = obj.getString("pub_date");
                    String sale_price = obj.getString("sale_price");
                    String title = obj.getString("title");
                    itemList.add(new Item(author,category,cover_s_url,description,link,list_price,pub_date,sale_price,title));
                }
                BookAdapter adapter = new BookAdapter(DaumOpenAPIActivity.this);
                ListView listView = (ListView)findViewById(R.id.listview);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        class Item {
            String author, category, cover_s_url, description, link, list_price, pub_date, sale_price, title;

            Item(String author, String category, String cover_s_url, String description, String link, String list_price,
                 String pub_date, String sale_price, String title) {
                this.author = author;
                this.category = category;
                this.cover_s_url = cover_s_url;
                this.description = description;
                this.link = link;
                this.list_price = list_price;
                this.pub_date = pub_date;
                this.sale_price = sale_price;
                this.title = title;
            }


        }
        ArrayList<Item> itemList = new ArrayList<Item>();
        class BookAdapter extends ArrayAdapter{
            public BookAdapter(Context context) {
                super(context, R.layout.list_book_item,itemList);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.list_blog_item,null);
                }else view = convertView;
                TextView titleText = (TextView)view.findViewById(R.id.title);
                TextView authorText = (TextView)view.findViewById(R.id.author);
                TextView categoryText = (TextView)view.findViewById(R.id.category);
                TextView descriptionText = (TextView)view.findViewById(R.id.description);
                TextView pubDateText = (TextView)view.findViewById(R.id.pub_date);
                TextView listPriceText = (TextView)view.findViewById(R.id.list_price);
                ImageView coverSUrlText = (ImageView)view.findViewById(R.id.cover_s_url);
                try {
                    titleText.setText(itemList.get(position).title);
                    authorText.setText(itemList.get(position).author);
                    categoryText.setText(itemList.get(position).category);
                    descriptionText.setText(itemList.get(position).description);
                    pubDateText.setText(itemList.get(position).pub_date);
                    listPriceText.setText(itemList.get(position).list_price + "(" + itemList.get(position).sale_price + ")");
                }catch (Exception e) {
                    e.printStackTrace();
                }

                return view;
            }
        }
    }
    }

