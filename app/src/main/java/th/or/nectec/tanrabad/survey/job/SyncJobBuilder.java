package th.or.nectec.tanrabad.survey.job;

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
import th.or.nectec.tanrabad.survey.service.SurveyRestService;

public class SyncJobBuilder {
    public WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(
            new PlaceRestService(), BrokerPlaceRepository.getInstance());
    public WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(
            new BuildingRestService(), BrokerBuildingRepository.getInstance());
    public PostDataJob placePostDataJob = new PostDataJob<>(
            new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    public PostDataJob buildingPostDataJob = new PostDataJob<>(
            new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    public PostDataJob surveyPostDataJob = new PostDataJob<>(
            new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());
    public PutDataJob placePutDataJob = new PutDataJob<>(
            new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    public PutDataJob buildingPutDataJob = new PutDataJob<>(
            new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    public PutDataJob surveyPutDataJob = new PutDataJob<>(
            new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());

    public AbsJobRunner build(AbsJobRunner runner) {
        runner.addJob(placePostDataJob);
        runner.addJob(buildingPostDataJob);
        runner.addJob(surveyPostDataJob);
        runner.addJob(placePutDataJob);
        runner.addJob(buildingPutDataJob);
        runner.addJob(surveyPutDataJob);
        runner.addJob(placeUpdateJob);
        runner.addJob(buildingUpdateJob);
        return runner;
    }
}
