package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScheduleDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        Intent intent = getIntent();

        String title, User, startDate, startTime, endDate, endTime, prepare, isBroadcast;
        //Integer startDate_int, startTime_int, endDate_int, endTime_int, isBroadcast_int;

        EditText this_edit_title = findViewById(R.id.edit_title);
        EditText this_edit_startDate = findViewById(R.id.edit_startDate);
        EditText this_edit_startTime = findViewById(R.id.edit_startTime);
        EditText this_edit_endDate = findViewById(R.id.edit_endDate);
        EditText this_edit_endTime = findViewById(R.id.edit_endTime);
        EditText this_edit_prepare = findViewById(R.id.edit_prepare);

        title = this_edit_title.getText().toString();

        User = intent.getStringExtra("userName");

        startDate = this_edit_startDate.getText().toString();
        //startDate_int = Integer.parseInt(startDate)+1;

        startTime = this_edit_startTime.getText().toString();
        //startTime_int = Integer.parseInt(startTime)+1;

        endDate = this_edit_endDate.getText().toString();
        //endDate_int = Integer.parseInt(endDate)+1;

        endTime = this_edit_endTime.getText().toString();
        //endTime_int = Integer.parseInt(endTime)+1;

        prepare = this_edit_prepare.getText().toString();

        //make setPrivacy function

        final String json_addedScheduleDetail =
                "{title : \"" + title + "\", User : \"" + User + "\", startDate : \"" + startDate +
                        "\", startTime : \"" + startTime + "\", endDate : \"" + endDate +
                        "\", endTime : \"" + endTime + "\" ,reqItems : \"" + prepare + "\" }";

        Button btn_submit = (Button)findViewById(R.id.btn_submit);
        Button.OnClickListener click_btn_submit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //connect ScheduleDetail-setPrivacy(아직 안만들엇음)

                Toast.makeText(ScheduleDetail.this, json_addedScheduleDetail, Toast.LENGTH_LONG).show();
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
}
