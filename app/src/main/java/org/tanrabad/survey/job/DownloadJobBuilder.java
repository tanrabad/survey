package org.tanrabad.survey.job;

import org.tanrabad.survey.repository.BrokerBuildingRepository;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.service.BuildingRestService;
import org.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;

import java.util.ArrayList;
import java.util.List;

public class DownloadJobBuilder {
    private WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(
            new PlaceRestService(), BrokerPlaceRepository.getInstance());
    private WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(
            new BuildingRestService(), BrokerBuildingRepository.getInstance());

    public List<Job> getJobs() {
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(placeUpdateJob);
        jobs.add(buildingUpdateJob);
        return jobs;
    }
}
