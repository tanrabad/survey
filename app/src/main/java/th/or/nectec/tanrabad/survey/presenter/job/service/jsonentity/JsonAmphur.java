package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.District;

@JsonObject
public class JsonAmphur {

    @JsonField(name = "amphur_code")
    public String amphurCode;

    @JsonField(name = "amphur_name")
    public String amphurName;

    @JsonField(name = "province_code")
    public String provinceCode;

    @JsonField(name = "province_name")
    public String provinceName;

    @JsonField
    public GeoJsonMultipolygon boundary;

    public District getEntity() {
        District subdistrict = new District();
        subdistrict.setName(amphurName);
        subdistrict.setCode(amphurCode);
        subdistrict.setProvinceName(provinceName);
        subdistrict.setProvinceCode(provinceCode);
        subdistrict.setBoundary(boundary.getEntities());
        return subdistrict;
    }
}
