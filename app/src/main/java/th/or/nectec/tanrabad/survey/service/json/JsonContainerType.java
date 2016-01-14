package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.ContainerType;

@JsonObject
public class JsonContainerType {

    @JsonField(name = "container_type_id")
    public int containerTypeID;

    @JsonField(name = "container_type_name")
    public String containerTypeName;

    public ContainerType getEntity() {
        return new ContainerType(containerTypeID, containerTypeName);
    }
}
