package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.tanrabad.survey.entity.lookup.Province;

@JsonObject
public class JsonProvince {

    @JsonField(name = "province_code")
    public String provinceCode;

    @JsonField(name = "province_name")
    public String provinceName;

    @JsonField
    public GeoJsonMultipolygon boundary;

    public Province getEntity() {
        Province province = new Province();
        province.setName(provinceName);
        province.setCode(provinceCode);
        if (boundary != null)
            province.setBoundary(boundary.getEntities());
        return province;
    }
}
