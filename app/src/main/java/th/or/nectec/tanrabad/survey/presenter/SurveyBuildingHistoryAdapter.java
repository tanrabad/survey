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

package th.or.nectec.tanrabad.survey.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.view.TimeAgoView;

public class SurveyBuildingHistoryAdapter extends RecyclerView.Adapter<SurveyBuildingHistoryAdapter.ViewHolder> implements ListViewAdapter<Survey> {

    Context context;

    ArrayList<Survey> surveyBuildings = new ArrayList<>();
    private AdapterView.OnItemClickListener onItemClickListener;

    public SurveyBuildingHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void updateData(List<Survey> dataList) {
        this.surveyBuildings.clear();
        this.surveyBuildings.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        this.surveyBuildings.clear();
        notifyDataSetChanged();
    }

    public Survey getItem(int i) {
        return surveyBuildings.get(i);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_survey_building_history, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Survey currentSurvey = surveyBuildings.get(position);
        holder.surveyBuildingTextView.setText(currentSurvey.getSurveyBuilding().getName());
        holder.timeAgoView.setTime(currentSurvey.getFinishTimestamp());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return surveyBuildings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView surveyBuildingTextView;
        ImageView surveyBuildingIcon;
        TimeAgoView timeAgoView;
        private SurveyBuildingHistoryAdapter adapter;

        public ViewHolder(View itemView, SurveyBuildingHistoryAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            surveyBuildingTextView = (TextView) itemView.findViewById(R.id.survey_building_name);
            surveyBuildingIcon = (ImageView) itemView.findViewById(R.id.survey_building_icon);
            timeAgoView = (TimeAgoView) itemView.findViewById(R.id.time_ago);
        }

        @Override
        public void onClick(View view) {
            adapter.onItemHolderClick(this);
        }
    }
}