package th.or.nectec.tanrabad.survey.job;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.service.EntomologyRestService;
import th.or.nectec.tanrabad.survey.service.RestService;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;

import java.io.IOException;
import java.util.List;


public class EntomologyJob implements Job {

    public static final int ID = 30000;
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
        List<JsonEntomology> entomologyList = restService.getUpdate();
        data = entomologyList.isEmpty() ? null : entomologyList.get(entomologyList.size() - 1);
    }

    public JsonEntomology getData() {
        return data;
    }
}
