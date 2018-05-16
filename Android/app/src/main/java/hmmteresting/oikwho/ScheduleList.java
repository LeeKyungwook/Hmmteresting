package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScheduleList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        Intent intent = getIntent();
        ListView scheduleList;
        String[] dummyItems;
        int getDate = intent.getIntExtra("selectday",-1);

        Integer month = getDate/100;
        month %= 100;
        Integer day = getDate % 100;

        ActionBar ab = getSupportActionBar();
        ab.setTitle(month+"월"+day+"일의 일정");

        //서버에서 스케줄 정보 받아오기

        //받아온 스케줄정보 표시하기
        ArrayAdapter<String> dummyAdapter;
        scheduleList = (ListView)findViewById(R.id.lv_scheduleList);

        dummyItems = new String[6];
        for(int i = 0; i < 6; i ++) {
            dummyItems[i] = "Listview Example"+i;
        }
        dummyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dummyItems);

        scheduleList.setAdapter(dummyAdapter);
    }
}
