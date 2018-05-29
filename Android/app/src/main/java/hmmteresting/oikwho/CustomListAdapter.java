package hmmteresting.oikwho;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<CustomListAdapter.ScheduleDataList> {

    private Context mycontext = null;

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.mycontext = context;
    }

    @Override
    public View getView(int nPosition, View convertView, ViewGroup parent){
        PointerView pView = null;
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(mycontext).inflate(R.layout.activity_schedule_list,null);
            pView = new PointerView(view);
            view.setTag(pView);
        }

        pView = (PointerView)view.getTag();

        ScheduleDataList receivedDataList = (ScheduleDataList) getItem(nPosition); //focus in this

        if(receivedDataList != null) {
            pView.getMainTitleView().setText(receivedDataList.getMainTitle());
            pView.getSubTitleView().setText(receivedDataList.getSubTitle());
        }

        return view;
    }

    private class PointerView {
        private View myBaseView = null;
        private TextView myMainTitle = null;
        private TextView mySubTitle = null;

        public PointerView(View BaseView) {
            this.myBaseView = BaseView;
        }

        public TextView getMainTitleView() {
            if(myMainTitle == null) {
                myMainTitle = (TextView)myBaseView.findViewById(R.id.list_mainTitle);
            }
            return myMainTitle;
        }

        public TextView getSubTitleView() {
            if(mySubTitle == null) {
                mySubTitle = (TextView)myBaseView.findViewById(R.id.list_subTitle);
            }
            return mySubTitle;
        }
    }

    static class ScheduleDataList {
        private String MainTitle;
        private String SubTitle;

        public ScheduleDataList(String title, String fromDate) {
            this.setMainTitle(title);
            this.setSubTitle(fromDate);
        }

        public String getMainTitle() {
            return MainTitle;
        }
        public void setMainTitle(String title) {
            MainTitle = title;
        }

        public String getSubTitle() {
            return SubTitle;
        }
        public void setSubTitle(String title) {
            SubTitle = title;
        }
    }
}