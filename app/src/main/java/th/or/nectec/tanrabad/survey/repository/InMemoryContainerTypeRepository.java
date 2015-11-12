/*
 * Copyright (c) 2015  NECTEC
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

package th.or.nectec.tanrabad.survey.repository;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.ContainerTypeRepository;
import th.or.nectec.tanrabad.entity.ContainerType;

public class InMemoryContainerTypeRepository implements ContainerTypeRepository {


    private static InMemoryContainerTypeRepository instance;
    private ArrayList<ContainerType> containerTypes;

    private InMemoryContainerTypeRepository() {
        containerTypes = new ArrayList<>();
        containerTypes.add(new ContainerType(1, "น้ำใช้"));
        containerTypes.add(new ContainerType(2, "น้ำดื่ม"));
        containerTypes.add(new ContainerType(3, "แจกัน"));
        containerTypes.add(new ContainerType(4, "ที่รองกันมด"));
        containerTypes.add(new ContainerType(5, "จานรองกระถาง"));
        containerTypes.add(new ContainerType(6, "อ่างบัว/ไม้น้ำ"));
        containerTypes.add(new ContainerType(7, "ยางรถยนต์เก่า"));
        containerTypes.add(new ContainerType(8, "กากใบพืช"));
        containerTypes.add(new ContainerType(9, "ภาชนะที่ไม่ใช้"));
        containerTypes.add(new ContainerType(10, "อื่นๆ(ที่ไม่ใช้ประโยชน์)"));
    }

    public static InMemoryContainerTypeRepository getInstance() {
        if (instance == null)
            instance = new InMemoryContainerTypeRepository();
        return instance;
    }

    @Override
    public List<ContainerType> find() {
        return containerTypes;
    }
}
