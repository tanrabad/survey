package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import th.or.nectec.tanrabad.entity.lookup.Subdistrict;

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
        if (boundary != null)
            subdistrict.setBoundary(boundary.getEntities());
        return subdistrict;
    }
}
