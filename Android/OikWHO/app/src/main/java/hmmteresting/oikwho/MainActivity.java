package hmmteresting.oikwho;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button.OnClickListener click_btn_add = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //connect main activity-schedule_detail
            Intent go_scheduleDetail = new Intent(
                    getApplicationContext(),
                    ScheduleDetail.class
            );
            startActivity(go_scheduleDetail);
        }
    };
    Button.OnClickListener click_btn_update = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //connect main activity-schedule_list(of that day)
            Intent go_scheduleList = new Intent(getApplicationContext(),
                    ScheduleList.class);
            startActivity(go_scheduleList);
        }
    };
    Button.OnClickListener click_btn_delete = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //connect main activity-schedule list(of that day with checkbox)
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CalendarView calendar = (CalendarView)findViewById(R.id.calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Button btn_add_schedule = (Button)findViewById(R.id.btn_scheduleInsert);
                Button btn_update_schedule = (Button)findViewById(R.id.btn_scheduleUpdate);
                Button btn_delete_schedule = (Button)findViewById(R.id.btn_scheduleDelete);

                btn_add_schedule.setVisibility(View.VISIBLE);
                btn_delete_schedule.setVisibility(View.VISIBLE);
                btn_update_schedule.setVisibility(View.VISIBLE);

                btn_add_schedule.setOnClickListener(click_btn_add);
                btn_delete_schedule.setOnClickListener(click_btn_delete);
                btn_update_schedule.setOnClickListener(click_btn_update);
            }
        });
    }
}
