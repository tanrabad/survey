package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class JsonKeyContainer {

    @JsonField
    public int rank;

    @JsonField(name = "container_name")
    public String containerName;

    @Override
    public String toString() {
        return "JsonKeyContainer{" +
                "rank=" + rank +
                ", containerName='" + containerName + '\'' +
                '}';
    }
}
