package th.or.nectec.tanrabad.survey.service;

import com.bluelinelabs.logansquare.LoganSquare;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.json.JsonSurvey;

import java.io.IOException;
import java.util.List;

public class SurveyRestService extends AbsUploadRestService<Survey> {

    public static final String PATH = "/survey";

    public SurveyRestService() {
        this(BASE_API, new ServiceLastUpdatePreference(TanrabadApp.getInstance(), PATH));
    }

    public SurveyRestService(String baseApi, ServiceLastUpdate serviceLastUpdate) {
        super(baseApi, serviceLastUpdate);
    }

    @Override
    protected String entityToJsonString(Survey data) throws IOException {
        return LoganSquare.serialize(JsonSurvey.parse(data));
    }

    @Override
    protected String getId(Survey data) {
        return data.getId().toString();
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List jsonToEntityList(String responseBody) {
        throw new RuntimeException("Survey rest service not support convert to entity.");
    }
}
