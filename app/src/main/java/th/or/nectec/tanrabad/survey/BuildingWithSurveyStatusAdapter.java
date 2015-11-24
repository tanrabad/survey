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

import th.or.nectec.tanrabad.domain.BuildingWithSurveyStatus;

public class BuildingWithSurveyStatusAdapter extends BaseAdapter {

    Context context;


    ArrayList<BuildingWithSurveyStatus> buildings = new ArrayList<>();

    public BuildingWithSurveyStatusAdapter(Context context, List<BuildingWithSurveyStatus> buildings) {
        this.context = context;
        this.buildings.addAll(buildings);
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public Object getItem(int i) {
        return buildings.get(i);
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
            view = inflater.inflate(R.layout.list_item_building, parent, false);
            holder = new ViewHolder();
            holder.buildingTextView = (TextView)view.findViewById(R.id.building_name);
            holder.buildingIcon = (ImageView)view.findViewById(R.id.building_icon);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        BuildingWithSurveyStatus buildingWithSurveyStatus = buildings.get(i);
        holder.buildingTextView.setText(buildingWithSurveyStatus.getBuilding().getName());

        if (buildingWithSurveyStatus.isSurvey()) {
            holder.buildingIcon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.container_bg_pink));
        } else {
            holder.buildingIcon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.container_bg));
        }

        view.setTag(holder);

        return view;
    }

    public  class ViewHolder {
        TextView buildingTextView;
        ImageView buildingIcon;
    }
}




