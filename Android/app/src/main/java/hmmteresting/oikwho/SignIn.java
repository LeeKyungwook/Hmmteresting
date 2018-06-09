package hmmteresting.oikwho;

import android.app.ActionBar;
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

public class SignIn extends AppCompatActivity {

    String[] input_id = new String[1];
    String[] input_pw = new String[1];
    String[] name = new String[1];

    boolean shouldGoCalendar;

    SendReqToServer sendLogInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("로그인");

        final EditText this_userId = findViewById(R.id.edit_userid);
        final EditText this_userPw = findViewById(R.id.edit_userpw);
        Button this_btn_login = findViewById(R.id.btn_login);
        shouldGoCalendar = false;

        //id를 디비에 주면 이름을 반환받을거임
        //그럼 그 이름이 여러 액티비티를 왔다갔다해야함
        this_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_id[0] = this_userId.getText().toString();
                input_pw[0] = this_userPw.getText().toString();

                sendLogInRequest = new SendReqToServer();
                sendLogInRequest.execute();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent go_main = new Intent(getApplicationContext(),
                        MainActivity.class);
                if(shouldGoCalendar == true) {
                    Toast.makeText(SignIn.this, "어서오세요, " + name[0] + "님!", Toast.LENGTH_SHORT).show();
                    go_main.putExtra("userName",name[0]);
                    startActivity(go_main);
                } else {
                    Toast.makeText(SignIn.this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
}
        });
    }

    class SendReqToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/login");
            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();
            try{  ////////request schedules for selected day
                nameValues.add(new BasicNameValuePair("id", URLDecoder.decode(input_id[0],"UTF-8")));
                nameValues.add(new BasicNameValuePair("pw",URLDecoder.decode(input_pw[0],"UTF-8")));
                //Log.d("뭘 보내냐",  input_id[0]+"가 아이디고 "+ input_pw[0]+ "가 패스워드 맞냐고 물어봄");
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
                if(str_response.equals("null")) {
                    shouldGoCalendar = false;
                } else {
                    shouldGoCalendar = true;
                    name[0] = str_response;
                }
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
