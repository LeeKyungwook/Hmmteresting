package hmmteresting.oikwho;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SignUp extends AppCompatActivity {

    String userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

                BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
                resizeOptions.inSampleSize = 4;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/oikwho");
                Bitmap resized_bitmap = BitmapFactory.decodeFile(dir+"/suhyun_1.jpg",resizeOptions);

                String bitmapByte = getStringFromBitmap(resized_bitmap);

                face[0] = bitmapByte;
                name[0] = this_edit_name.getText().toString();
                id[0] = this_edit_id.getText().toString();
                pwd[0] = this_edit_pwd.getText().toString();

                userinfo = "{ name: \'"+ name[0] +"\' , id: \'"+ id[0] +"\' , password : \' "+ pwd[0] + "\', " +
                        "image: [ {"+face[0] + "} ] }";
                //Toast.makeText(SignUp.this, userinfo, Toast.LENGTH_LONG).show();
                Log.d("회원가입 사진",face[0]);
            }
        });
    }

    private String getStringFromBitmap(Bitmap bMap) {
        final int COMPRESSION_QUALITY = 100;

        String encodedBmap;

        ByteArrayOutputStream byteArrayBmapStream = new ByteArrayOutputStream();
        bMap.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,byteArrayBmapStream);
        //png로만 해야하는것인가? jpg로 하면 안되는것인가??

        byte[] b = byteArrayBmapStream.toByteArray();

        encodedBmap = Base64.encodeToString(b,Base64.DEFAULT);

        return encodedBmap;
    }
}
