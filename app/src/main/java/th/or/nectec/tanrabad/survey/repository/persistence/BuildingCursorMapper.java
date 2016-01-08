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

package th.or.nectec.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.UUID;

class BuildingCursorMapper implements CursorMapper<Building> {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private int idIndex;
    private int nameIndex;
    private int latIndex;
    private int lngIndex;
    private int updateByIndex;
    private int placeId;

    public BuildingCursorMapper(Cursor cursor, UserRepository userRepository, PlaceRepository placeRepository) {
        this.userRepository = userRepository;
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
    }

    @Override
    public Building map(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(idIndex));
        Building building = new Building(uuid, cursor.getString(nameIndex));
        building.setLocation(new Location(cursor.getDouble(latIndex), cursor.getDouble(lngIndex)));
        building.setUpdateBy(userRepository.findUserByName(cursor.getString(updateByIndex)));
        building.setPlace(placeRepository.findPlaceByUUID(UUID.fromString(cursor.getString(placeId))));
        return building;
    }


}
