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
import th.or.nectec.tanrabad.entity.field.Location;

@JsonObject
public class JsonLocation {

    @JsonField
    public Double latitude;

    @JsonField
    public Double longitude;

    public static JsonLocation parse(Location location) {
        JsonLocation jsonLocation = new JsonLocation();
        jsonLocation.latitude = location.getLatitude();
        jsonLocation.longitude = location.getLongitude();
        return jsonLocation;
    }

    public Location getEntity() {
        return new Location(latitude, longitude);
    }

    @Override
    public String toString() {
        return "{\"latitude\":" + latitude + ",\"longitude\":" + longitude + '}';
    }
}
