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
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.GetDataJob;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.SyncJobBuilder;
import th.or.nectec.tanrabad.survey.presenter.view.KeyContainerView;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceTypeRepository;
import th.or.nectec.tanrabad.survey.service.EntomologyRestService;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;
import th.or.nectec.tanrabad.survey.service.json.JsonKeyContainer;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.thai.address.AddressPrinter;

import java.text.DecimalFormat;
import java.util.List;
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
    TextView placeNameView;
    TextView addressView;
    TextView houseIndexView, containerIndexView, breteauIndexView;
    TextView surveyCountView;
    TextView surveyFoundCountView;
    TextView noContainerHousesView;
    TextView containerCountView;
    LinearLayout indoorContainer;
    LinearLayout outdoorContainer;
    ContentLoadingProgressBar progressBar;
    Button gotIt;
    private GetDataJob<JsonEntomology> jsonEntomologyGetDataJob;

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
        containerIndexView = (TextView) view.findViewById(R.id.container_index);
        breteauIndexView = (TextView) view.findViewById(R.id.breteau_index);
        surveyCountView = (TextView) view.findViewById(R.id.survey_count);
        surveyFoundCountView = (TextView) view.findViewById(R.id.survey_found_count);
        noContainerHousesView = (TextView) view.findViewById(R.id.no_container_houses);
        containerCountView = (TextView) view.findViewById(R.id.container_count);
        indoorContainer = (LinearLayout) view.findViewById(R.id.indoor_container);
        outdoorContainer = (LinearLayout) view.findViewById(R.id.outdoor_container);
        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.loading);

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
        AbsJobRunner jobRunner = SyncJobBuilder.build(new SurveyResultJobRunner());
        jsonEntomologyGetDataJob = new GetDataJob<>(new EntomologyRestService(place));
        jobRunner.addJob(jsonEntomologyGetDataJob);
        jobRunner.start();
    }


    private boolean isVillage(JsonEntomology jsonEntomology) {
        return jsonEntomology.placeType == PlaceType.VILLAGE_COMMUNITY;
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
                GetDataJob<JsonEntomology> entomologyJob = (GetDataJob<JsonEntomology>) job;
                List<JsonEntomology> entomologyList = entomologyJob.getData();
                entomology = entomologyList == null ? null : entomologyList.get(0);
            }
        }

        @Override
        protected void onJobStart(Job startingJob) {
            progressBar.setVisibility(View.VISIBLE);
            surveyResultLayout.setVisibility(View.GONE);
            gotIt.setVisibility(View.GONE);
        }

        @Override
        protected void onRunFinish() {
            if (errorJobs() == 0) {
                progressBar.setVisibility(View.GONE);
                surveyResultLayout.setVisibility(View.VISIBLE);
                gotIt.setVisibility(View.VISIBLE);
                updateEntomologyInfo(entomology);
            } else {
                dismiss();
                Alert.highLevel().show(R.string.cannot_view_survey_result);
            }
        }

        private void updateEntomologyInfo(JsonEntomology jsonEntomology) {
            Place place = BrokerPlaceRepository.getInstance().findByUUID(jsonEntomology.placeID);
            placeIconView.setImageResource(PlaceIconMapping.getPlaceIcon(place));
            placeTypeView.setText(new DbPlaceTypeRepository(getContext()).findByID(jsonEntomology.placeType).getName());
            placeNameView.setText(jsonEntomology.placeName);
            addressView.setText(AddressPrinter.print(
                    jsonEntomology.tambonName, jsonEntomology.amphurName, jsonEntomology.provinceName));
            boolean isVillage = isVillage(jsonEntomology);
            setSurveyIndex(jsonEntomology, isVillage);
            setSurveyCount(jsonEntomology, isVillage);
            setSurveyFoundCount(jsonEntomology, isVillage);
            setNoContainerHouseCount(jsonEntomology, isVillage);
            setSurveyContainerCount(jsonEntomology);
            setKeyContainerInfo(jsonEntomology);
        }


        private void setKeyContainerInfo(JsonEntomology jsonEntomology) {
            for (JsonKeyContainer keyContainer : jsonEntomology.keyContainerIn) {
                if (!TextUtils.isEmpty(keyContainer.containerName)) {
                    getFirstOfSameLevelKeyContainer(keyContainer, indoorContainer);
                }
            }
            for (JsonKeyContainer keyContainer : jsonEntomology.keyContainerOut) {
                if (!TextUtils.isEmpty(keyContainer.containerName)) {
                    getFirstOfSameLevelKeyContainer(keyContainer, outdoorContainer);
                }
            }
        }

        private void getFirstOfSameLevelKeyContainer(JsonKeyContainer keyContainer, LinearLayout container) {
            String[] sameLevelKeyContainer = keyContainer.containerName.split(", ");
            container.addView(buildKeyContainerView(sameLevelKeyContainer[0]));
        }

        private KeyContainerView buildKeyContainerView(String containerName) {
            KeyContainerView keyContainerView = new KeyContainerView(getContext());
            keyContainerView.setContainerType(containerName);
            return keyContainerView;
        }

        private void setSurveyContainerCount(JsonEntomology jsonEntomology) {
            containerCountView.setText(String.format(getString(R.string.survey_container_count),
                    jsonEntomology.numSurveyedContainer, jsonEntomology.numFoundContainers));
        }

        private void setNoContainerHouseCount(JsonEntomology jsonEntomology, boolean isVillage) {
            noContainerHousesView.setText(String.format(getString(R.string.no_container_houses),
                    isVillage ? HOUSE : BUILDING, jsonEntomology.numNoContainerHouses));
        }

        private void setSurveyFoundCount(JsonEntomology jsonEntomology, boolean isVillage) {
            surveyFoundCountView.setText(String.format(getString(R.string.survey_found_count),
                    isVillage ? HOUSE : BUILDING, jsonEntomology.numFoundHouses));
        }

        private void setSurveyCount(JsonEntomology jsonEntomology, boolean isVillage) {
            surveyCountView.setText(String.format(getString(R.string.survey_count),
                    isVillage ? HOUSE : BUILDING, jsonEntomology.numSurveyedHouses));
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
                houseIndexView.setVisibility(View.GONE);
                breteauIndexView.setVisibility(View.GONE);
            }
            containerIndexView.setText(String.format(getString(R.string.container_index),
                    df.format(jsonEntomology.ciValue)));

        }
    }
}
