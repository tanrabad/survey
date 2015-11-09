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

import th.or.nectec.tanrabad.domain.ContainerTypeRepository;
import th.or.nectec.tanrabad.entity.ContainerType;

import java.util.ArrayList;
import java.util.List;

public class InMemoryContainerTypeRepository implements ContainerTypeRepository {


    private static InMemoryContainerTypeRepository instance;
    private ArrayList<ContainerType> containerTypes;

    private InMemoryContainerTypeRepository() {
        containerTypes = new ArrayList<>();
        containerTypes.add(new ContainerType(1, "น้ำดื่ม"));
        containerTypes.add(new ContainerType(2, "น้ำใช้"));
        containerTypes.add(new ContainerType(3, "แจกัน"));
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
