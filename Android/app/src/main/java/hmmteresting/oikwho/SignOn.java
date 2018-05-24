package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignOn extends AppCompatActivity {

    String userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);
        Intent intent = getIntent();

        //회원정보 입력
        EditText this_edit_name = findViewById(R.id.edit_name);
        EditText this_edit_id = findViewById(R.id.edit_id);
        EditText this_edit_pwd = findViewById(R.id.edit_pwd);

        final String name, id, pwd;
        name = this_edit_name.getText().toString();
        id = this_edit_id.getText().toString();
        pwd = this_edit_pwd.getText().toString();


        //카메라액티비티로 이동
        Button goPic = findViewById(R.id.btn_goCamera);
        Button.OnClickListener click_btn_goPic = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                userinfo = "name: "+name+", id: "+id+", password : "+pwd;
                Toast.makeText(SignOn.this, userinfo, Toast.LENGTH_LONG).show();

              /*  Intent go_camera = new Intent(
                        getApplicationContext(),
                        Camera.class
                );
              */
            }
        };
    }
}
