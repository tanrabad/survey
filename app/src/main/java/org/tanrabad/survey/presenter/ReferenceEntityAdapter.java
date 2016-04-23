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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;
import org.tanrabad.survey.repository.BrokerPlaceTypeRepository;
import th.or.nectec.tanrabad.entity.ReferenceEntity;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;

import java.util.ArrayList;
import java.util.List;

public class ReferenceEntityAdapter extends BaseAdapter {

    private Context context;

    private List<ReferenceEntity> listData = new ArrayList<>();

    private ReferenceEntityAdapter(Context context, List<ReferenceEntity> data) {
        this.context = context;
        listData = data;
    }

    public static ReferenceEntityAdapter buildPlaceType(Context context) {
        List<ReferenceEntity> placeTypes = new ArrayList<>();
        placeTypes.add(new PlaceType(-1, context.getString(R.string.not_define_place_type)));
        placeTypes.addAll(BrokerPlaceTypeRepository.getInstance().find());
        return new ReferenceEntityAdapter(context, placeTypes);
    }

    public static ReferenceEntityAdapter buildPlaceTypeForAdd(Context context, boolean canAddVillage) {
        List<ReferenceEntity> placeTypes = new ArrayList<>();
        placeTypes.addAll(BrokerPlaceTypeRepository.getInstance().find());
        if (!canAddVillage)
            placeTypes.remove(new PlaceType(1, "หมู่บ้าน/ชุมชน"));
        return new ReferenceEntityAdapter(context, placeTypes);
    }

    public static ReferenceEntityAdapter buildPlaceSubType(Context context, int placeTypeId) {
        List<ReferenceEntity> placeSubTypes = new ArrayList<>();
        placeSubTypes.addAll(BrokerPlaceSubTypeRepository.getInstance().findByPlaceTypeId(placeTypeId));
        return new ReferenceEntityAdapter(context, placeSubTypes);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public ReferenceEntity getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listData.get(i).getId();
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
        holder.nameView.setText(listData.get(i).getName());
        view.setTag(holder);

        return view;
    }

    public int getPosition(int id) {
        for (ReferenceEntity placeSubType : listData) {
            if (placeSubType.getId() == id) {
                return listData.indexOf(placeSubType);
            }
        }
        return -1;
    }

    public class ViewHolder {
        protected TextView nameView;
    }
}
