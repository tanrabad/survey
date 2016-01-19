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

package th.or.nectec.tanrabad.domain.building;

import th.or.nectec.tanrabad.domain.WritableRepository;
import th.or.nectec.tanrabad.entity.Building;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends WritableRepository<Building> {
    List<Building> findByPlaceUUID(UUID placeUuid);

    List<Building> findByPlaceUUIDAndBuildingName(UUID placeUUID, String buildingName);

    Building findByUUID(UUID uuid);
}
