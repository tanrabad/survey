/*
 * Copyright (c) 2018 NECTEC
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

import org.tanrabad.survey.domain.organization.OrganizationRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Organization;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerOrganizationRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;

public class Authenticator {

    private AuthenticatorPresent presenter;

    public Authenticator(AuthenticatorPresent presenter) {
        this.presenter = presenter;
    }

    private OrganizationRepository organizationRepository;
    private UserRepository userRepository;

    public void request() {
        presenter.startPage();
    }

    public void close() {
        presenter.close();
    }

    public void setAuthenWith(UserProfile profile) {
        UserProfileMapper userProfileMapper = new UserProfileMapper(profile);

        Organization org = userProfileMapper.getOrganization();
        saveOrUpdate(org);

        User user = userProfileMapper.getUser();
        saveOrUpdate(user);
    }


    private void saveOrUpdate(Organization org) {
        organizationRepository = BrokerOrganizationRepository.getInstance();
        Organization organization = organizationRepository.findById(org.getOrganizationId());
        if (organization == null) {
            organizationRepository.save(org);
        } else {
            organizationRepository.update(org);
        }
    }

    private void saveOrUpdate(User user) {
        userRepository = BrokerUserRepository.getInstance();
        User userInRepo = userRepository.findByUsername(user.getUsername());
        if (userInRepo == null) {
            userRepository.save(user);
        } else {
            userRepository.update(user);
        }
    }
}
