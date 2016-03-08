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

package th.or.nectec.tanrabad.entity.lookup;

import th.or.nectec.tanrabad.entity.Entity;
import th.or.nectec.tanrabad.entity.ReferenceEntity;

public class PlaceSubType extends Entity implements ReferenceEntity {
    public static final int ชุมชนแออัด = 10;
    public static final int TEMPLE = 13;
    public static final int CHURCH = 14;
    public static final int MOSQUE = 15;
    private int id;
    private String name;
    private int placeTypeId;

    public PlaceSubType(int id, String name, int placeTypeId) {
        this.id = id;
        this.name = name;
        this.placeTypeId = placeTypeId;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getPlaceTypeId() {
        return placeTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceSubType that = (PlaceSubType) o;
        if (id != that.id) return false;
        if (placeTypeId != that.placeTypeId) return false;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return "PlaceSubType{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", placeTypeId=" + placeTypeId
                + '}';
    }
}
