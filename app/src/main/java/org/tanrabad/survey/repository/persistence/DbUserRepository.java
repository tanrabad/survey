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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.tanrabad.survey.domain.organization.OrganizationRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerOrganizationRepository;
import org.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;

public class DbUserRepository extends DbRepository implements UserRepository {

    public static final String TABLE_NAME = "user_profile";
    private OrganizationRepository organizationRepository;

    public DbUserRepository(Context context) {
        this(context, BrokerOrganizationRepository.getInstance());
    }

    public DbUserRepository(Context context, OrganizationRepository organizationRepository) {
        super(context);
        this.organizationRepository = organizationRepository;
    }

    @Override
    public User findByUsername(String userName) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME, UserColumn.wildcard(),
                UserColumn.USERNAME + "=?", new String[]{userName}, null, null, null);
        User user = getUser(cursor);
        db.close();
        return user;
    }

    private User getUser(Cursor cursor) {
        if (cursor.moveToFirst()) {
            User user = getMapper(cursor).map(cursor);
            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<User> getMapper(Cursor cursor) {
        return new UserCursorMapper(cursor, organizationRepository);
    }

    @Override
    public boolean save(User user) {
        ContentValues values = userContentValues(user);
        SQLiteDatabase db = writableDatabase();
        boolean success = saveByContentValues(db, values);
        db.close();
        return success;
    }

    private ContentValues userContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserColumn.USERNAME, user.getUsername());
        values.put(UserColumn.FIRSTNAME, user.getFirstname());
        values.put(UserColumn.LASTNAME, user.getLastname());
        values.put(UserColumn.PASSWORD, user.getPassword());
        values.put(UserColumn.ORG_ID, user.getOrganizationId());
        values.put(UserColumn.AVATAR_FILENAME, user.getAvatarFileName());
        values.put(UserColumn.PHONE_NUMBER, user.getPhoneNumber());
        values.put(UserColumn.EMAIL, user.getEmail());
        values.put(UserColumn.API_FILTER, user.getApiFilter());
        return values;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues user) {
        return db.insert(TABLE_NAME, null, user) != ERROR_INSERT_ID;
    }

    @Override
    public boolean update(User user) {
        ContentValues values = userContentValues(user);
        SQLiteDatabase db = writableDatabase();
        boolean success = updateByContentValues(db, values);
        db.close();
        return success;
    }

    @Override
    public boolean delete(User data) {
        return false;
    }

    @Override
    public void updateOrInsert(List<User> users) {

    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues user) {
        return db.update(TABLE_NAME, user, UserColumn.USERNAME + "=?", new String[]{user.getAsString(UserColumn.USERNAME)}) > 0;
    }
}
