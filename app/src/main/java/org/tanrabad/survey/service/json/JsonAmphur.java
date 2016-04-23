package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.lookup.District;

@JsonObject
public class JsonAmphur {

    @JsonField(name = "amphur_code")
    public String amphurCode;

    @JsonField(name = "amphur_name")
    public String amphurName;

    @JsonField(name = "province_code")
    public String provinceCode;

    @JsonField
    public GeoJsonMultipolygon boundary;

    public District getEntity() {
        District district = new District();
        district.setName(amphurName);
        district.setCode(amphurCode);
        district.setProvinceCode(provinceCode);
        if (boundary != null)
            district.setBoundary(boundary.getEntities());
        return district;
    }
}
