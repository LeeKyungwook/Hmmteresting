package hmmteresting.oikwho;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ScheduleList extends AppCompatActivity {

    private ArrayList<CustomListAdapter.ScheduleDataList> Schedule_Array_Data;
    private CustomListAdapter.ScheduleDataList Schedule_Data;
    private CustomListAdapter Schedule_Data_Adapter;
    int this_date = 0;
    SendSchToServer requestToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        Intent intent = getIntent();
        int getDate = intent.getIntExtra("selectday",-1);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        Integer month = getDate/100;
        month %= 100;
        Integer day = getDate % 100;

        ActionBar ab = getSupportActionBar();
        ab.setTitle(month+"월"+day+"일의 일정");

        this_date = (year*10000) + (month*100) + day;

        //서버에서 스케줄 정보 받아오기
        requestToServer.execute();

        //받아온 스케줄정보 표시하기
        //http://necos.tistory.com/entry/Android-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EB%B7%B0%EB%A5%BC-%EB%82%B4-%EB%A7%98%EB%8C%80%EB%A1%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EC%9E%90%EC%BB%A4%EC%8A%A4%ED%85%80-%EB%A6%AC%EC%8A%A4%ED%8A%B8%EB%B7%B0

        ListView scheduleList = (ListView)findViewById(R.id.lv_scheduleList);
        Schedule_Array_Data = new ArrayList<CustomListAdapter.ScheduleDataList>();

        Schedule_Data = new CustomListAdapter.ScheduleDataList("1번째",
                "5월 28일 4시부터");
        Schedule_Array_Data.add(Schedule_Data);
        Schedule_Data = new CustomListAdapter.ScheduleDataList("2번째",
                "5월29일 13시부터");
        Schedule_Array_Data.add(Schedule_Data);

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

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/showschedule");
            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try{  ////////request schedules for selected day
                nameValues.add(new BasicNameValuePair("date", URLDecoder.decode(str_thisdate,"UTF-8")));

                //setting values to HttpPost
                post.setEntity(new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Insert Log", e.toString());
            }

            try{ ////////get schedules of selected day
                HttpResponse response = client.execute(post);
                //make log. if you got 200, it worked well.
                Log.d("답변은이렇게생김",response.toString());

                Log.i("Insert Log", "response.getStatusCode:"+response.getStatusLine().getStatusCode());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
