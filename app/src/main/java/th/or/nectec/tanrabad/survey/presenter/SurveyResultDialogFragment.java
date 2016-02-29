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

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.lookup.District;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.entity.lookup.Province;
import th.or.nectec.tanrabad.entity.lookup.Subdistrict;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.EntomologyJob;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.SyncJobBuilder;
import th.or.nectec.tanrabad.survey.presenter.view.KeyContainerView;
import th.or.nectec.tanrabad.survey.presenter.view.RelativeTimeAgoTextView;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbDistrictRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbProvinceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSubdistrictRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;
import th.or.nectec.tanrabad.survey.service.json.JsonKeyContainer;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDatePrinter;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDateTimeConverter;
import th.or.nectec.thai.address.AddressPrinter;

public class SurveyResultDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String ARG_PLACE_ID = "place_id";
    public static final String FRAGMENT_TAG = "survey_result";
    public static final String BUILDING = "อาคาร";
    public static final String HOUSE = "บ้าน";
    LinearLayout placeInfoLayout;
    RelativeLayout surveyResultLayout;
    RelativeLayout reportUpdateLayout;
    ImageView placeIconView;
    ImageView syncDataView;
    TextView placeSubTypeView;
    TextView surveyDateView;
    RelativeTimeAgoTextView resultUpdateView;
    TextView placeNameView;
    TextView addressView;
    TextView houseIndexView;
    TextView containerIndexView;
    TextView breteauIndexView;
    TextView surveyCountView;
    TextView surveyFoundCountView;
    TextView surveyNotFoundCountView;
    TextView surveyDuplicateView;
    TextView noContainerHousesView;
    TextView containerCountView;
    LinearLayout indoorContainerLayout;
    LinearLayout outdoorContainerLayout;
    ProgressBar progressBar;
    TextView errorMsgView;
    Button gotIt;
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
        surveyResultLayout = (RelativeLayout) view.findViewById(R.id.result_layout);
        reportUpdateLayout = (RelativeLayout) view.findViewById(R.id.report_update_layout);
        placeInfoLayout = (LinearLayout) view.findViewById(R.id.place_info_layout);
        placeIconView = (ImageView) view.findViewById(R.id.place_icon);
        placeSubTypeView = (TextView) view.findViewById(R.id.place_type);
        placeNameView = (TextView) view.findViewById(R.id.place_name);
        addressView = (TextView) view.findViewById(R.id.address);
        houseIndexView = (TextView) view.findViewById(R.id.house_index);
        houseIndexView = (TextView) view.findViewById(R.id.house_index);
        containerIndexView = (TextView) view.findViewById(R.id.container_index);
        breteauIndexView = (TextView) view.findViewById(R.id.breteau_index);
        surveyCountView = (TextView) view.findViewById(R.id.survey_count);
        surveyFoundCountView = (TextView) view.findViewById(R.id.survey_found_count);
        surveyNotFoundCountView = (TextView) view.findViewById(R.id.survey_not_found_count);
        noContainerHousesView = (TextView) view.findViewById(R.id.no_container_houses);
        surveyDuplicateView = (TextView) view.findViewById(R.id.survey_duplicate_count);
        containerCountView = (TextView) view.findViewById(R.id.container_count);
        indoorContainerLayout = (LinearLayout) view.findViewById(R.id.indoor_container);
        outdoorContainerLayout = (LinearLayout) view.findViewById(R.id.outdoor_container);
        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        errorMsgView = (TextView) view.findViewById(R.id.error_msg);
        surveyDateView = (TextView) view.findViewById(R.id.survey_date);
        resultUpdateView = (RelativeTimeAgoTextView) view.findViewById(R.id.report_update);

        gotIt = (Button) view.findViewById(R.id.got_it);
        gotIt.setOnClickListener(this);

        syncDataView = (ImageView) view.findViewById(R.id.update_button);
        syncDataView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
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
            AbsJobRunner jobRunner = new SyncJobBuilder().build(new SurveyResultJobRunner());
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
        progressBar.setVisibility(View.INVISIBLE);
        errorMsgView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String placeId = getArguments().getString(ARG_PLACE_ID);
        place = BrokerPlaceRepository.getInstance().findByUUID(UUID.fromString(placeId));
        setPlaceInfo(place);
        startJob(place);
    }

    private void setPlaceInfo(Place place) {
        placeIconView.setImageResource(PlaceIconMapping.getPlaceIcon(place));
        placeSubTypeView.setText(BrokerPlaceSubTypeRepository.getInstance().findById(place.getSubType()).getName());
        placeNameView.setText(place.getName());
        Subdistrict subdistrict = DbSubdistrictRepository.getInstance().findByCode(place.getSubdistrictCode());
        District district = DbDistrictRepository.getInstance().findByCode(subdistrict.getDistrictCode());
        Province province = DbProvinceRepository.getInstance().findByCode(district.getProvinceCode());
        addressView.setText(AddressPrinter.print(subdistrict.getName(), district.getName(), province.getName()));
    }

    private boolean isVillage(JsonEntomology jsonEntomology) {
        return jsonEntomology.placeType == PlaceType.VILLAGE_COMMUNITY;
    }

    private void showProgressBar() {
        reportUpdateLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        errorMsgView.setVisibility(View.INVISIBLE);
        gotIt.setEnabled(false);
    }

    private void hideKeyContainerLayout() {
        getView().findViewById(R.id.key_container_title).setVisibility(View.GONE);
        getView().findViewById(R.id.indoor_title).setVisibility(View.GONE);
        getView().findViewById(R.id.outdoor_title).setVisibility(View.GONE);
        indoorContainerLayout.setVisibility(View.GONE);
        outdoorContainerLayout.setVisibility(View.GONE);
    }

    private void setPlaceBackgroundIcon(JsonEntomology jsonEntomology) {
        GradientDrawable background = (GradientDrawable) placeIconView.getBackground();
        int color;
        if (place.getType() == PlaceType.VILLAGE_COMMUNITY) {
            if (jsonEntomology.ciValue <= 10)
                color = ResourceUtils.from(getActivity()).getColor(R.color.without_larvae);
            else if (jsonEntomology.ciValue <= 50)
                color = ResourceUtils.from(getActivity()).getColor(R.color.amber_500);
            else
                color = ResourceUtils.from(getActivity()).getColor(R.color.have_larvae);
        } else {
            if (jsonEntomology.ciValue == 0)
                color = ResourceUtils.from(getActivity()).getColor(R.color.without_larvae);
            else
                color = ResourceUtils.from(getActivity()).getColor(R.color.have_larvae);
        }
        background.setColor(color);
    }

    private void setSurveyDate(JsonEntomology jsonEntomology) {
        String surveyDate;
        if (jsonEntomology.surveyStartDate == null) {
            surveyDate = getString(R.string.no_survey_start_date);
        } else if (jsonEntomology.surveyStartDate.equals(jsonEntomology.surveyEndDate)) {
            surveyDate = ThaiDatePrinter.print(jsonEntomology.surveyStartDate);
        } else {
            surveyDate = ThaiDatePrinter.print(jsonEntomology.surveyStartDate)
                    + " - " + ThaiDatePrinter.print(jsonEntomology.surveyEndDate);
        }
        surveyDateView.setText(surveyDate);
    }

    private LinearLayout.LayoutParams addSpace() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 16);

        return layoutParams;
    }

    public class SurveyResultJobRunner extends AbsJobRunner {
        private JsonEntomology entomology;

        @Override
        protected void onJobError(Job errorJob, Exception exception) {
            super.onJobError(errorJob, exception);
            TanrabadApp.log(exception);
        }

        @Override
        protected void onJobDone(Job job) {
            super.onJobDone(job);
            if (job.equals(jsonEntomologyGetDataJob)) {
                EntomologyJob entomologyJob = (EntomologyJob) job;
                entomology = entomologyJob.getData();
            }
        }

        @Override
        protected void onJobStart(Job startingJob) {
            showProgressBar();
        }

        @Override
        protected void onRunFinish() {
            progressBar.setVisibility(View.GONE);
            gotIt.setEnabled(true);
            if (errorJobs() == 0 && entomology != null) {
                reportUpdateLayout.setVisibility(View.VISIBLE);
                surveyResultLayout.setVisibility(View.VISIBLE);
                updateEntomologyInfo(entomology);
            } else {
                surveyResultLayout.setVisibility(View.INVISIBLE);
                errorMsgView.setVisibility(View.VISIBLE);
                errorMsgView.setText(R.string.cannot_view_survey_result);
            }
        }

        private void updateEntomologyInfo(JsonEntomology jsonEntomology) {
            final boolean isVillage = isVillage(jsonEntomology);

            setSurveyDate(jsonEntomology);
            resultUpdateView.setTime(ThaiDateTimeConverter.convert(jsonEntomology.reportUpdate));
            setPlaceBackgroundIcon(jsonEntomology);
            setSurveyIndex(jsonEntomology, isVillage);
            setSurveyCount(jsonEntomology, isVillage);
            setSurveyFoundCount(jsonEntomology);
            setSurveyNotFoundCount(jsonEntomology);
            setNoContainerHouseCount(jsonEntomology);
            setDuplicateSurveyCount(jsonEntomology);
            setSurveyContainerCount(jsonEntomology);
            setKeyContainerInfo(jsonEntomology);
        }

        private void setSurveyIndex(JsonEntomology jsonEntomology, boolean isVillage) {
            DecimalFormat df = new DecimalFormat("#.##");
            if (isVillage) {
                houseIndexView.setVisibility(View.VISIBLE);
                breteauIndexView.setVisibility(View.VISIBLE);
                houseIndexView.setText(String.format(getString(R.string.house_index),
                        df.format(jsonEntomology.hiValue)));
                breteauIndexView.setText(String.format(getString(R.string.breteau_index),
                        df.format(jsonEntomology.biValue)));
            } else {
                houseIndexView.setVisibility(View.INVISIBLE);
                breteauIndexView.setVisibility(View.INVISIBLE);
            }
            containerIndexView.setText(String.format(getString(R.string.container_index),
                    df.format(jsonEntomology.ciValue)));
        }

        private void setSurveyCount(JsonEntomology jsonEntomology, boolean isVillage) {
            surveyCountView.setText(String.format(getString(R.string.survey_count),
                    isVillage ? HOUSE : BUILDING, jsonEntomology.numSurveyedHouses));
        }

        private void setSurveyFoundCount(JsonEntomology jsonEntomology) {
            surveyFoundCountView.setText(String.format(getString(R.string.survey_found_count),
                    jsonEntomology.numFoundHouses));
        }

        private void setSurveyNotFoundCount(JsonEntomology jsonEntomology) {
            surveyNotFoundCountView.setText(String.format(getString(R.string.survey_not_found_count),
                    jsonEntomology.numSurveyedHouses - jsonEntomology.numFoundHouses
                            - jsonEntomology.numNoContainerHouses));
        }

        private void setNoContainerHouseCount(JsonEntomology jsonEntomology) {
            noContainerHousesView.setText(String.format(getString(R.string.no_container_houses),
                    jsonEntomology.numNoContainerHouses));
        }

        private void setSurveyContainerCount(JsonEntomology jsonEntomology) {
            containerCountView.setText(String.format(getString(R.string.survey_container_count),
                    jsonEntomology.numSurveyedContainer, jsonEntomology.numFoundContainers));
        }

        private void setDuplicateSurveyCount(JsonEntomology jsonEntomology) {
            surveyDuplicateView.setText(String.format(getString(R.string.duplicate_survey_count),
                    jsonEntomology.numDuplicateSurvey));
        }

        private void setKeyContainerInfo(JsonEntomology jsonEntomology) {
            indoorContainerLayout.removeAllViews();
            outdoorContainerLayout.removeAllViews();
            for (int index = 0; index < 3; index++) {
                JsonKeyContainer indoorKeyContainer = jsonEntomology.keyContainerIn.get(index);
                JsonKeyContainer outdoorKeyContainer = jsonEntomology.keyContainerOut.get(index);

                if (index == 0 && TextUtils.isEmpty(indoorKeyContainer.containerName)
                        && TextUtils.isEmpty(outdoorKeyContainer.containerName)) {
                    hideKeyContainerLayout();
                } else {
                    setupKeyContainer(indoorKeyContainer, indoorContainerLayout);
                    setupKeyContainer(outdoorKeyContainer, outdoorContainerLayout);
                }
            }
        }

        private void setupKeyContainer(JsonKeyContainer keyContainer,
                                       LinearLayout container) {
            if (keyContainer.containerId == null && keyContainer.containerName == null)
                return;

            String[] sameLevelKeyContainerId = keyContainer.containerId.split(", ");
            String[] sameLevelKeyContainerName = keyContainer.containerName.split(", ");
            int sameLevelSize = sameLevelKeyContainerId.length;

            for (int i = 0; i < sameLevelSize; i++) {
                KeyContainerView keyContainerView = buildKeyContainerView(
                        Integer.valueOf(sameLevelKeyContainerId[i]),
                        sameLevelKeyContainerName[i]);

                if (i == sameLevelSize - 1)
                    keyContainerView.setLayoutParams(addSpace());

                container.addView(keyContainerView);
            }
        }

        private KeyContainerView buildKeyContainerView(int containerId, String containerName) {
            KeyContainerView keyContainerView = new KeyContainerView(getContext());
            keyContainerView.setContainerType(containerId, containerName);
            return keyContainerView;
        }
    }
}
