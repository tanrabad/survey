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

package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.survey.ContainerTypeRepository;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbContainerTypeRepository;

import java.util.List;

public final class BrokerContainerTypeRepository implements ContainerTypeRepository {

    private static BrokerContainerTypeRepository instance;
    private ContainerTypeRepository cache;
    private ContainerTypeRepository persistence;


    private BrokerContainerTypeRepository(ContainerTypeRepository cache, ContainerTypeRepository persistence) {
        this.cache = cache;
        this.persistence = persistence;
        cache.updateOrInsert(persistence.find());
    }

    public static BrokerContainerTypeRepository getInstance() {
        if (instance == null)
            instance = new BrokerContainerTypeRepository(InMemoryContainerTypeRepository.getInstance(),
                    new DbContainerTypeRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public List<ContainerType> find() {
        List<ContainerType> containerTypes = cache.find();
        if (containerTypes.isEmpty()) {
            containerTypes = persistence.find();
            cache.updateOrInsert(containerTypes);
        }
        return containerTypes;
    }

    @Override
    public ContainerType findById(int containerTypeId) {
        ContainerType containerType = cache.findById(containerTypeId);
        if (containerType == null) {
            containerType = persistence.findById(containerTypeId);
            cache.save(containerType);
        }
        return containerType;
    }

    @Override
    public boolean save(ContainerType containerType) {
        boolean success = persistence.save(containerType);
        if (success) {
            cache.save(containerType);
        }
        return success;
    }

    @Override
    public boolean update(ContainerType containerType) {
        boolean success = persistence.update(containerType);
        if (success) {
            cache.update(containerType);
        }
        return success;
    }

    @Override
    public boolean delete(ContainerType containerType) {
        boolean success = persistence.delete(containerType);
        if (success) {
            cache.delete(containerType);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<ContainerType> containerTypes) {
        persistence.updateOrInsert(containerTypes);
        cache.updateOrInsert(containerTypes);
    }
}
