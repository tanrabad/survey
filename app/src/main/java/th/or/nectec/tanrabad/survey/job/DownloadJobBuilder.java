package th.or.nectec.tanrabad.survey.job;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.service.BuildingRestService;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;

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
