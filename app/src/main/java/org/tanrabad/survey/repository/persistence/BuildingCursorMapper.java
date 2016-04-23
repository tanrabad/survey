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

package org.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import org.tanrabad.survey.utils.collection.CursorMapper;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.field.Location;

import java.util.UUID;

class BuildingCursorMapper implements CursorMapper<Building> {
    private final PlaceRepository placeRepository;
    private int idIndex;
    private int nameIndex;
    private int latIndex;
    private int lngIndex;
    private int updateByIndex;
    private int updateTimeIndex;
    private int changedStatusIndex;
    private int placeId;

    public BuildingCursorMapper(Cursor cursor, PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(BuildingColumn.ID);
        nameIndex = cursor.getColumnIndex(BuildingColumn.NAME);
        latIndex = cursor.getColumnIndex(BuildingColumn.LATITUDE);
        lngIndex = cursor.getColumnIndex(BuildingColumn.LONGITUDE);
        placeId = cursor.getColumnIndex(BuildingColumn.PLACE_ID);
        updateByIndex = cursor.getColumnIndex(BuildingColumn.UPDATE_BY);
        updateTimeIndex = cursor.getColumnIndex(BuildingColumn.UPDATE_TIME);
        changedStatusIndex = cursor.getColumnIndex(BuildingColumn.CHANGED_STATUS);
    }

    @Override
    public BuildingWithChange map(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(idIndex));
        BuildingWithChange building = new BuildingWithChange(
                uuid, cursor.getString(nameIndex), cursor.getInt(changedStatusIndex));
        building.setLocation(new Location(cursor.getDouble(latIndex), cursor.getDouble(lngIndex)));
        building.setUpdateBy(cursor.getString(updateByIndex));
        building.setPlace(placeRepository.findByUuid(UUID.fromString(cursor.getString(placeId))));
        building.setUpdateTimestamp(cursor.getString(updateTimeIndex));
        return building;
    }
}
