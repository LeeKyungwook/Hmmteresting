package hmmteresting.oikwho;

import android.annotation.SuppressLint;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to server. exchanged nothing
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendar);
        final Integer[] selectedDate = {0};
        final String sh = "suhyun";

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Button btn_add_schedule = (Button)findViewById(R.id.btn_scheduleInsert);
                Button btn_update_schedule = (Button)findViewById(R.id.btn_scheduleUpdate);
                Button btn_delete_schedule = (Button)findViewById(R.id.btn_scheduleDelete);

                btn_add_schedule.setVisibility(View.VISIBLE);
                btn_delete_schedule.setVisibility(View.VISIBLE);
                btn_update_schedule.setVisibility(View.VISIBLE);

                selectedDate[0] = year*10000 + (month + 1)*100 + dayOfMonth;

                btn_add_schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //connect main activity-schedule_detail
                        Intent go_scheduleDetail = new Intent(getApplicationContext(),
                                ScheduleDetail.class
                        );
                        go_scheduleDetail.putExtra("userName",sh);  //"suhyun" is dummy data
                        startActivity(go_scheduleDetail);
                    }
                });

                btn_delete_schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //connect main activity-schedule list(of that day with checkbox)
                        Intent go_scheduleList = new Intent(getApplicationContext(),
                                ScheduleList.class);
                        go_scheduleList.putExtra("selectday", selectedDate[0]);
                        go_scheduleList.putExtra("userName", sh);
                        startActivity(go_scheduleList);

                        Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    }
                });

                btn_update_schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //connect main activity-schedule_list(of that day)
                        Intent go_scheduleList = new Intent(getApplicationContext(),
                                ScheduleList.class);
                        go_scheduleList.putExtra("selectday", selectedDate[0]);
                        startActivity(go_scheduleList);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //서버랑 연결 끊음,..  여기서 끊어?아ㅏ닐걸~
    }
}
