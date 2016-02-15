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

package th.or.nectec.tanrabad.entity;

import th.or.nectec.tanrabad.entity.field.Location;

import java.util.UUID;

public class Building extends Entity implements LocationEntity, Comparable<Building> {

    private UUID id;
    private String name;
    private UUID placeId;
    private Place place;
    private Location location;
    private String updateBy;

    public Building(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Building withName(String name) {
        UUID uuid = UUID.randomUUID();
        return new Building(uuid, name);
    }

    public UUID getPlaceId() {
        return placeId;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.placeId = place.getId();
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building building = (Building) o;

        if (!id.equals(building.id)) return false;
        if (name != null ? !name.equals(building.name) : building.name != null) return false;
        return !(place != null ? !place.equals(building.place) : building.place != null);
    }


    @Override
    public String toString() {
        return "Building{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", place=" + place
                + '}';
    }

    @Override
    public int compareTo(Building that) {
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
