/*
 * Copyright (c) 2015 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.survey.view.TimeAgoView;

import java.util.ArrayList;
import java.util.List;

public class SurveyBuildingHistoryAdapter extends BaseAdapter {

    Context context;

    ArrayList<Survey> surveyBuildings = new ArrayList<>();

    public SurveyBuildingHistoryAdapter(Context context, List<Survey> surveyBuildings){
        this.context = context;
        this.surveyBuildings.addAll(surveyBuildings);
    }

    @Override
    public int getCount() {
        return surveyBuildings.size();
    }

    @Override
    public Survey getItem(int i) {
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
            view = inflater.inflate(R.layout.list_item_survey_building_history, parent, false);
            holder = new ViewHolder();
            holder.surveyBuildingTextView = (TextView)view.findViewById(R.id.survey_building_name);
            holder.surveyBuildingIcon = (ImageView)view.findViewById(R.id.survey_building_icon);
            holder.timeAgoView = (TimeAgoView) view.findViewById(R.id.time_ago);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Survey currentSurvey = surveyBuildings.get(i);
        holder.surveyBuildingTextView.setText(currentSurvey.getSurveyBuilding().getName());
        holder.timeAgoView.setTime(currentSurvey.getFinishTimestamp());
        view.setTag(holder);

        return view;
    }
    public class ViewHolder{
        TextView surveyBuildingTextView;
        ImageView surveyBuildingIcon;
        TimeAgoView timeAgoView;
    }
}
