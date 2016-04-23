package org.tanrabad.survey.job;

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.repository.persistence.DbBuildingRepository;
import org.tanrabad.survey.repository.persistence.DbPlaceRepository;
import org.tanrabad.survey.repository.persistence.DbSurveyRepository;
import org.tanrabad.survey.service.BuildingRestService;
import org.tanrabad.survey.service.PlaceRestService;
import org.tanrabad.survey.service.SurveyRestService;

import java.util.ArrayList;
import java.util.List;

public class UploadJobBuilder {
    public static final int PLACE_POST_ID = 101;
    public static final int BUILDING_POST_ID = 102;
    public static final int SURVEY_POST_ID = 103;
    public static final int PLACE_PUT_ID = 201;
    public static final int BUILDING_PUT_ID = 202;
    public static final int SURVEY_PUT_ID = 203;

    public PostDataJob placePostDataJob = new PostDataJob<>(
            PLACE_POST_ID, new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    public PostDataJob buildingPostDataJob = new PostDataJob<>(
            BUILDING_POST_ID, new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    public PostDataJob surveyPostDataJob = new PostDataJob<>(
            SURVEY_POST_ID, new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());
    public PutDataJob placePutDataJob = new PutDataJob<>(
            PLACE_PUT_ID, new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
    public PutDataJob buildingPutDataJob = new PutDataJob<>(
            BUILDING_PUT_ID, new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
    public PutDataJob surveyPutDataJob = new PutDataJob<>(
            SURVEY_PUT_ID, new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());

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
