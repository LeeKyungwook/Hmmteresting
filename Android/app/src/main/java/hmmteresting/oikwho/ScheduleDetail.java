package hmmteresting.oikwho;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        Intent intent = getIntent();

        final String title, User, startDate, startTime, endDate, endTime, prepare, isBroadcast;

        EditText this_edit_title = findViewById(R.id.edit_title);
        EditText this_edit_startDate = findViewById(R.id.edit_startDate);
        EditText this_edit_startTime = findViewById(R.id.edit_startTime);
        EditText this_edit_endDate = findViewById(R.id.edit_endDate);
        EditText this_edit_endTime = findViewById(R.id.edit_endTime);
        EditText this_edit_prepare = findViewById(R.id.edit_prepare);

        title = this_edit_title.getText().toString();
        User = intent.getStringExtra("userName");
        startDate = this_edit_startDate.getText().toString();
        startTime = this_edit_startTime.getText().toString();
        endDate = this_edit_endDate.getText().toString();
        endTime = this_edit_endTime.getText().toString();
        prepare = this_edit_prepare.getText().toString();

        //make setPrivacy function


        Button btn_submit = (Button)findViewById(R.id.btn_submit);
        Button.OnClickListener click_btn_submit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                json_addedScheduleDetail =
                        "{title : \"" + title + "\", User : \"" + User + "\", startDate : \"" + startDate +
                                "\", startTime : \"" + startTime + "\", endDate : \"" + endDate +
                                "\", endTime : \"" + endTime + "\" ,reqItems : \"" + prepare + "\" }";

                Toast.makeText(ScheduleDetail.this, json_addedScheduleDetail, Toast.LENGTH_LONG).show();

                //sendSch = new SendSchToServer();
                //sendSch.execute();

                Intent go_main = new Intent(
                        getApplicationContext(),
                        MainActivity.class
                );
                //send this to server.(server:OK I'll insert into my DB)
                startActivity(go_main);
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
