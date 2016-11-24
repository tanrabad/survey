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

package org.tanrabad.survey.presenter.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tanrabad.survey.R;
import org.tanrabad.survey.entity.SurveyDetail;
import org.tanrabad.survey.entity.lookup.ContainerType;
import org.tanrabad.survey.entity.utils.UuidUtils;
import org.tanrabad.survey.utils.MacAddressUtils;
import org.tanrabad.survey.utils.android.ResourceUtils;

import java.util.HashMap;

public class SurveyContainerView extends RelativeLayout {
    private SurveyDetail surveyDetail;
    private ContainerType containerType;
    private TextView containerTypeView;
    private EditText totalContainerView;
    private EditText foundContainerView;
    private ImageView containerIconView;
    private OnLongClickListener showAdvanceStepperDialog = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            new AdvanceStepperDialog(getContext(), (TextView) view).show();
            return true;
        }
    };

    public SurveyContainerView(Context context) {
        this(context, new ContainerType(1, "น้ำใช้")); //this for Layout Tool of Android Studio
    }

    public SurveyContainerView(Context context, ContainerType containerType) {
        this(context, null, containerType);
    }

    private SurveyContainerView(Context context, AttributeSet attrs, ContainerType containerType) {
        super(context, attrs);
        initInflate();
        initInstances();
        setContainerType(containerType);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_survey_container, this);
        setMinimumHeight(getContext().getResources().getDimensionPixelOffset(R.dimen.container_view_height));
    }

    private void initInstances() {
        containerTypeView = (TextView) findViewById(R.id.container_type);
        containerIconView = (ImageView) findViewById(R.id.container_icon);
        foundContainerView = (EditText) findViewById(R.id.found_larvae_container);
        foundContainerView.setOnLongClickListener(showAdvanceStepperDialog);
        foundContainerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                increaseTotalWhenFoundMoreThanTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        totalContainerView = (EditText) findViewById(R.id.total_container);
        totalContainerView.setOnLongClickListener(showAdvanceStepperDialog);
        totalContainerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                decreaseFoundWhenTotalLessThanFound();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void increaseTotalWhenFoundMoreThanTotal() {
        if (getFoundValue() > getTotalValue()) {
            totalContainerView.setText(String.valueOf(getFoundValue()));
        }
    }

    private int getFoundValue() {
        String foundStr = foundContainerView.getText().toString();
        return TextUtils.isEmpty(foundStr) ? 0 : Integer.valueOf(foundStr);
    }

    private int getTotalValue() {
        String totalStr = totalContainerView.getText().toString();
        return TextUtils.isEmpty(totalStr) ? 0 : Integer.valueOf(totalStr);
    }

    private void decreaseFoundWhenTotalLessThanFound() {
        if (getTotalValue() < getFoundValue()) {
            foundContainerView.setText(String.valueOf(getTotalValue()));
        }
    }

    private void setContainerType(ContainerType container) {
        containerType = container;
        containerTypeView.setText(container.getName().trim());
        containerIconView.setImageResource(ContainerIconMapping.getContainerIcon(container));
        foundContainerView.setContentDescription(container.getName());
        totalContainerView.setContentDescription(container.getName());
    }


    public SurveyDetail getSurveyDetail() {
        if (surveyDetail != null) {
            surveyDetail.setContainerCount(getTotalValue(), getFoundValue());
            return surveyDetail;
        } else {
            if (getTotalValue() == 0 && getFoundValue() == 0) {
                return null;
            }
            return new SurveyDetail(UuidUtils.generateOrdered(MacAddressUtils.getMacAddress(getContext())),
                    containerType, getTotalValue(), getFoundValue());
        }
    }

    public void setSurveyDetail(SurveyDetail surveyDetail) {
        this.surveyDetail = surveyDetail;
        int totalContainer = surveyDetail.getTotalContainer();
        if (totalContainer > 0)
            totalContainerView.setText(String.valueOf(totalContainer));

        int foundLarvaContainer = surveyDetail.getFoundLarvaContainer();
        if (foundLarvaContainer > 0)
            foundContainerView.setText(String.valueOf(surveyDetail.getFoundLarvaContainer()));
    }

    public boolean isValid() {
        if (getFoundValue() > getTotalValue()) {
            setBackgroundColor(ResourceUtils.from(getContext()).getColor(R.color.pink_transparent_30));
            return false;
        } else {
            setBackgroundColor(Color.TRANSPARENT);
            return true;
        }
    }

    private static class ContainerIconMapping {
        private static HashMap<Integer, Integer> containerIconMapper = getIconMapping();

        private static HashMap<Integer, Integer> getIconMapping() {
            HashMap<Integer, Integer> iconMapper = new HashMap<>();
            iconMapper.put(1, R.mipmap.ic_container_earthen_jar);
            iconMapper.put(2, R.mipmap.ic_container_bottle);
            iconMapper.put(3, R.mipmap.ic_container_vase);
            iconMapper.put(4, R.mipmap.ic_container_ant_tray);
            iconMapper.put(5, R.mipmap.ic_container_pot_saucer);
            iconMapper.put(6, R.mipmap.ic_container_lotus);
            iconMapper.put(7, R.mipmap.ic_container_tire);
            iconMapper.put(8, R.mipmap.ic_container_leaf);
            iconMapper.put(9, R.mipmap.ic_container_garbages);
            iconMapper.put(10, R.mipmap.ic_container_bucket);
            iconMapper.put(11, R.mipmap.ic_container_bowl);
            iconMapper.put(12, R.mipmap.ic_container_fridge);
            return iconMapper;
        }

        @DrawableRes
        private static int getContainerIcon(ContainerType containerType) {
            if (!containerIconMapper.containsKey(containerType.getId()))
                return R.mipmap.ic_building_home_black;
            return containerIconMapper.get(containerType.getId());
        }
    }
}
