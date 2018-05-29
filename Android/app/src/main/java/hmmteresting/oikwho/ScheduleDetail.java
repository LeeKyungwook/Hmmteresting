package hmmteresting.oikwho;

import android.content.Intent;
import android.os.AsyncTask;
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
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ScheduleDetail extends AppCompatActivity {

    String json_addedScheduleDetail;
    SendSchToServer sendSch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        final Intent intent = getIntent();

        final String[] title = new String[1];
        final String[] User = new String[1];
        final String[] startDate = new String[1];
        final String[] startTime = new String[1];
        final String[] endDate = new String[1];
        final String[] endTime = new String[1];
        final String[] prepare = new String[1];
        final int[] isBroadcast = new int[1];

        final EditText this_edit_title = (EditText) findViewById(R.id.edit_title);
        final EditText this_edit_startDate = (EditText) findViewById(R.id.edit_startDate);
        final EditText this_edit_startTime = (EditText) findViewById(R.id.edit_startTime);
        final EditText this_edit_endDate = (EditText) findViewById(R.id.edit_endDate);
        final EditText this_edit_endTime = (EditText) findViewById(R.id.edit_endTime);
        final EditText this_edit_prepare = (EditText) findViewById(R.id.edit_prepare);
        final RadioGroup this_rg_privacy = (RadioGroup) findViewById(R.id.radio_privacy);


        //make setPrivacy function with radiobutton


        Button btn_submit = (Button)findViewById(R.id.btn_submit);
        Button.OnClickListener click_btn_submit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                title[0] = this_edit_title.getText().toString();
                User[0] = intent.getStringExtra("userName");
                startDate[0] = this_edit_startDate.getText().toString();
                startTime[0] = this_edit_startTime.getText().toString();
                endDate[0] = this_edit_endDate.getText().toString();
                endTime[0] = this_edit_endTime.getText().toString();
                prepare[0] = this_edit_prepare.getText().toString();

                isBroadcast[0] = this_rg_privacy.getCheckedRadioButtonId();


                json_addedScheduleDetail =
                        "{title : \"" + title[0] + "\", User : \"" + User[0] + "\", startDate : \"" + startDate[0] +
                                "\", startTime : \"" + startTime[0] + "\", endDate : \"" + endDate[0] +
                                "\", endTime : \"" + endTime[0] + "\" ,reqItems : \"" + prepare[0] + "\" }";


                Toast.makeText(ScheduleDetail.this, json_addedScheduleDetail, Toast.LENGTH_LONG).show();

                //sendSch = new SendSchToServer();
                //sendSch.execute();

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
            HttpPost post = new HttpPost("http://112.151.162.170:7000");
            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try{
                //must set values that passed by POST rules
                nameValues.add(new BasicNameValuePair("command", URLDecoder.decode(json_addedScheduleDetail,"UTF-8")));

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
