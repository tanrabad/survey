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

package th.or.nectec.tanrabad.survey.presenter.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.utils.UUIDUtils;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.MacAddressUtils;

public class SurveyContainerView extends LinearLayout {
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
        super(context);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_survey_container, this);
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

    public SurveyContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    public void setContainerType(ContainerType container) {
        containerType = container;
        containerTypeView.setText(container.getName());
    }

    public void setContainerIcon(@DrawableRes int iconResource) {
        containerIconView.setImageResource(iconResource);
    }

    public SurveyDetail getSurveyDetail() {
        return new SurveyDetail(UUIDUtils.generateV1(MacAddressUtils.getMacAddress(getContext())),
                containerType, getTotalValue(), getFoundValue());
    }

    public void setSurveyDetail(SurveyDetail surveyDetail) {
        int totalContainer = surveyDetail.getTotalContainer();
        if (totalContainer > 0)
            totalContainerView.setText(String.valueOf(totalContainer));

        int foundLarvaContainer = surveyDetail.getFoundLarvaContainer();
        if (foundLarvaContainer > 0)
            foundContainerView.setText(String.valueOf(surveyDetail.getFoundLarvaContainer()));
    }

    public boolean isValid() {
        if (getFoundValue() > getTotalValue()) {
            setBackgroundColor(getColor(R.color.pink_transparent_30));
            return false;
        } else {
            setBackgroundColor(Color.TRANSPARENT);
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private int getColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return getResources().getColor(color, getContext().getTheme());
        else
            //noinspection deprecation
            return getResources().getColor(color);
    }
}
