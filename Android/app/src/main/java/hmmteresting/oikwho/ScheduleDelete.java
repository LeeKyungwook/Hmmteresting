package hmmteresting.oikwho;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

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

public class ScheduleDelete extends AppCompatActivity {

    int this_date = 0;
    String userName;
    int selectedItem;

    final String[] _id = new String[20];
    final String[] title = new String[20];
    final String[] User = new String[20];
    final String[] startDate = new String[20];
    final String[] startTime = new String[20];
    final String[] endDate = new String[20];
    final String[] endTime = new String[20];
    final String[] reqItems = new String[20];
    final String[] isBroadcast = new String[20];

    SendRequestToServer requestToServer;
    SendDelToServer deleteSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_delete);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        int getDate = intent.getIntExtra("selectday",-1);

        Integer month = getDate/100;
        month %= 100;
        Integer day = getDate % 100;

        ActionBar ab = getSupportActionBar();
        ab.setTitle(month+"월 "+day+"일의 일정");

        //받아온 스케줄정보 표시하기
        //http://necos.tistory.com/entry/Android-안드로이드-리스트-뷰를-내-맘대로-사용하자커스텀-리스트뷰
        ArrayList<CustomListAdapter.ScheduleDataList> schedule_Array_Data = new ArrayList<CustomListAdapter.ScheduleDataList>();

        this_date = getDate;
        requestToServer = new SendRequestToServer();
        requestToServer.execute();
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("다 받아오고나서 제목의 상태","이것은 첫번째 제목이다 = "+title[0]);

        if(title[0] == null) {
            Toast.makeText(ScheduleDelete.this, month + "월" + day + "일의 스케줄이 없습니다.", Toast.LENGTH_LONG).show();
        } else {
            for(int i = 0; _id.length > i; i++) {
                if(_id[i] != null) {
                    CustomListAdapter.ScheduleDataList schedule_Data =
                            new CustomListAdapter.ScheduleDataList(title[i], "시작 날짜 : " + startDate[i]);
                    schedule_Array_Data.add(schedule_Data);
                }
            }
        }

        ListView scheduleList = (ListView)findViewById(R.id.lv_scheduleList);
        CustomListAdapter schedule_Data_Adapter = new CustomListAdapter(this,
                android.R.layout.simple_list_item_1, schedule_Array_Data);

        scheduleList.setAdapter(schedule_Data_Adapter);

        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedItem = position;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleDelete.this);

                alertDialogBuilder.setTitle("스케줄 삭제");
                alertDialogBuilder
                        .setMessage(title[position] + " 스케줄을 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteSchedule = new SendDelToServer();
                                        deleteSchedule.execute();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });

    }

    class SendRequestToServer extends AsyncTask<Void, Void, Void> {

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
                nameValues.add(new BasicNameValuePair("thisDate", URLDecoder.decode(str_thisdate,"UTF-8")));
                nameValues.add(new BasicNameValuePair("name",URLDecoder.decode(userName, "UTF-8")));

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

Log.i("뭐 받앗냐",str_response);
                JSONObject json_response = new JSONObject(str_response);
                JSONArray json_parsed_response = json_response.getJSONArray("schedules");
                int jsonArraySize = json_parsed_response.length();
                for(int i = 0; i < jsonArraySize; i ++) {

                    JSONObject json_parsed_one = json_parsed_response.getJSONObject(i);
                    _id[i] = json_parsed_one.get("_id").toString();
                    title[i] = json_parsed_one.get("title").toString();
                    User[i] = json_parsed_one.get("User").toString();
                    startDate[i] = json_parsed_one.get("startDate").toString();
                    startTime[i] = json_parsed_one.get("startTime").toString();
                    endDate[i] = json_parsed_one.get("endDate").toString();
                    endTime[i] = json_parsed_one.get("endTime").toString();
                    reqItems[i] = json_parsed_one.getString("reqItems");
                    isBroadcast[i] = json_parsed_one.get("isBroadcast").toString();

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

    class SendDelToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/deleteSchedule");
            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();
            try{  ////////request schedules for selected day
                nameValues.add(new BasicNameValuePair("_id", URLDecoder.decode(_id[selectedItem],"UTF-8")));
Log.d("뭘 보내냐", title[selectedItem] + " 스케줄을 없애달라고함");
                //setting values to HttpPost
                post.setEntity(new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Insert Log", e.toString());
            }

            try{ ////////get schedules of selected day
                HttpResponse response = client.execute(post);
                //make log. if you got 200, it worked well.
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
