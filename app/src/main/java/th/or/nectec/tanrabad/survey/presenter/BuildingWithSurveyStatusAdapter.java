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

import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.android.DrawableResource;

public class BuildingWithSurveyStatusAdapter extends RecyclerView.Adapter<BuildingWithSurveyStatusAdapter.ViewHolder> {

    Context context;


    ArrayList<BuildingWithSurveyStatus> buildings = new ArrayList<>();
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public BuildingWithSurveyStatusAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<BuildingWithSurveyStatus> buildings) {
        this.buildings.clear();
        this.buildings.addAll(buildings);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.buildings.clear();
        notifyDataSetChanged();
    }

    public Object getItem(int i) {
        return buildings.get(i);
    }

    @Override
    public BuildingWithSurveyStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_building, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(BuildingWithSurveyStatusAdapter.ViewHolder holder, int position) {
        BuildingWithSurveyStatus buildingWithSurveyStatus = buildings.get(position);
        holder.buildingTextView.setText(buildingWithSurveyStatus.getBuilding().getName());

        if (buildingWithSurveyStatus.isSurvey()) {
            holder.buildingIcon.setBackgroundDrawable(DrawableResource.get(R.drawable.container_bg_pink));
        } else {
            holder.buildingIcon.setBackgroundDrawable(DrawableResource.get(R.drawable.container_bg));
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private void onItemHolderLongClick(ViewHolder itemHolder) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView buildingTextView;
        ImageView buildingIcon;
        private BuildingWithSurveyStatusAdapter adapter;

        public ViewHolder(View itemView, BuildingWithSurveyStatusAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            buildingTextView = (TextView) itemView.findViewById(R.id.building_name);
            buildingIcon = (ImageView) itemView.findViewById(R.id.building_icon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            adapter.onItemHolderClick(this);
        }

        @Override
        public boolean onLongClick(View view) {
            adapter.onItemHolderLongClick(this);
            return true;
        }
    }
}




