package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

@JsonObject
public class JsonEntomology {
    @JsonField(name = "place_id", typeConverter = UUIDConverter.class)
    public UUID placeID;

    @JsonField(name = "place_type")
    public int placeType;

    @JsonField(name = "place_name")
    public String placeName;

    @JsonField
    public GeoJsonPoint location;

    @JsonField(name = "tambon_name")
    public String tambonName;

    @JsonField(name = "amphur_name")
    public String amphurName;

    @JsonField(name = "province_name")
    public String provinceName;

    @JsonField(name = "num_surveyed_houses")
    public int numSurveyedHouses;

    @JsonField(name = "num_found_houses")
    public int numFoundHouses;

    @JsonField(name = "num_no_container_houses")
    public int numNoContainerHouses;

    @JsonField(name = "num_surveyed_containers")
    public int numSurveyedContainer;

    @JsonField(name = "num_found_containers")
    public int numFoundContainers;

    @JsonField(name = "date_surveyed", typeConverter = UnixThaiDateTimeConverter.class)
    public DateTime dateSurveyed;

    @JsonField(name = "hi_value")
    public double hiValue;

    @JsonField(name = "bi_value")
    public double biValue;

    @JsonField(name = "ci_value")
    public double ciValue;

    @JsonField(name = "key_container_in")
    public List<JsonKeyContainer> keyContainerIn;

    @JsonField(name = "key_container_out")
    public List<JsonKeyContainer> keyContainerOut;

    @Override
    public String toString() {
        return "JsonEntomology{" +
                "placeID=" + placeID +
                ", placeType=" + placeType +
                ", placeName='" + placeName + '\'' +
                ", location=" + location +
                ", tambonName='" + tambonName + '\'' +
                ", amphurName='" + amphurName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", numSurveyedContainer=" + numSurveyedContainer +
                ", numFoundContainers=" + numFoundContainers +
                ", dateSurveyed=" + dateSurveyed +
                ", hiValue=" + hiValue +
                ", biValue=" + biValue +
                ", ciValue=" + ciValue +
                ", keyContainerIn=" + keyContainerIn +
                ", keyContainerOut=" + keyContainerOut +
                '}';
    }
}
