package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.ContainerType;

@JsonObject
public class JsonContainerType {

    @JsonField(name = "container_type_id")
    public int containerTypeID;

    @JsonField(name = "container_type_name")
    public String containerTypeName;

    @JsonField
    public GeoJsonMultipolygon boundary;

    public ContainerType getEntity() {
        return new ContainerType(containerTypeID, containerTypeName);
    }
}
