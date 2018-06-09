package hmmteresting.oikwho;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

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

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.Future;


public class SignUp extends AppCompatActivity {

    SendToServer sendInfo;
    isDoubleID isdoubleid;
    isDoubleName isdoublename;

    final String[] name = new String[1];
    final String[] id = new String[1];
    final String[] pwd = new String[1];
    final String[] family = new String[1];

    boolean isDouble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("회원가입");

        //permission확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) ||
                    (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)) {

                ActivityCompat.requestPermissions(SignUp.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        100);
            }

        }

        //회원정보 입력
        final EditText this_edit_name = findViewById(R.id.edit_name);
        final EditText this_edit_id = findViewById(R.id.edit_id);
        final EditText this_edit_pwd = findViewById(R.id.edit_pwd);
        final RadioGroup this_radio_family = findViewById(R.id.radio_family);

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        File sdCard = Environment.getExternalStorageDirectory();
        final String[] path = {sdCard.getAbsolutePath()+"/oikwho/"};

        Button check_doubleName = findViewById(R.id.btn_double_name);
        check_doubleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = this_edit_name.getText().toString();
                //이름 중복체크
                isdoublename = new isDoubleName();
                isdoublename.execute();

                isDouble = true;

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isDouble == true) {
                    Toast.makeText(SignUp.this, "새로운 회원입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp.this, "이미 가입된 회원입니다. 로그인해주세요.", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
            }
        });

        Button check_doubleID = findViewById(R.id.btn_double_id);
        check_doubleID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id[0] = this_edit_id.getText().toString();
                //아이디 중복체크
                isdoubleid = new isDoubleID();
                isdoubleid.execute();

                isDouble = true;
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isDouble == true) {
                    Toast.makeText(SignUp.this, "사용 가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp.this, "중복된 ID입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //카메라액티비티로 이동
        Button goPic = findViewById(R.id.btn_goCamera);
        goPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = this_edit_name.getText().toString();
                id[0] = this_edit_id.getText().toString();
                pwd[0] = this_edit_pwd.getText().toString();
                int radioID = this_radio_family.getCheckedRadioButtonId();
                family[0] = String.valueOf(radioID);

                path[0] = path[0] + name[0] +".jpg";
Log.d("사진 path",path[0]);
                sendInfo = new SendToServer();
                sendInfo.execute();

                Intent go_camera = new Intent(
                        getApplicationContext(),
                        TakePhoto.class
                );
                go_camera.putExtra("username", name[0]);
                startActivity(go_camera);
            }
        });

        Button do_signUp_send = findViewById(R.id.btn_signUp_send);
        do_signUp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File picture = new File(path[0]);

                Future uploading = Ion.with(SignUp.this)
                        .load("http://112.151.162.170:7000/joinPicture")
                        .setMultipartFile("image", picture)
                        .asString()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                try {
                                    JSONObject jobj = new JSONObject(result.getResult());
                                    Toast.makeText(getApplicationContext(),
                                            jobj.getString("response"), Toast.LENGTH_SHORT);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                Toast.makeText(SignUp.this, "send success",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    class SendToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/joinInfo");

            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try {
                //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
                nameValues.add(new BasicNameValuePair(
                        "name", URLDecoder.decode(name[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair(
                        "id", URLDecoder.decode(id[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair(
                        "pw", URLDecoder.decode(pwd[0], "UTF-8")));
                nameValues.add(new BasicNameValuePair(
                        "family", URLDecoder.decode(family[0], "UTF-8")));

Log.d("내가뭘보내냐",name[0] + ", " + id[0] +", "+ pwd[0] + "를 보냇다 순서대로 이름 아이디 비번임");
                //HttpPost에 넘길 값을들 Set해주기
                post.setEntity(
                        new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Log.e("Insert Log", ex.toString());
            }

            try {
                //설정한 URL을 실행시키기
                HttpResponse response = client.execute(post);
                //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
                Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

                //중복이면
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
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

    class isDoubleID extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/joinCheckID");

            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try {
                //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
                nameValues.add(new BasicNameValuePair(
                        "id", URLDecoder.decode(id[0], "UTF-8")));

                Log.d("내가뭘보내냐", id[0] + "가 중복ID인지 물어봤다");
                //HttpPost에 넘길 값을들 Set해주기
                post.setEntity(
                        new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Log.e("Insert Log", ex.toString());
            }

            try {
                //설정한 URL을 실행시키기
                HttpResponse response = client.execute(post);
                //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
                Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

                String str_response = new String();
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null) {
                    str_response = EntityUtils.toString(responseEntity);
                }
                if(str_response.equals("true")) {   isDouble = true;    }
                else {  isDouble = false; }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
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


    class isDoubleName extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/joinCheckName");

            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try {
                //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
                nameValues.add(new BasicNameValuePair(
                        "name", URLDecoder.decode(name[0], "UTF-8")));

                Log.d("내가뭘보내냐", id[0] + "가 중복이름인지 물어봤다");
                //HttpPost에 넘길 값을들 Set해주기
                post.setEntity(
                        new UrlEncodedFormEntity(nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Log.e("Insert Log", ex.toString());
            }

            try {
                //설정한 URL을 실행시키기
                HttpResponse response = client.execute(post);
                //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
                Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

                String str_response = new String();
                HttpEntity responseEntity = response.getEntity();
                if(responseEntity != null) {
                    str_response = EntityUtils.toString(responseEntity);
                }
                if(str_response.equals("true")) {   isDouble = true;    }
                else {  isDouble = false; }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
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
