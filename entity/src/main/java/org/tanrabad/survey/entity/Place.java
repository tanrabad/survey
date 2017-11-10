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

package org.tanrabad.survey.entity;

import java.util.UUID;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.utils.WeightEntity;

public class Place extends Entity implements LocationEntity, WeightEntity, Comparable<Place> {

    private final UUID id;
    private String name;
    private int type;
    private int subType;
    private Location location;
    private String subdistrictCode;
    private String updateBy;
    private double weight;

    public Place(UUID id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public static Place withName(String name) {
        UUID uuid = UUID.randomUUID();
        return new Place(uuid, name);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSubdistrictCode() {
        return subdistrictCode;
    }

    public void setSubdistrictCode(String subdistrictCode) {
        this.subdistrictCode = subdistrictCode;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy.getUsername();
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override public double getWeight() {
        return weight;
    }

    @Override public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (type != place.type) return false;
        if (subType != place.subType) return false;
        if (Double.compare(place.weight, weight) != 0) return false;
        if (!id.equals(place.id)) return false;
        if (!name.equals(place.name)) return false;
        return subdistrictCode != null ? subdistrictCode.equals(place.subdistrictCode) : place.subdistrictCode == null;
    }

    @Override public int hashCode() {
        int result;
        result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + type;
        result = 31 * result + subType;
        result = 31 * result + (subdistrictCode != null ? subdistrictCode.hashCode() : 0);
        long temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override public String toString() {
        return "Place{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", type=" + type
                + ", subType=" + subType
                + ", location=" + location
                + ", subdistrictCode='" + subdistrictCode + '\''
                + ", weight=" + weight
                + '}';
    }

    @Override
    public int compareTo(Place that) {
        if (this.subdistrictCode.compareTo(that.subdistrictCode) < 0) {
            return -1;
        } else if (this.subdistrictCode.compareTo(that.subdistrictCode) > 0) {
            return 1;
        }

        if (this.type < that.type) {
            return -1;
        } else if (this.type > that.type) {
            return 1;
        }

        if (this.subType < that.subType) {
            return -1;
        } else if (this.subType > that.subType) {
            return 1;
        }

        if (this.name.compareTo(that.name) < 0) {
            return -1;
        } else if (this.name.compareTo(that.name) > 0) {
            return 1;
        }

        if (this.updateTimestamp.compareTo(that.updateTimestamp) < 0) {
            return -1;
        } else if (this.updateTimestamp.compareTo(that.updateTimestamp) > 0) {
            return 1;
        }
        return 0;
    }
}
