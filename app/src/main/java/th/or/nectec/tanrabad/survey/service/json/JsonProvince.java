package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.Province;

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
        province.setBoundary(boundary.getEntities());
        return province;
    }
}
