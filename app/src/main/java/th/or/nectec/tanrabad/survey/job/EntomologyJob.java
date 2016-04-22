package th.or.nectec.tanrabad.survey.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.service.EntomologyRestService;
import th.or.nectec.tanrabad.survey.service.RestService;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;


public class EntomologyJob implements Job {

    private static final int ID = 30000;
    private RestService<JsonEntomology> restService;
    private JsonEntomology data;

    public EntomologyJob(Place place) {
        this.restService = new EntomologyRestService(place);
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() throws IOException {
        List<JsonEntomology> entomologyList = new ArrayList<>();
        do {
            entomologyList.addAll(restService.getUpdate());
        } while (restService.hasNextRequest());
        data = entomologyList.isEmpty() ? null : entomologyList.get(entomologyList.size() - 1);
    }

    public JsonEntomology getData() {
        return data;
    }
}
