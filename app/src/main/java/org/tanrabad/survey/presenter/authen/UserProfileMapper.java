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

package org.tanrabad.survey.presenter.authen;

import org.tanrabad.survey.entity.Organization;
import org.tanrabad.survey.entity.User;
import org.trb.authen.model.UserProfile;

class UserProfileMapper {

    private final UserProfile profile;

    public UserProfileMapper(UserProfile profile) {
        this.profile = profile;
    }

    public User getUser() {
        User user = new User(profile.getUserName());
        user.setFirstname(profile.getFirstName());
        user.setLastname(profile.getLastName());

        Organization organization = getOrganization();
        user.setOrganizationId(organization.getOrganizationId());
        user.setHealthRegionCode(organization.getHealthRegionCode());
        user.setApiFilter(profile.getOrgQueryString());
        return user;
    }

    public Organization getOrganization() {

        Organization org = new Organization(getOrganizationId(),
                profile.getOrgName());
        org.setSubdistrictCode(profile.getOrgTambonCode());
        org.setHealthRegionCode(profile.getOrgHealthRegionCode());
        org.setAddress(profile.getOrgAddress());
        return org;
    }

    private int getOrganizationId() {
        return Integer.parseInt(profile.getOrgId());
    }
}