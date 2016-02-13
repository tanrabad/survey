package th.or.nectec.tanrabad.survey.presenter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.GetDataJob;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.SyncJobBuilder;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceTypeRepository;
import th.or.nectec.tanrabad.survey.service.EntomologyRestService;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.thai.address.AddressPrinter;

import java.text.DecimalFormat;
import java.util.UUID;

public class SurveyResultDialogFragment extends DialogFragment {

    public static final String ARG_PLACE_ID = "place_id";
    public static final String FRAGMENT_TAG = "survey_result";
    public static final String BUILDING = "อาคาร";
    public static final String HOUSE = "บ้าน";
    LinearLayout placeInfoLayout;
    ImageView placeIconView;
    TextView placeTypeView;
    TextView placeNameView;
    TextView addressView;
    TextView indexInfoView;
    TextView surveyCountView;
    TextView surveyFoundCountView;
    TextView noContainerHousesView;
    TextView containerCountView;
    LinearLayout indoorContainer;
    LinearLayout outdoorContainer;
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
        return view;
    }

    private void assignViews(View view) {
        placeInfoLayout = (LinearLayout) view.findViewById(R.id.place_info_layout);
        placeIconView = (ImageView) view.findViewById(R.id.place_icon);
        placeTypeView = (TextView) view.findViewById(R.id.place_type);
        placeNameView = (TextView) view.findViewById(R.id.place_name);
        addressView = (TextView) view.findViewById(R.id.address);
        indexInfoView = (TextView) view.findViewById(R.id.index_info);
        surveyCountView = (TextView) view.findViewById(R.id.survey_count);
        surveyFoundCountView = (TextView) view.findViewById(R.id.survey_found_count);
        noContainerHousesView = (TextView) view.findViewById(R.id.no_container_houses);
        containerCountView = (TextView) view.findViewById(R.id.container_count);
        indoorContainer = (LinearLayout) view.findViewById(R.id.indoor_container);
        outdoorContainer = (LinearLayout) view.findViewById(R.id.outdoor_container);
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

    private void updateEntomologyInfo(JsonEntomology jsonEntomology) {
        DecimalFormat df = new DecimalFormat("#.##");
        Log.e("ento", jsonEntomology.toString());
        Place place = BrokerPlaceRepository.getInstance().findByUUID(jsonEntomology.placeID);
        placeIconView.setImageResource(PlaceIconMapping.getPlaceIcon(place));
        placeTypeView.setText(new DbPlaceTypeRepository(getContext()).findByID(jsonEntomology.placeType).getName());
        placeNameView.setText(jsonEntomology.placeName);
        addressView.setText(AddressPrinter.print(
                jsonEntomology.tambonName, jsonEntomology.amphurName, jsonEntomology.provinceName));
        boolean isVillage = isVillage(jsonEntomology);
        String indexInfo = isVillage
                ? String.format(getString(R.string.house_survey_index),
                df.format(jsonEntomology.hiValue), df.format(jsonEntomology.ciValue), df.format(jsonEntomology.biValue))
                : String.format(getString(R.string.place_survey_index),
                df.format(jsonEntomology.hiValue), df.format(jsonEntomology.ciValue));
        indexInfoView.setText(indexInfo);
        surveyCountView.setText(String.format(getString(R.string.survey_count),
                isVillage ? HOUSE : BUILDING, jsonEntomology.numSurveyedHouses));
        surveyFoundCountView.setText(String.format(getString(R.string.survey_found_count),
                isVillage ? HOUSE : BUILDING, jsonEntomology.numFoundHouses));
        noContainerHousesView.setText(String.format(getString(R.string.no_container_houses),
                isVillage ? HOUSE : BUILDING, jsonEntomology.numNoContainerHouses));
        containerCountView.setText(String.format(getString(R.string.survey_container_count),
                jsonEntomology.numSurveyedContainer, jsonEntomology.numFoundContainers));
    }

    private boolean isVillage(JsonEntomology jsonEntomology) {
        return jsonEntomology.placeType == Place.TYPE_VILLAGE_COMMUNITY;
    }

    public class SurveyResultJobRunner extends AbsJobRunner {

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
                updateEntomologyInfo(entomologyJob.getData().get(0));
            }
        }

        @Override
        protected void onJobStart(Job startingJob) {
        }

        @Override
        protected void onRunFinish() {
            if (errorJobs() == 0) {
                Alert.mediumLevel().show("ปรับปรุงข้อมูลเรียบร้อยแล้วนะ");
            } else {
                Alert.mediumLevel().show("ปรับปรุงข้อมูลไม่สำเร็จ ลองใหม่อีกครั้งนะ");
            }
        }
    }
}
