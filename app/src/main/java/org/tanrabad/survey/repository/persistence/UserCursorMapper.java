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

import org.tanrabad.survey.domain.organization.OrganizationRepository;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.utils.collection.CursorMapper;

class UserCursorMapper implements CursorMapper<User> {

    private int usernameIndex;
    private int firstNameIndex;
    private int lastNameIndex;
    private int orgIdIndex;
    private int passwordIndex;
    private int emailIndex;
    private int avatarIndex;
    private int phoneNumberIndex;
    private int apiFilterIndex;
    private OrganizationRepository organizationRepository;

    public UserCursorMapper(Cursor cursor, OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        usernameIndex = cursor.getColumnIndex(UserColumn.USERNAME);
        firstNameIndex = cursor.getColumnIndex(UserColumn.FIRSTNAME);
        lastNameIndex = cursor.getColumnIndex(UserColumn.LASTNAME);
        orgIdIndex = cursor.getColumnIndex(UserColumn.ORG_ID);
        passwordIndex = cursor.getColumnIndex(UserColumn.PASSWORD);
        emailIndex = cursor.getColumnIndex(UserColumn.EMAIL);
        avatarIndex = cursor.getColumnIndex(UserColumn.AVATAR_FILENAME);
        phoneNumberIndex = cursor.getColumnIndex(UserColumn.PHONE_NUMBER);
        apiFilterIndex = cursor.getColumnIndex(UserColumn.API_FILTER);
    }

    @Override
    public User map(Cursor cursor) {
        User user = new User(cursor.getString(usernameIndex));
        user.setFirstname(cursor.getString(firstNameIndex));
        user.setLastname(cursor.getString(lastNameIndex));
        user.setPassword(cursor.getString(passwordIndex));
        user.setEmail(cursor.getString(emailIndex));
        user.setPhoneNumber(cursor.getString(phoneNumberIndex));
        user.setAvatarFileName(cursor.getString(avatarIndex));
        int orgId = cursor.getInt(orgIdIndex);
        user.setOrganizationId(orgId);
        user.setHealthRegionCode(organizationRepository.findById(orgId).getHealthRegionCode());
        user.setApiFilter(cursor.getString(apiFilterIndex));
        return user;
    }
}
