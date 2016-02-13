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

public class PlaceType extends Entity {

    public static final int VILLAGE_COMMUNITY = 1;
    public static final int WORSHIP = 2;
    public static final int SCHOOL = 3;
    public static final int HOSPITAL = 4;
    public static final int FACTORY = 5;
    private int id;
    private String name;

    public PlaceType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceType placeType = (PlaceType) o;

        if (id != placeType.id) return false;
        return name != null ? name.equals(placeType.name) : placeType.name == null;
    }
}
