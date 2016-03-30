package th.or.nectec.tanrabad.survey.job;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSurveyRepository;
import th.or.nectec.tanrabad.survey.service.BuildingRestService;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.service.SurveyRestService;

public class UploadJobBuilder {
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

    public List<Job> getJobs() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(placePostDataJob);
        jobs.add(buildingPostDataJob);
        jobs.add(surveyPostDataJob);
        jobs.add(placePutDataJob);
        jobs.add(buildingPutDataJob);
        jobs.add(surveyPutDataJob);
        return jobs;
    }
}
