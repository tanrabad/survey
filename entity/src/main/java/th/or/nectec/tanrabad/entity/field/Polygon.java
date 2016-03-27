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

package th.or.nectec.tanrabad.entity.field;

import java.util.Arrays;
import java.util.List;

public class Polygon {
    private final List<Location> boundary;
    private final List<Location>[] holes;

    public Polygon(List<Location> boundary, List<Location>[] holes) {
        this.boundary = boundary;
        this.holes = holes;
    }

    public List<Location> getBoundary() {
        return boundary;
    }

    public List<Location> getHole(int holeIndex) {
        return holes[holeIndex];
    }

    public List<Location>[] getAllHoles() {
        return holes;
    }

    public int getHolesCount() {
        return holes.length;
    }

    @Override
    public int hashCode() {
        int result = boundary != null ? boundary.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(holes);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polygon polygon = (Polygon) o;
        return boundary.equals(polygon.boundary)
                && Arrays.equals(holes, polygon.holes);
    }

    @Override
    public String toString() {
        return "Polygon{"
                + "boundary=" + boundary
                + ", holes=" + Arrays.toString(holes)
                + '}';
    }
}
