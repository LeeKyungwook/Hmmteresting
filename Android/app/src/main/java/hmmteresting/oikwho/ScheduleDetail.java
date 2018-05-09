package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleDetail extends AppCompatActivity {

    String str_addedScheduleDetail = "";

    Button.OnClickListener click_btn_submit = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //connect ScheduleDetail-setPrivacy(아직 안만들엇음)

            Toast.makeText(ScheduleDetail.this, str_addedScheduleDetail, Toast.LENGTH_SHORT).show();
            Intent go_scheduleDetail = new Intent(
                    getApplicationContext(),
                    MainActivity.class
            );
            startActivity(go_scheduleDetail);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        EditText this_edit_title = (EditText)findViewById(R.id.edit_title);
        EditText this_edit_startDate = (EditText)findViewById(R.id.edit_startDate);
        EditText this_edit_startTime = (EditText)findViewById(R.id.edit_startTime);
        EditText this_edit_endDate = (EditText)findViewById(R.id.edit_endDate);
        EditText this_edit_endTime = (EditText)findViewById(R.id.edit_endTime);
        EditText this_edit_prepare = (EditText)findViewById(R.id.edit_prepare);

        String title = this_edit_title.getText().toString();
        String startDate = this_edit_startDate.getText().toString();
        /*try {
            Date startDate_inDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }  //translate start date in String foramt to Date fomat*/
        String startTime = this_edit_startTime.getText().toString();
        /*try {
            Time startTime_inTime = new SimpleDateFormat("HH:mm").parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }  //translate start time in String format to Time format*/
        String endDate = this_edit_endDate.getText().toString();
        /*try {
            Date endDate_inDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }  //translate end date in String foramt to Date fomat*/
        String endTime = this_edit_endTime.getText().toString();
        /*try {
            Time endTime_inTime = new SimpleDateFormat("HH:mm").parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }  //translate start time in String format to Time format*/
        String prepare = this_edit_prepare.getText().toString();
        //it may not return str with newline. it may return str[0]. str[1], str[2]..i don't know.... plz help me

        str_addedScheduleDetail = "";
        str_addedScheduleDetail = str_addedScheduleDetail+"Title : "+title;

        Button btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(click_btn_submit);
    }
}
