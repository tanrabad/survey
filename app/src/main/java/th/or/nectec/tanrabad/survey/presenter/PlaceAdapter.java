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

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> implements ListViewAdapter<Place> {

    Context context;

    ArrayList<Place> places = new ArrayList<>();
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public PlaceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void updateData(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        this.places.clear();
        notifyDataSetChanged();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeTextView.setText(place.getName());
        holder.placeIcon.setImageResource(PlaceIconMapping.getPlaceIcon(place));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    private void onItemHolderLongClick(ViewHolder itemHolder) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView placeTextView;
        ImageView placeIcon;
        private PlaceAdapter adapter;

        public ViewHolder(View itemView, PlaceAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            placeTextView = (TextView) itemView.findViewById(R.id.place_name);
            placeIcon = (ImageView) itemView.findViewById(R.id.place_icon);
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
