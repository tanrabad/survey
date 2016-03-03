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

package th.or.nectec.tanrabad.survey.repository.persistence;

import android.database.Cursor;

import th.or.nectec.tanrabad.entity.Organization;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

class OrganizationCursorMapper implements CursorMapper<Organization> {

    private int idIndex;
    private int nameIndex;
    private int addressIndex;
    private int subdistrictCodeIndex;
    private int hrCodeIndex;

    public OrganizationCursorMapper(Cursor cursor) {
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(OrganizationColumn.ID);
        nameIndex = cursor.getColumnIndex(OrganizationColumn.NAME);
        addressIndex = cursor.getColumnIndex(OrganizationColumn.ADDRESS);
        subdistrictCodeIndex = cursor.getColumnIndex(OrganizationColumn.SUBDISTRICT_CODE);
        hrCodeIndex = cursor.getColumnIndex(OrganizationColumn.HEALTH_REGION_CODE);
    }

    @Override
    public Organization map(Cursor cursor) {
        Organization organization = new Organization(cursor.getInt(idIndex), cursor.getString(nameIndex));
        organization.setAddress(cursor.getString(addressIndex));
        organization.setSubdistrictCode(cursor.getString(subdistrictCodeIndex));
        organization.setHealthRegionCode(cursor.getString(hrCodeIndex));
        return organization;
    }
}
