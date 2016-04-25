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

package org.tanrabad.survey.service.json;

import org.tanrabad.survey.entity.field.Location;

import java.util.List;

public class JsonPolygon {

    private List<Location> boundary;
    private List<Location>[] holes;

    public JsonPolygon(List<Location> boundary, List<Location>... holes) {
        this.boundary = boundary;
        this.holes = holes;
    }

    public List<Location> getBoundary() {
        return boundary;
    }

    public List<Location> getHole(int holeIndex) {
        return holes[holeIndex];
    }

    public int getHolesCount() {
        return holes == null ? 0 : holes.length;
    }

    public List<Location>[] getAllHoles() {
        return holes;
    }
}
