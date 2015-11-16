package th.or.nectec.tanrabad.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Building;

public class SurveyBuildingHistoryAdapter extends BaseAdapter {

    Context context;

    ArrayList<Building> surveyBuildings = new ArrayList<>();

    public SurveyBuildingHistoryAdapter(Context context, List<Building> surveyBuildings){
        this.context = context;
        this.surveyBuildings.addAll(surveyBuildings);
    }

    @Override
    public int getCount() {
        return surveyBuildings.size();
    }

    @Override
    public Building getItem(int i) {

        return surveyBuildings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        if(view == null || view.getTag() == null){
            view = inflater.inflate(R.layout.survey_building_history_item, parent, false);
            holder = new ViewHolder();
            holder.surveyBuildingTextView = (TextView)view.findViewById(R.id.survey_building_name);
            holder.surveyBuildingIcon = (ImageView)view.findViewById(R.id.survey_building_icon);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.surveyBuildingTextView.setText(surveyBuildings.get(i).getName());
        view.setTag(holder);

        return view;
    }
    public class ViewHolder{
        TextView surveyBuildingTextView;
        ImageView surveyBuildingIcon;
    }
}
