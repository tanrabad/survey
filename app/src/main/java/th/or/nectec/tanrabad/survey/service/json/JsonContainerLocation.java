package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.ContainerLocation;

@JsonObject
public class JsonContainerLocation {

    @JsonField(name = "container_location_id")
    public int containerLocationID;

    @JsonField(name = "container_location_name")
    public String containerLocationName;

    public ContainerLocation getEntity() {
        return new ContainerLocation(containerLocationID, containerLocationName);
    }
}
