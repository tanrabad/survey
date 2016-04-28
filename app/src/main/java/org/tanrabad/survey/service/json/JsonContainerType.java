package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.tanrabad.survey.entity.lookup.ContainerType;

@JsonObject
public class JsonContainerType {

    @JsonField(name = "container_type_id")
    public int containerTypeId;

    @JsonField(name = "container_type_name")
    public String containerTypeName;

    public ContainerType getEntity() {
        return new ContainerType(containerTypeId, containerTypeName);
    }
}