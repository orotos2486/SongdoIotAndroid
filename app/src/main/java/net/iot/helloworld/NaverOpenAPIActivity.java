package net.iot.helloworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

// 안드로이드는 네트워크나 파일입출력은 어느정도 시간이 걸릴지 모를경우 쓰레드를 사용해야한다
// 안드로이는 어싱크테스크를 이용하면 통신을 하기전에 수행해야하는것과 한후에 수행해야하는것을 쉽게 만들수있다.
//
public class NaverOpenAPIActivity extends AppCompatActivity {
    class item{
        String title;String link;String description;String bloggername,bloggerlink,postdate;
        item(String title,String link, String description,String bloggername,String bloggerlink,String postdate){
            this.title=title; this.link=link;this.description=description;this.bloggername=bloggername;
            this.bloggerlink=bloggerlink;this.postdate=postdate;
        }
    }
    ArrayList<item> itemList = new ArrayList<item>();
    class BlogAdapter extends ArrayAdapter{

        public BlogAdapter(Context context) {
            super(context, R.layout.list_blog_item,itemList);
        }//내가 리스트뷰에 출력할 뷰를 저장하기위해 뷰를 어댑터에 입력


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //convertView는 기존에 컨버트된 데이터가 저장되어있다 만약되어있지않을경우 null
            View view =null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_blog_item,null);
            }else{
                view = convertView;
            }
            TextView titleText= (TextView)view.findViewById(R.id.title);
            TextView postdateText = (TextView)view.findViewById(R.id.postdate);
            TextView descriptionText= (TextView)view.findViewById(R.id.description);
            TextView bloggernameText = (TextView)view.findViewById(R.id.bloggername);
            titleText.setText(Html.fromHtml(itemList.get(position).title));
            postdateText.setText(itemList.get(position).postdate);
            descriptionText.setText(Html.fromHtml(itemList.get(position).description));
            bloggernameText.setText(itemList.get(position).bloggername);
            final int pos= position;
            titleText.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(itemList.get(pos).link));
                    startActivity(intent);
                }
            });
            bloggernameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(itemList.get(pos).bloggerlink));
                    startActivity(intent);
                }
            });
            return view;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_open_api);
    }
    public void sendRequest(View view){
        EditText keywordText = (EditText)findViewById(R.id.keyword);
        new LoadNaverBloginfo().execute(keywordText.getText().toString());
    }

    class LoadNaverBloginfo extends AsyncTask<String,String,String>{
        ProgressDialog dialog = new ProgressDialog(NaverOpenAPIActivity.this);
        @Override
        protected String doInBackground(String... params) {//2 연결중
            String clientId = "IZBLgiGYkE_LEIwXz124";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "emfUzEx0Bw";//애플리케이션 클라이언트 시크릿값";
            StringBuffer response = new StringBuffer();
            try {
                String text = URLEncoder.encode(params[0], "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/blog.json?query="+ text; // json 결과
                //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPreExecute() {//1 인터넷연결전
            dialog.setMessage("네이버 블로그 정보 로딩 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {//3 연결후
            dialog.dismiss();
            //Toast.makeText(NaverOpenAPIActivity.this,s,Toast.LENGTH_LONG).show();

            try{
                //JSON 문자열 -> JSON 객체로 변환
            JSONObject json = new JSONObject(s);
                //JSON 객체에서 items 키값의 배열을 추출 ->여기에만 한정
                JSONArray items = json.getJSONArray("items");
                itemList.clear();//동적배열 초기화
                for(int i=0;i< items.length();i++) {//items 배열안의 객체 정보 개별 추출
                    JSONObject obj = items.getJSONObject(i);
                    String title =obj.getString("title");
                    String link = obj.getString("link");
                    String description = obj.getString("description");
                    String bloggername = obj.getString("bloggername");
                    String postdate = obj.getString("postdate");
                    String bloggerlink = obj.getString("bloggerlink");
                    itemList.add(new item(title,link,description,bloggername,bloggerlink,postdate));//타이틀지번,링크지번,블로거네임,
                }
                BlogAdapter adapter = new BlogAdapter(NaverOpenAPIActivity.this);
                        ListView listView = (ListView)findViewById(listview);
                        listView.setAdapter(adapter);
                        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemList.get(position).link));
                                startActivity(intent);
                            }
                        });*/
               // Toast.makeText(NaverOpenAPIActivity.this,items.length()+" ",Toast.LENGTH_LONG).show();
        }catch(Exception e){
                e.printStackTrace();
            }
    }
}
}
