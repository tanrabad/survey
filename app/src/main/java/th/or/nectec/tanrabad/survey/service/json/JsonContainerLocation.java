package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import th.or.nectec.tanrabad.entity.lookup.ContainerLocation;

@JsonObject
public class JsonContainerLocation {

    @JsonField(name = "container_location_id")
    public int containerLocationId;

    @JsonField(name = "container_location_name")
    public String containerLocationName;

    public ContainerLocation getEntity() {
        return new ContainerLocation(containerLocationId, containerLocationName);
    }
}
