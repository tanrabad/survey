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

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceAdapter extends BaseAdapter {

    Context context;

    ArrayList<Place> places = new ArrayList<>();

    public PlaceAdapter(Context context) {
        this.context = context;
    }

    public PlaceAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places.addAll(places);
    }

    public void updateData(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.places.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int i) {
        return places.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.place_list_item, parent, false);
            holder = new ViewHolder();
            holder.placeTextView = (TextView) view.findViewById(R.id.place_name);
            holder.placeIcon = (ImageView) view.findViewById(R.id.place_icon);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.placeTextView.setText(places.get(i).getName());
        view.setTag(holder);

        return view;
    }

    public class ViewHolder {
        TextView placeTextView;
        ImageView placeIcon;
    }

}
