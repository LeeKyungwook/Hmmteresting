package hmmteresting.oikwho;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ScheduleUpdate extends AppCompatActivity {

    SendSchToServer sendSch;

    final String[] title = new String[1];
    final String[] User = new String[1];
    final String[] startDate = new String[1];
    final String[] startTime = new String[1];
    final String[] endDate = new String[1];
    final String[] endTime = new String[1];
    final String[] reqitems = new String[1];
    final String[] isBroadcast = new String[1];

    final String[] selected__id = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_update);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("일정 정보 변경");

        final Intent intent = getIntent();
        User[0] = intent.getStringExtra("User");

        final EditText this_edit_title = (EditText) findViewById(R.id.edit_title);
        final EditText this_edit_startDate = (EditText) findViewById(R.id.edit_startDate);
        final EditText this_edit_startTime = (EditText) findViewById(R.id.edit_startTime);
        final EditText this_edit_endDate = (EditText) findViewById(R.id.edit_endDate);
        final EditText this_edit_endTime = (EditText) findViewById(R.id.edit_endTime);
        final EditText this_edit_reqitems = (EditText) findViewById(R.id.edit_reqitems);
        final RadioGroup this_rg_privacy = (RadioGroup) findViewById(R.id.radio_privacy);

        selected__id[0] = intent.getStringExtra("_id");
        String selected_title = intent.getStringExtra("title");
        String selected_startDate = intent.getStringExtra("startDate");
        String selected_startTime = intent.getStringExtra("startTime");
        String selected_endDate = intent.getStringExtra("endDate");
        String selected_endTime = intent.getStringExtra("endTime");
        String selected_reqItems = intent.getStringExtra("reqItems");
        String selected_isBroadcast = intent.getStringExtra("isBroadcast");

        this_edit_title.setText(selected_title);
        this_edit_startDate.setText(selected_startDate);
        this_edit_startTime.setText(selected_startTime);
        this_edit_endDate.setText(selected_endDate);
        this_edit_endTime.setText(selected_endTime);
        this_edit_reqitems.setText(selected_reqItems);
        this_rg_privacy.check(Integer.parseInt(selected_isBroadcast));

        Button btn_submit = (Button)findViewById(R.id.btn_submit);
        Button.OnClickListener click_btn_submit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                title[0] = this_edit_title.getText().toString();
                startDate[0] = this_edit_startDate.getText().toString();
                startTime[0] = this_edit_startTime.getText().toString();
                endDate[0] = this_edit_endDate.getText().toString();
                endTime[0] = this_edit_endTime.getText().toString();
                reqitems[0] = this_edit_reqitems.getText().toString();
                isBroadcast[0] = Integer.toString(this_rg_privacy.getCheckedRadioButtonId());

                sendSch = new SendSchToServer();
                sendSch.execute();

                finish();
            }
        };
        btn_submit.setOnClickListener(click_btn_submit);
    }

    class SendSchToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/updateSchedule");
            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try{
                //must set values that passed by JSON rules
                nameValues.add(new BasicNameValuePair("_id",URLDecoder.decode(selected__id[0],"UTF-8")));
                nameValues.add(new BasicNameValuePair("title", URLDecoder.decode(title[0],"UTF-8")));
                nameValues.add(new BasicNameValuePair("User",URLDecoder.decode(User[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair("startDate",URLDecoder.decode(startDate[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair("startTime",URLDecoder.decode(startTime[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair("endDate",URLDecoder.decode(endDate[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair("endTime",URLDecoder.decode(endTime[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair("reqItems",URLDecoder.decode(reqitems[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair("isBroadcast",URLDecoder.decode(isBroadcast[0], "UTF-8")));

                //setting values to HttpPost
                post.setEntity(new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Insert Log", e.toString());
            }

            try{
                //execute pre-setted URL
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
