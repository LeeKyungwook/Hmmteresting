package hmmteresting.oikwho;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpOrIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_or_in);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("어잌WHO");

        Button signIn = findViewById(R.id.btn_signIN);
        Button signOn = findViewById(R.id.btn_signON);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 액티비티로
                Intent go_signIN = new Intent(
                        getApplicationContext(),
                        SignIn.class
                );
                startActivity(go_signIN);
            }
        });

        signOn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

            //회원가입 액티비티로
            Intent go_sign_ON = new Intent(
                    getApplicationContext(),
                    SignUp.class
            );
            startActivity(go_sign_ON);
            }
        });

    }
}
