/*
 * Copyright (c) 2016 NECTEC
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

package org.tanrabad.survey.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.tanrabad.survey.R;
import org.tanrabad.survey.domain.entomology.ContainerIndex;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.presenter.view.TimeAgoView;
import org.tanrabad.survey.repository.persistence.SurveyWithChange;
import org.tanrabad.survey.utils.time.DurationTimePrinter;

public class SurveyBuildingHistoryAdapter extends RecyclerView.Adapter<SurveyBuildingHistoryAdapter.ViewHolder>
        implements ListViewAdapter<Survey> {

    private Context context;
    private List<Survey> surveyBuildings = new ArrayList<>();
    private int buildingIcon;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public SurveyBuildingHistoryAdapter(Context context, @DrawableRes int buildingIcon) {
        this.context = context;
        this.buildingIcon = buildingIcon;
    }

    @Override
    public void updateData(List<Survey> dataList) {
        Collections.sort(dataList, Collections.<Survey>reverseOrder());
        this.surveyBuildings = dataList;
        notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        this.surveyBuildings = new ArrayList<>();
        notifyDataSetChanged();
    }

    public Survey getItem(int position) {
        return surveyBuildings.get(position);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null,
                    itemHolder.itemView,
                    itemHolder.getAdapterPosition(),
                    itemHolder.getItemId());
        }
    }

    private boolean onItemHolderLongClick(ViewHolder viewHolder) {
        if (onItemLongClickListener != null) {
            return onItemLongClickListener.onItemLongClick(null,
                    viewHolder.itemView,
                    viewHolder.getAdapterPosition(),
                    viewHolder.getItemId());
        }
        return false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_survey_building_history,
                parent, false);
        return new ViewHolder(view, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(surveyBuildings.get(position));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return surveyBuildings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView duration;
        private final TextView surveyBuildingTextView;
        private final ImageView surveyBuildingIcon;
        private final TimeAgoView timeAgoView;
        private final TextView containerIndex;
        private final TextView containerCount;
        private final ImageView notSync;
        private final SurveyBuildingHistoryAdapter adapter;
        private final Context context;

        ViewHolder(View itemView, SurveyBuildingHistoryAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.context = adapter.context;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            surveyBuildingTextView = (TextView) itemView.findViewById(R.id.survey_building_name);
            surveyBuildingIcon = (ImageView) itemView.findViewById(R.id.survey_building_icon);
            surveyBuildingIcon.setImageResource(adapter.buildingIcon);
            timeAgoView = (TimeAgoView) itemView.findViewById(R.id.time_ago);
            duration = (TextView) itemView.findViewById(R.id.survey_duration);
            containerIndex = (TextView) itemView.findViewById(R.id.survey_ci);
            containerCount = (TextView) itemView.findViewById(R.id.survey_container_count);
            notSync = (ImageView) itemView.findViewById(R.id.not_sync);
        }

        @Override
        public void onClick(View view) {
            adapter.onItemHolderClick(this);
        }

        @Override
        public boolean onLongClick(View view) {
            return adapter.onItemHolderLongClick(this);
        }

        void bind(Survey survey) {
            surveyBuildingTextView.setText(survey.getSurveyBuilding().getName());
            //noinspection SetTextI18n
            duration.setText(context.getString(R.string.survey_duration, getDuration(survey)));
            timeAgoView.setTime(survey.getFinishTimestamp());
            if (survey instanceof SurveyWithChange) {
                notSync.setVisibility(((SurveyWithChange) survey).isNotSynced() ? View.VISIBLE : View.GONE);
            }
            setCiValue(survey);
        }

        private String getDuration(Survey survey) {
            return DurationTimePrinter.print(survey.getStartTimestamp(), survey.getFinishTimestamp());
        }

        private void setCiValue(Survey currentSurvey) {
            ContainerIndex ci = new ContainerIndex(currentSurvey);
            float ciValue = ci.calculate();
            surveyBuildingIcon.setBackgroundResource(getIconBackgroundByCi(ci));
            containerIndex.setText(ci.getTotalContainer() > 0
                ? context.getString(R.string.format_ci, (int) ciValue)
                : context.getString(R.string.not_available));
            containerCount.setText(Html.fromHtml(context.getString(R.string.format_container_count,
                ci.getTotalContainer(), ci.getFoundLarvaeContainer())));
        }

        @DrawableRes
        private int getIconBackgroundByCi(ContainerIndex ciValue) {
            if (ciValue.getFoundLarvaeContainer() == 0) {
                if (ciValue.getTotalContainer() == 0)
                    return R.drawable.bg_icon;
                return R.drawable.bg_icon_building_without_larvae;
            } else {
                return R.drawable.bg_icon_building_have_larvae;
            }
        }
    }
}
