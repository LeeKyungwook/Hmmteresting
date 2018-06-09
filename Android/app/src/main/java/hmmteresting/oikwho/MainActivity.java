package hmmteresting.oikwho;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("어잌WHO");

        Intent intent = getIntent();
        //connect to server. exchanged nothing
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendar);
        final Integer[] selectedDate = {0};
        final String userName; //사인-인(로그인)에서 서버로부터 반환받은 이름을 저장할것
        userName = intent.getStringExtra("userName");

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
                        go_scheduleDetail.putExtra("selectday", selectedDate[0]);
                        go_scheduleDetail.putExtra("userName",userName);
                        startActivity(go_scheduleDetail);
                    }
                });

                btn_delete_schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent go_scheduleDelete = new Intent(getApplicationContext(),
                                ScheduleDelete.class);
                        go_scheduleDelete.putExtra("selectday", selectedDate[0]);
                        go_scheduleDelete.putExtra("userName", userName);
                        startActivity(go_scheduleDelete);
                        }
                });

                btn_update_schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent go_scheduleList = new Intent(getApplicationContext(),
                                ScheduleList.class);
                        go_scheduleList.putExtra("selectday", selectedDate[0]);
                        go_scheduleList.putExtra("userName", userName);
                        startActivity(go_scheduleList);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
