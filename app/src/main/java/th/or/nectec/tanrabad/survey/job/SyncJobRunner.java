package th.or.nectec.tanrabad.survey.job;

import android.util.Log;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSurveyRepository;
import th.or.nectec.tanrabad.survey.service.BuildingRestService;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.service.json.SurveyRestService;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class SyncJobRunner extends AbsJobRunner {

    WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(new PlaceRestService(), BrokerPlaceRepository.getInstance());
    WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(new BuildingRestService(), BrokerBuildingRepository.getInstance());
    PostDataJob placePostDataJob = new PostDataJob<>(new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    PostDataJob buildingPostDataJob = new PostDataJob<>(new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    PostDataJob surveyPostDataJob = new PostDataJob<>(new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());
    PutDataJob placePutDataJob = new PutDataJob<>(new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    PutDataJob buildingPutDataJob = new PutDataJob<>(new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    PutDataJob surveyPutDataJob = new PutDataJob<>(new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());

    public SyncJobRunner() {
        addJob(placeUpdateJob);
        addJob(buildingUpdateJob);
        addJob(placePostDataJob);
        addJob(buildingPostDataJob);
        addJob(surveyPostDataJob);
        addJob(placePutDataJob);
        addJob(buildingPutDataJob);
        addJob(surveyPutDataJob);
    }

    @Override
    protected void onJobError(Job errorJob, Exception exception) {
        super.onJobError(errorJob, exception);
        Log.e(errorJob.toString(), exception.getMessage());
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
