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

class UserProfileMapper {

    private final UserProfile profile;

    public UserProfileMapper(UserProfile profile) {
        this.profile = profile;
    }

    public User getUser() {
        User user = new User(profile.userName);
        user.setFirstname(profile.getFirstName());
        user.setLastname(profile.getLastName());

        Organization organization = getOrganization();
        user.setOrganization(organization);
        user.setApiFilter(profile.getParam().orgQueryString);
        return user;
    }

    public Organization getOrganization() {
        Organization org = new Organization(Integer.parseInt(profile.orgId), profile.orgName);
        org.setSubdistrictCode(profile.orgTambonCode);
        org.setHealthRegionCode(profile.getParam().orgHealthRegionCode);
        org.setAddress(profile.orgAddress);
        return org;
    }

}
