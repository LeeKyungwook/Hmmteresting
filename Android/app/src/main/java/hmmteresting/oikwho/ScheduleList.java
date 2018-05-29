package hmmteresting.oikwho;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScheduleList extends AppCompatActivity {

    private ArrayList<CustomListAdapter.ScheduleDataList> Schedule_Array_Data;
    private CustomListAdapter.ScheduleDataList Schedule_Data;
    private CustomListAdapter Schedule_Data_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        Intent intent = getIntent();
        int getDate = intent.getIntExtra("selectday",-1);

        Integer month = getDate/100;
        month %= 100;
        Integer day = getDate % 100;

        ActionBar ab = getSupportActionBar();
        ab.setTitle(month+"월"+day+"일의 일정");

        //서버에서 스케줄 정보 받아오기

        //받아온 스케줄정보 표시하기
        //http://necos.tistory.com/entry/Android-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EB%B7%B0%EB%A5%BC-%EB%82%B4-%EB%A7%98%EB%8C%80%EB%A1%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EC%9E%90%EC%BB%A4%EC%8A%A4%ED%85%80-%EB%A6%AC%EC%8A%A4%ED%8A%B8%EB%B7%B0

        ListView scheduleList = (ListView)findViewById(R.id.lv_scheduleList);
        Schedule_Array_Data = new ArrayList<CustomListAdapter.ScheduleDataList>();

        Schedule_Data = new CustomListAdapter.ScheduleDataList("1번째",
                "5월 28일 4시부터");
        Schedule_Array_Data.add(Schedule_Data);
        Schedule_Data = new CustomListAdapter.ScheduleDataList("2번째",
                "5월29일 13시부터");
        Schedule_Array_Data.add(Schedule_Data);

        Schedule_Data_Adapter = new CustomListAdapter(this,
                android.R.layout.simple_list_item_1, Schedule_Array_Data);
        scheduleList.setAdapter(Schedule_Data_Adapter);
    }
}
