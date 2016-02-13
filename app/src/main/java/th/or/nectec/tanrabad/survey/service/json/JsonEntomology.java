/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

@JsonObject
public class JsonEntomology {
    @JsonField(name = "place_id", typeConverter = UuidTypeConverter.class)
    public UUID placeId;

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
        return "JsonEntomology{"
                + "placeId=" + placeId
                + ", placeType=" + placeType
                + ", placeName='" + placeName + '\''
                + ", location=" + location
                + ", tambonName='" + tambonName + '\''
                + ", amphurName='" + amphurName + '\''
                + ", provinceName='" + provinceName + '\''
                + ", numSurveyedContainer=" + numSurveyedContainer
                + ", numFoundContainers=" + numFoundContainers
                + ", dateSurveyed=" + dateSurveyed
                + ", hiValue=" + hiValue
                + ", biValue=" + biValue
                + ", ciValue=" + ciValue
                + ", keyContainerIn=" + keyContainerIn
                + ", keyContainerOut=" + keyContainerOut
                + '}';
    }
}
