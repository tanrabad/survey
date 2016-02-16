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

package th.or.nectec.tanrabad.survey.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceTypeRepository;

import java.util.ArrayList;

public class PlaceTypeForAddAdapter extends BaseAdapter {

    Context context;

    ArrayList<PlaceType> placeTypes = new ArrayList<>();

    public PlaceTypeForAddAdapter(Context context, boolean canAddVillage) {
        this.context = context;
        ArrayList<PlaceType> placeTypes = new ArrayList<>();
        placeTypes.addAll(BrokerPlaceTypeRepository.getInstance().find());
        if (!canAddVillage)
            placeTypes.remove(new PlaceType(1, "หมู่บ้าน/ชุมชน"));
        this.placeTypes.addAll(placeTypes);
    }

    @Override
    public int getCount() {
        return placeTypes.size();
    }

    @Override
    public PlaceType getItem(int i) {
        return placeTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return placeTypes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.list_item_spinner, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) view.findViewById(R.id.text_item);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameView.setText(placeTypes.get(i).getName());
        view.setTag(holder);

        return view;
    }

    public int getPlaceTypePosition(int placetypeid) {
        for (PlaceType eachPlaceTypes : placeTypes) {
            if (eachPlaceTypes.getId() == placetypeid)
                return placeTypes.indexOf(eachPlaceTypes);
        }
        return -1;
    }

    public class ViewHolder {
        TextView nameView;
    }

}
