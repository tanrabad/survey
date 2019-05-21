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

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.UUID;
import nectec.thai.address.AddressPrinter;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.lookup.District;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.entity.lookup.Province;
import org.tanrabad.survey.entity.lookup.Subdistrict;
import org.tanrabad.survey.job.AbsJobRunner;
import org.tanrabad.survey.job.EntomologyJob;
import org.tanrabad.survey.job.Job;
import org.tanrabad.survey.job.UploadJobRunner;
import org.tanrabad.survey.presenter.view.KeyContainerView;
import org.tanrabad.survey.presenter.view.RelativeTimeAgoTextView;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;
import org.tanrabad.survey.repository.persistence.DbDistrictRepository;
import org.tanrabad.survey.repository.persistence.DbProvinceRepository;
import org.tanrabad.survey.repository.persistence.DbSubdistrictRepository;
import org.tanrabad.survey.service.json.JsonEntomology;
import org.tanrabad.survey.service.json.JsonKeyContainer;
import org.tanrabad.survey.utils.android.InternetConnection;
import org.tanrabad.survey.utils.android.ResourceUtils;
import org.tanrabad.survey.utils.time.ThaiDatePrinter;
import org.tanrabad.survey.utils.time.ThaiDateTimeConverter;

public class SurveyResultDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String FRAGMENT_TAG = "survey_result";
    private static final String ARG_PLACE_ID = "place_id";
    private static final String BUILDING = "อาคาร";
    private static final String HOUSE = "บ้าน";
    private RelativeLayout surveyResultLayout;
    private RelativeLayout reportUpdateLayout;
    private ImageView placeIconView;
    private TextView placeSubTypeView;
    private TextView surveyDateView;
    private RelativeTimeAgoTextView resultUpdateView;
    private TextView placeNameView;
    private TextView addressView;
    private TextView houseIndexView;
    private TextView containerIndexView;
    private TextView breteauIndexView;
    private TextView surveyCountView;
    private TextView surveyFoundCountView;
    private TextView surveyNotFoundCountView;
    private TextView surveyDuplicateView;
    private TextView surveyPersonCountView;
    private TextView noContainerHousesView;
    private TextView containerCountView;
    private LinearLayout indoorContainerLayout;
    private LinearLayout outdoorContainerLayout;
    private ProgressBar surveyResultProgressBar;
    private TextView errorMsgView;
    private Button gotItButton;
    private EntomologyJob jsonEntomologyGetDataJob;
    private Place place;

    public static SurveyResultDialogFragment newInstances(Place place) {
        SurveyResultDialogFragment fragment = new SurveyResultDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_ID, place.getId().toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_survey_result, container, false);
        assignViews(view);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    private void assignViews(View view) {
        surveyResultLayout = view.findViewById(R.id.result_layout);
        reportUpdateLayout = view.findViewById(R.id.report_update_layout);
        placeIconView = view.findViewById(R.id.place_icon);
        placeSubTypeView = view.findViewById(R.id.place_type);
        placeNameView = view.findViewById(R.id.place_name);
        addressView = view.findViewById(R.id.address);
        houseIndexView = view.findViewById(R.id.house_index);
        houseIndexView = view.findViewById(R.id.house_index);
        containerIndexView = view.findViewById(R.id.container_index);
        breteauIndexView = view.findViewById(R.id.breteau_index);
        surveyCountView = view.findViewById(R.id.survey_count);
        surveyFoundCountView = view.findViewById(R.id.survey_found_count);
        surveyNotFoundCountView = view.findViewById(R.id.survey_not_found_count);
        surveyPersonCountView = view.findViewById(R.id.survey_person_count);
        noContainerHousesView = view.findViewById(R.id.no_container_houses);
        surveyDuplicateView = view.findViewById(R.id.survey_duplicate_count);
        containerCountView = view.findViewById(R.id.container_count);
        indoorContainerLayout = view.findViewById(R.id.indoor_container);
        outdoorContainerLayout = view.findViewById(R.id.outdoor_container);
        surveyResultProgressBar = view.findViewById(R.id.loading);
        errorMsgView = view.findViewById(R.id.error_msg);
        surveyDateView = view.findViewById(R.id.survey_date);
        resultUpdateView = view.findViewById(R.id.report_update);

        gotItButton = view.findViewById(R.id.got_it);
        gotItButton.setOnClickListener(this);

        ImageView syncDataButton = view.findViewById(R.id.update_button);
        syncDataButton.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.got_it:
                dismiss();
                break;
            case R.id.update_button:
                startJob(place);
                break;
        }
    }

    private void startJob(Place place) {
        if (InternetConnection.isAvailable(getContext())) {
            AbsJobRunner jobRunner = new SurveyResultJobRunner();
            jobRunner.addJobs(new UploadJobRunner.Builder().getJobs());
            jsonEntomologyGetDataJob = new EntomologyJob(place);
            jobRunner.addJob(jsonEntomologyGetDataJob);
            jobRunner.start();
        } else {
            showErrorMessageView();
            errorMsgView.setText(R.string.please_connect_internet_before_view_entomology);
        }
    }

    private void showErrorMessageView() {
        reportUpdateLayout.setVisibility(View.INVISIBLE);
        surveyResultLayout.setVisibility(View.INVISIBLE);
        surveyResultProgressBar.setVisibility(View.INVISIBLE);
        errorMsgView.setVisibility(View.VISIBLE);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String placeId = getArguments().getString(ARG_PLACE_ID);
        place = BrokerPlaceRepository.getInstance().findByUuid(UUID.fromString(placeId));
        setPlaceInfo(place);
        startJob(place);
    }

    private void setPlaceInfo(Place place) {
        placeIconView.setImageResource(PlaceIconMapping.getPlaceIcon(place));
        placeSubTypeView.setText(
            BrokerPlaceSubTypeRepository.getInstance().findById(place.getSubType()).getName());
        placeNameView.setText(place.getName());
        Subdistrict subdistrict =
            DbSubdistrictRepository.getInstance().findByCode(place.getSubdistrictCode());
        District district =
            DbDistrictRepository.getInstance().findByCode(subdistrict.getDistrictCode());
        Province province =
            DbProvinceRepository.getInstance().findByCode(district.getProvinceCode());
        addressView.setText(
            AddressPrinter.print(subdistrict.getName(), district.getName(), province.getName()));
    }

    private boolean isVillage(JsonEntomology jsonEntomology) {
        return jsonEntomology.placeType == PlaceType.VILLAGE_COMMUNITY;
    }

    private void showProgressBar() {
        reportUpdateLayout.setVisibility(View.INVISIBLE);
        surveyResultProgressBar.setVisibility(View.VISIBLE);
        errorMsgView.setVisibility(View.INVISIBLE);
        gotItButton.setEnabled(false);
    }

    private void hideKeyContainerLayout() {
        getView().findViewById(R.id.key_container_title).setVisibility(View.GONE);
        getView().findViewById(R.id.indoor_title).setVisibility(View.GONE);
        getView().findViewById(R.id.outdoor_title).setVisibility(View.GONE);
        indoorContainerLayout.setVisibility(View.GONE);
        outdoorContainerLayout.setVisibility(View.GONE);
    }

    private void updateEntomologyInfo(JsonEntomology jsonEntomology) {
        if (!isActivityAvailable()) return;

        final boolean isVillage = isVillage(jsonEntomology);

        setSurveyDate(jsonEntomology);
        resultUpdateView.setTime(ThaiDateTimeConverter.convert(jsonEntomology.reportUpdate));
        setPlaceBackgroundIcon(jsonEntomology);
        setSurveyIndex(jsonEntomology, isVillage);
        surveyCountView.setText(
            String.format(getString(R.string.survey_count), isVillage ? HOUSE : BUILDING,
                jsonEntomology.numSurveyedHouses));
        surveyFoundCountView.setText(
            String.format(getString(R.string.survey_found_count), jsonEntomology.numFoundHouses));
        surveyNotFoundCountView.setText(String.format(getString(R.string.survey_not_found_count),
            jsonEntomology.numSurveyedHouses
                - jsonEntomology.numFoundHouses
                - jsonEntomology.numNoContainerHouses));
        noContainerHousesView.setText(String.format(getString(R.string.no_container_houses),
            jsonEntomology.numNoContainerHouses));
        surveyDuplicateView.setText(String.format(getString(R.string.duplicate_survey_count),
            jsonEntomology.numDuplicateSurvey));
        surveyPersonCountView.setText(
            String.format(getString(R.string.sum_person_count), jsonEntomology.sumPersonCount));
        containerCountView.setText(String.format(getString(R.string.survey_container_count),
            jsonEntomology.numSurveyedContainer, jsonEntomology.numFoundContainers));
        setKeyContainerInfo(jsonEntomology);
    }

    private void setSurveyDate(JsonEntomology jsonEntomology) {
        String surveyDate;
        if (jsonEntomology.surveyStartDate == null) {
            surveyDate = getString(R.string.no_survey_start_date);
        } else if (jsonEntomology.surveyStartDate.equals(jsonEntomology.surveyEndDate)) {
            surveyDate = ThaiDatePrinter.print(jsonEntomology.surveyStartDate);
        } else {
            surveyDate = ThaiDatePrinter.print(jsonEntomology.surveyStartDate)
                + " - "
                + ThaiDatePrinter.print(jsonEntomology.surveyEndDate);
        }
        surveyDateView.setText(surveyDate);
    }

    private void setPlaceBackgroundIcon(JsonEntomology jsonEntomology) {
        FragmentActivity activity = getActivity();
        if (activity == null) return;

        ResourceUtils resourceUtils = ResourceUtils.from(activity);
        int color;
        if (place.getType() == PlaceType.VILLAGE_COMMUNITY) {
            if (jsonEntomology.hiValue <= 10) {
                color = resourceUtils.getColor(R.color.without_larvae);
            } else if (jsonEntomology.hiValue <= 50) {
                color = resourceUtils.getColor(R.color.have_larvae_warning);
            } else {
                color = resourceUtils.getColor(R.color.have_larvae);
            }
        } else {
            if (jsonEntomology.ciValue == 0) {
                color = resourceUtils.getColor(R.color.without_larvae);
            } else if (jsonEntomology.ciValue <= 5) {
                color = ResourceUtils.from(getContext()).getColor(R.color.have_larvae_warning);
            } else {
                color = resourceUtils.getColor(R.color.have_larvae);
            }
        }
        ((GradientDrawable) placeIconView.getBackground()).setColor(color);
    }

    private void setSurveyIndex(JsonEntomology jsonEntomology, boolean isVillage) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (isVillage) {
            houseIndexView.setVisibility(View.VISIBLE);
            breteauIndexView.setVisibility(View.VISIBLE);
            houseIndexView.setText(
                String.format(getString(R.string.house_index), df.format(jsonEntomology.hiValue)));
            breteauIndexView.setText(String.format(getString(R.string.breteau_index),
                df.format(jsonEntomology.biValue)));
        } else {
            houseIndexView.setVisibility(View.INVISIBLE);
            breteauIndexView.setVisibility(View.INVISIBLE);
        }
        containerIndexView.setText(
            String.format(getString(R.string.container_index), df.format(jsonEntomology.ciValue)));
    }

    private void setKeyContainerInfo(JsonEntomology jsonEntomology) {
        indoorContainerLayout.removeAllViews();
        outdoorContainerLayout.removeAllViews();
        for (int index = 0; index < 3; index++) {
            JsonKeyContainer indoorKeyContainer = jsonEntomology.keyContainerIn.get(index);
            JsonKeyContainer outdoorKeyContainer = jsonEntomology.keyContainerOut.get(index);

            if (index == 0
                && TextUtils.isEmpty(indoorKeyContainer.containerName)
                && TextUtils.isEmpty(outdoorKeyContainer.containerName)) {
                hideKeyContainerLayout();
            } else {
                setupKeyContainer(indoorKeyContainer, indoorContainerLayout);
                setupKeyContainer(outdoorKeyContainer, outdoorContainerLayout);
            }
        }
    }

    private void setupKeyContainer(JsonKeyContainer keyContainer, LinearLayout container) {
        if (keyContainer.containerId == null || keyContainer.containerName == null) return;

        String[] sameLevelKeyContainerId = keyContainer.containerId.split(", ");
        String[] sameLevelKeyContainerName = keyContainer.containerName.split(", ");
        int sameLevelSize = sameLevelKeyContainerId.length;

        for (int i = 0; i < sameLevelSize; i++) {
            KeyContainerView keyContainerView =
                buildKeyContainerView(Integer.valueOf(sameLevelKeyContainerId[i]),
                    sameLevelKeyContainerName[i]);

            if (i == sameLevelSize - 1) keyContainerView.setLayoutParams(addSpace());

            container.addView(keyContainerView);
        }
    }

    private LinearLayout.LayoutParams addSpace() {
        LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 16);
        return layoutParams;
    }

    private KeyContainerView buildKeyContainerView(int containerId, String containerName) {
        KeyContainerView keyContainerView = new KeyContainerView(getContext());
        keyContainerView.setContainerType(containerId, containerName);
        return keyContainerView;
    }

    public boolean isActivityAvailable() {
        FragmentActivity activity = getActivity();
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }

    public class SurveyResultJobRunner extends AbsJobRunner {
        private JsonEntomology entomology;

        @Override protected void onJobError(Job errorJob, Exception exception) {
            super.onJobError(errorJob, exception);
            TanrabadApp.log(exception);
        }

        @Override protected void onJobDone(Job job) {
            super.onJobDone(job);

            if (job.equals(jsonEntomologyGetDataJob)) {
                EntomologyJob entomologyJob = (EntomologyJob) job;
                entomology = entomologyJob.getData();
            }
        }

        @Override protected void onJobStart(Job startingJob) {
            showProgressBar();
        }

        @Override protected void onRunFinish() {
            surveyResultProgressBar.setVisibility(View.GONE);
            gotItButton.setEnabled(true);
            if (entomology != null) {
                reportUpdateLayout.setVisibility(View.VISIBLE);
                surveyResultLayout.setVisibility(View.VISIBLE);
                updateEntomologyInfo(entomology);
            } else {
                surveyResultLayout.setVisibility(View.INVISIBLE);
                errorMsgView.setVisibility(View.VISIBLE);
                errorMsgView.setText(R.string.cannot_view_survey_result);
            }
        }
    }
}
