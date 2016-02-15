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

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbDistrictRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceSubTypeRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbProvinceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSubdistrictRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;
import th.or.nectec.tanrabad.survey.service.json.JsonKeyContainer;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDatePrinter;
import th.or.nectec.thai.address.AddressPrinter;

import java.text.DecimalFormat;
import java.util.UUID;

public class SurveyResultDialogFragment extends DialogFragment {

    public static final String ARG_PLACE_ID = "place_id";
    public static final String FRAGMENT_TAG = "survey_result";
    public static final String BUILDING = "อาคาร";
    public static final String HOUSE = "บ้าน";
    LinearLayout placeInfoLayout;
    RelativeLayout surveyResultLayout;
    ImageView placeIconView;
    TextView placeTypeView;
    TextView surveyDateView;
    TextView placeNameView;
    TextView addressView;
    TextView houseIndexView;
    TextView containerIndexView;
    TextView breteauIndexView;
    TextView surveyCountView;
    TextView surveyFoundCountView;
    TextView noContainerHousesView;
    TextView containerCountView;
    LinearLayout indoorContainerLayout;
    LinearLayout outdoorContainerLayout;
    ContentLoadingProgressBar progressBar;
    TextView errorMsgView;
    Button gotIt;
    private EntomologyJob jsonEntomologyGetDataJob;

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
        placeInfoLayout = (LinearLayout) view.findViewById(R.id.place_info_layout);
        placeIconView = (ImageView) view.findViewById(R.id.place_icon);
        placeTypeView = (TextView) view.findViewById(R.id.place_type);
        placeNameView = (TextView) view.findViewById(R.id.place_name);
        addressView = (TextView) view.findViewById(R.id.address);
        houseIndexView = (TextView) view.findViewById(R.id.house_index);
        houseIndexView = (TextView) view.findViewById(R.id.house_index);
        containerIndexView = (TextView) view.findViewById(R.id.container_index);
        breteauIndexView = (TextView) view.findViewById(R.id.breteau_index);
        surveyCountView = (TextView) view.findViewById(R.id.survey_count);
        surveyFoundCountView = (TextView) view.findViewById(R.id.survey_found_count);
        noContainerHousesView = (TextView) view.findViewById(R.id.no_container_houses);
        containerCountView = (TextView) view.findViewById(R.id.container_count);
        indoorContainerLayout = (LinearLayout) view.findViewById(R.id.indoor_container);
        outdoorContainerLayout = (LinearLayout) view.findViewById(R.id.outdoor_container);
        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.loading);
        errorMsgView = (TextView) view.findViewById(R.id.error_msg);
        surveyDateView = (TextView) view.findViewById(R.id.survey_date);

        gotIt = (Button) view.findViewById(R.id.got_it);
        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        String placeId = getArguments().getString(ARG_PLACE_ID);
        Place place = BrokerPlaceRepository.getInstance().findByUUID(UUID.fromString(placeId));
        super.onActivityCreated(savedInstanceState);
        setPlaceInfo(place);
        if (InternetConnection.isAvailable(getContext())) {
            startJob(place);
        } else {
            showErrorMessageView();
            errorMsgView.setText(R.string.please_connect_internet_before_view_entomology);
        }
    }

    private void setPlaceInfo(Place place) {
        placeIconView.setImageResource(PlaceIconMapping.getPlaceIcon(place));
        placeTypeView.setText(new DbPlaceSubTypeRepository(getContext()).findByID(place.getSubType()).getName());
        placeNameView.setText(place.getName());
        Subdistrict subdistrict = DbSubdistrictRepository.getInstance().findByCode(place.getSubdistrictCode());
        District district = DbDistrictRepository.getInstance().findByCode(subdistrict.getDistrictCode());
        Province province = DbProvinceRepository.getInstance().findByCode(district.getProvinceCode());
        addressView.setText(AddressPrinter.print(subdistrict.getName(), district.getName(), province.getName()));
    }

    private void startJob(Place place) {
        AbsJobRunner jobRunner = SyncJobBuilder.build(new SurveyResultJobRunner());
        jsonEntomologyGetDataJob = new EntomologyJob(place);
        jobRunner.addJob(jsonEntomologyGetDataJob);
        jobRunner.start();
    }

    private void showErrorMessageView() {
        surveyResultLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorMsgView.setVisibility(View.VISIBLE);
    }

    private boolean isVillage(JsonEntomology jsonEntomology) {
        return jsonEntomology.placeType == PlaceType.VILLAGE_COMMUNITY;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        errorMsgView.setVisibility(View.GONE);
        surveyResultLayout.setVisibility(View.GONE);
        gotIt.setVisibility(View.GONE);
    }

    private void hideKeyContainerLayout() {
        getView().findViewById(R.id.key_container_title).setVisibility(View.GONE);
        getView().findViewById(R.id.indoor_title).setVisibility(View.GONE);
        getView().findViewById(R.id.outdoor_title).setVisibility(View.GONE);
        indoorContainerLayout.setVisibility(View.GONE);
        outdoorContainerLayout.setVisibility(View.GONE);
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
            gotIt.setVisibility(View.VISIBLE);
            if (errorJobs() == 0 && entomology != null) {
                surveyResultLayout.setVisibility(View.VISIBLE);
                updateEntomologyInfo(entomology);
            } else {
                errorMsgView.setVisibility(View.VISIBLE);
                errorMsgView.setText(R.string.cannot_view_survey_result);
            }
        }

        private void updateEntomologyInfo(JsonEntomology jsonEntomology) {
            boolean isVillage = isVillage(jsonEntomology);
            surveyDateView.setText(ThaiDatePrinter.print(jsonEntomology.dateSurveyed));
            setSurveyIndex(jsonEntomology, isVillage);
            setSurveyCount(jsonEntomology, isVillage);
            setSurveyFoundCount(jsonEntomology, isVillage);
            setNoContainerHouseCount(jsonEntomology, isVillage);
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

        private void setSurveyFoundCount(JsonEntomology jsonEntomology, boolean isVillage) {
            surveyFoundCountView.setText(String.format(getString(R.string.survey_found_count),
                    isVillage ? HOUSE : BUILDING, jsonEntomology.numFoundHouses));
        }

        private void setNoContainerHouseCount(JsonEntomology jsonEntomology, boolean isVillage) {
            noContainerHousesView.setText(String.format(getString(R.string.no_container_houses),
                    isVillage ? HOUSE : BUILDING, jsonEntomology.numNoContainerHouses));
        }

        private void setSurveyContainerCount(JsonEntomology jsonEntomology) {
            containerCountView.setText(String.format(getString(R.string.survey_container_count),
                    jsonEntomology.numSurveyedContainer, jsonEntomology.numFoundContainers));
        }

        private void setKeyContainerInfo(JsonEntomology jsonEntomology) {
            for (int index = 0; index < 3; index++) {
                JsonKeyContainer indoorKeyContainer = jsonEntomology.keyContainerIn.get(index);
                JsonKeyContainer outdoorKeyContainer = jsonEntomology.keyContainerOut.get(index);
                if (TextUtils.isEmpty(indoorKeyContainer.containerName)
                        && TextUtils.isEmpty(outdoorKeyContainer.containerName)) {
                    hideKeyContainerLayout();
                } else {
                    getFirstOfSameLevelKeyContainer(indoorKeyContainer, indoorContainerLayout);
                    getFirstOfSameLevelKeyContainer(outdoorKeyContainer, outdoorContainerLayout);
                }
            }
        }

        private void getFirstOfSameLevelKeyContainer(JsonKeyContainer keyContainer, LinearLayout container) {
            if (keyContainer.containerName != null)
                return;
            String[] sameLevelKeyContainer = keyContainer.containerName.split(", ");
            container.addView(buildKeyContainerView(sameLevelKeyContainer[0]));
        }

        private KeyContainerView buildKeyContainerView(String containerName) {
            KeyContainerView keyContainerView = new KeyContainerView(getContext());
            keyContainerView.setContainerType(containerName);
            return keyContainerView;
        }
    }
}
