package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    String userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);
        Intent intent = getIntent();

        //회원정보 입력
        final EditText this_edit_name = findViewById(R.id.edit_name);
        final EditText this_edit_id = findViewById(R.id.edit_id);
        final EditText this_edit_pwd = findViewById(R.id.edit_pwd);
        final EditText this_edit_family = findViewById(R.id.edit_family);

        final String[] name = new String[1];
        final String[] id = new String[1];
        final String[] pwd = new String[1];
        final String[] familyCode = new String[1];


        //카메라액티비티로 이동
        Button goPic = findViewById(R.id.btn_goCamera);

        goPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_camera = new Intent(
                        getApplicationContext(),
                        TakePhoto.class
                );
                startActivity(go_camera);
            }
        });

        Button submit = findViewById(R.id.btn_signUp_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = this_edit_name.getText().toString();
                id[0] = this_edit_id.getText().toString();
                pwd[0] = this_edit_pwd.getText().toString();

                userinfo = "{name: \'"+ name[0] +"\' , id: \'"+ id[0] +"\' , password : \' "+ pwd[0] + "\', " +
                        "image: [ {";
                Toast.makeText(SignUp.this, userinfo, Toast.LENGTH_LONG).show();
                //3개의 이미지를 byte배열로 변환해서 붙이고, userinfo를 완성하자!


            }
        });
    }
}
