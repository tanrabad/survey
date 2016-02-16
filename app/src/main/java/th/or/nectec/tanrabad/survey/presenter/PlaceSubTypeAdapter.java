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
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceSubTypeAdapter extends BaseAdapter {

    Context context;

    List<PlaceSubType> placeSubTypes = new ArrayList<>();

    public PlaceSubTypeAdapter(Context context, int placeTypeId) {
        this.context = context;
        placeSubTypes = BrokerPlaceSubTypeRepository.getInstance().findByPlaceTypeID(placeTypeId);
    }

    @Override
    public int getCount() {
        return placeSubTypes.size();
    }

    @Override
    public PlaceSubType getItem(int i) {
        return placeSubTypes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return placeSubTypes.get(i).getId();
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
        holder.nameView.setText(placeSubTypes.get(i).getName());
        view.setTag(holder);

        return view;
    }

    public int getPosition(int subtypeId) {
        for (PlaceSubType placeSubType : placeSubTypes) {
            if (placeSubType.getId() == subtypeId) {
                return placeSubTypes.indexOf(placeSubType);
            }
        }
        return -1;
    }

    public class ViewHolder {
        TextView nameView;
    }
}
