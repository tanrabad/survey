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
    WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(
            new PlaceRestService(), BrokerPlaceRepository.getInstance());
    WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(
            new BuildingRestService(), BrokerBuildingRepository.getInstance());
    PostDataJob placePostDataJob = new PostDataJob<>(
            new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    PostDataJob buildingPostDataJob = new PostDataJob<>(
            new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    PostDataJob surveyPostDataJob = new PostDataJob<>(
            new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());
    PutDataJob placePutDataJob = new PutDataJob<>(
            new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    PutDataJob buildingPutDataJob = new PutDataJob<>(
            new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    PutDataJob surveyPutDataJob = new PutDataJob<>(
            new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());

    public static AbsJobRunner build(AbsJobRunner runner) {
        SyncJobBuilder syncJobBuilder = new SyncJobBuilder();
        runner.addJob(syncJobBuilder.placePostDataJob);
        runner.addJob(syncJobBuilder.buildingPostDataJob);
        runner.addJob(syncJobBuilder.surveyPostDataJob);
        runner.addJob(syncJobBuilder.placePutDataJob);
        runner.addJob(syncJobBuilder.buildingPutDataJob);
        runner.addJob(syncJobBuilder.surveyPutDataJob);
        runner.addJob(syncJobBuilder.placeUpdateJob);
        runner.addJob(syncJobBuilder.buildingUpdateJob);
        return runner;
    }
}
