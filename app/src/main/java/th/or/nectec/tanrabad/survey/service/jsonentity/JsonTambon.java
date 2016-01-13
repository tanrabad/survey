package th.or.nectec.tanrabad.survey.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.Subdistrict;

@JsonObject
public class JsonTambon {

    @JsonField(name = "tambon_code")
    public String tambonCode;

    @JsonField(name = "tambon_name")
    public String tambonName;

    @JsonField(name = "amphur_code")
    public String amphurCode;

    @JsonField
    public GeoJsonMultipolygon boundary;

    public Subdistrict getEntity() {
        Subdistrict subdistrict = new Subdistrict();
        subdistrict.setName(tambonName);
        subdistrict.setCode(tambonCode);
        subdistrict.setDistrictCode(amphurCode);
        subdistrict.setBoundary(boundary.getEntities());
        return subdistrict;
    }
}
