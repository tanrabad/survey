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

import java.util.HashMap;
import java.util.List;

import th.or.nectec.tanrabad.domain.organization.OrganizationRepository;
import th.or.nectec.tanrabad.domain.organization.OrganizationRepositoryException;
import th.or.nectec.tanrabad.entity.Organization;

public class InMemoryOrganizationRepository implements OrganizationRepository {
    public static InMemoryOrganizationRepository instance;

    HashMap<Integer, Organization> organizationMapping = new HashMap<>();

    protected static InMemoryOrganizationRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryOrganizationRepository();
        }
        return instance;
    }

    @Override
    public Organization findById(int organizationId) {
        if (organizationMapping.containsKey(organizationId)) {
            return organizationMapping.get(organizationId);
        } else {
            return null;
        }
    }

    @Override
    public boolean save(Organization organization) {
        if (organizationMapping.containsKey(organization.getOrganizationId())) {
            throw new OrganizationRepositoryException();
        }

        organizationMapping.put(organization.getOrganizationId(), organization);
        return true;
    }

    @Override
    public boolean update(Organization organization) {
        if (!organizationMapping.containsKey(organization.getOrganizationId())) {
            throw new OrganizationRepositoryException();
        }
        organizationMapping.put(organization.getOrganizationId(), organization);
        return true;
    }

    @Override
    public void updateOrInsert(List<Organization> organizations) {
    }
}
