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
import android.widget.Toast;

import java.io.File;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.Future;


public class SignUp extends AppCompatActivity {

    String userinfo;
    SendToServer sendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        final EditText this_edit_family = findViewById(R.id.edit_family);

        final String[] name = new String[1];
        final String[] id = new String[1];
        final String[] pwd = new String[1];
        final String[] familyCode = new String[1];
        final String[] face = new String[1];

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        File sdCard = Environment.getExternalStorageDirectory();
        final String[] path = {sdCard.getAbsolutePath()+"/oikwho/"};

        //카메라액티비티로 이동
        Button goPic = findViewById(R.id.btn_goCamera);

        goPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = this_edit_name.getText().toString();

                Intent go_camera = new Intent(
                        getApplicationContext(),
                        TakePhoto.class
                );
                go_camera.putExtra("username", name[0]);
                startActivity(go_camera);
            }
        });

        //Button submit = findViewById(R.id.btn_signUp_submit);
        Button picture = findViewById(R.id.btn_signUp_sendpict);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                path[0] = path[0] + name[0] + ".jpg";
                File picture = new File(path[0]);

                Future uploading = Ion.with(SignUp.this)
                        .load("http://112.151.162.170:7000/join")
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
            }
        });

/*
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = this_edit_name.getText().toString();
                id[0] = this_edit_id.getText().toString();
                pwd[0] = this_edit_pwd.getText().toString();
                path[0] = path[0] + "/oikwho/" + name[0] +".jpg";

                userinfo = "{ \"name\": \""+ name[0] +"\" , \"id\": \""
                        + id[0] +"\" , \"password\" : \" "+ pwd[0] + "\" }";

                sendInfo = new SendToServer();
                sendInfo.execute();


                File picture = new File(path[0]);

                Future uploading = Ion.with(SignUp.this)
                        .load("http://112.151.162.170:7000/join")
                        .setMultipartFile("image",picture)
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
                        } );
                Log.d("회원가입","남수현.jpg를 전송했다.");
            }
        });*/
    }

    class SendToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://112.151.162.170:7000/join");

            ArrayList<NameValuePair> nameValues =
                    new ArrayList<NameValuePair>();

            try {
                //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
                nameValues.add(new BasicNameValuePair(
                        "command", URLDecoder.decode(userinfo, "UTF-8")));

                //HttpPost에 넘길 값을들 Set해주기
                post.setEntity(
                        new UrlEncodedFormEntity(
                                nameValues, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Log.e("Insert Log", ex.toString());
            }

            try {
                //설정한 URL을 실행시키기
                HttpResponse response = client.execute(post);
                //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
                Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());
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
