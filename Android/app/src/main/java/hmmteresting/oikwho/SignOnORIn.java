package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignOnORIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on_orin);

        Button signIn = findViewById(R.id.btn_signIN);
        Button signOn = findViewById(R.id.btn_signON);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 액티비티로
                Intent go_signIN = new Intent(
                        getApplicationContext(),
                        MainActivity.class
                );
                startActivity(go_signIN);
            }
        });

        signOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 액티비티로
                Intent go_sign_ON = new Intent(
                        getApplicationContext(),
                        SignOn.class
                );
                startActivity(go_sign_ON);
            }
        });

    }
}
