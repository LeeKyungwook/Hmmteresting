package hmmteresting.oikwho;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.koushikdutta.async.parser.JSONObjectParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ScheduleList extends AppCompatActivity {

    private ArrayList<CustomListAdapter.ScheduleDataList> Schedule_Array_Data;
    private CustomListAdapter.ScheduleDataList Schedule_Data;
    private CustomListAdapter Schedule_Data_Adapter;
    int this_date = 0;
    String userName;
    SendSchToServer requestToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        int getDate = intent.getIntExtra("selectday",-1);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        Integer month = getDate/100;
        month %= 100;
        Integer day = getDate % 100;

        ActionBar ab = getSupportActionBar();
        ab.setTitle(month+"월"+day+"일의 일정");

        this_date = getDate;

        //서버에서 스케줄 정보 받아오기
        requestToServer = new SendSchToServer();
        requestToServer.execute();

        //받아온 스케줄정보 표시하기
        //http://necos.tistory.com/entry/Android-안드로이드-리스트-뷰를-내-맘대로-사용하자커스텀-리스트뷰

        Schedule_Array_Data = new ArrayList<CustomListAdapter.ScheduleDataList>();

        Schedule_Data = new CustomListAdapter.ScheduleDataList("1번째",
                "부제목 1");
        Schedule_Array_Data.add(Schedule_Data);
        Schedule_Data = new CustomListAdapter.ScheduleDataList("2번째",
                "부제목2");
        Schedule_Array_Data.add(Schedule_Data);

        ListView scheduleList = (ListView)findViewById(R.id.lv_scheduleList);
        Schedule_Data_Adapter = new CustomListAdapter(this,
                android.R.layout.simple_list_item_1, Schedule_Array_Data);
        scheduleList.setAdapter(Schedule_Data_Adapter);


    }

    class SendSchToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String str_thisdate;
            str_thisdate = Integer.toString(this_date);
            Log.d("이 날짜의 일정을 줘라", str_thisdate);
            Log.d("이 사람의 일정을 줘라",userName);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7777/showschedule");
            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try{  ////////request schedules for selected day
                nameValues.add(new BasicNameValuePair("thisDate", URLDecoder.decode(str_thisdate,"UTF-8")));
                nameValues.add(new BasicNameValuePair("userName",URLDecoder.decode(userName, "UTF-8")));

                Log.d("뭘 보내냐","thisDate는 " + str_thisdate + "고, 이름은 " + userName + "이 간다");
                //setting values to HttpPost
                post.setEntity(new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Insert Log", e.toString());
            }

            try{ ////////get schedules of selected day
                HttpResponse response = client.execute(post);
                //make log. if you got 200, it worked well.
                Log.i("Insert Log", "response.getStatusCode:"+response.getStatusLine().getStatusCode());

                String str_response = new String();
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null) {
                    str_response = EntityUtils.toString(responseEntity);
                }
                //문자열로 받아오는것가진 성공했음 이걸 어케 json Obj로 바꿔주냐..
                JSONObject json_response = new JSONObject(str_response);
                JSONArray json_parsed_response = json_response.getJSONArray("schedules");  //이거 아니면 SCHEDULES일듯..

                for(int i = 0; i < json_parsed_response.length(); i ++) {
                    String _id = json_parsed_response.getJSONObject(i).getString("_id");
                    String title = json_parsed_response.getJSONObject(i).getString("title");
                    String User = json_parsed_response.getJSONObject(i).getString("User");//Uid가 온다
                    String startDate = json_parsed_response.getJSONObject(i).getString("startDate");
                    String startTime = json_parsed_response.getJSONObject(i).getString("startTime");
                    String endDate = json_parsed_response.getJSONObject(i).getString("endDate");
                    String endTime = json_parsed_response.getJSONObject(i).getString("endTime");
                    //String reqItems = json_parsed_response.getJSONObject(i).getString("");
                    String isBroadcast = json_parsed_response.getJSONObject(i).getString("isBroadcast");

                    //CustomList 거기다가 넣어조야한다
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
