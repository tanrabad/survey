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
import org.tanrabad.survey.utils.collection.CursorMapper;
import th.or.nectec.tanrabad.domain.survey.ContainerTypeRepository;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import java.util.UUID;

class SurveyDetailCursorMapper implements CursorMapper<SurveyDetail> {

    private int idIndex;
    private int containerTypeIdIndex;
    private int containerCountIndex;
    private int containerHaveLarvaIndex;
    private ContainerTypeRepository containerTypeRepository;

    public SurveyDetailCursorMapper(Cursor cursor, ContainerTypeRepository containerTypeRepository) {
        this.containerTypeRepository = containerTypeRepository;
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(SurveyDetailColumn.ID);
        containerTypeIdIndex = cursor.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID);
        containerCountIndex = cursor.getColumnIndex(SurveyDetailColumn.CONTAINER_COUNT);
        containerHaveLarvaIndex = cursor.getColumnIndex(SurveyDetailColumn.CONTAINER_HAVE_LARVA);
    }

    @Override
    public SurveyDetail map(Cursor cursor) {
        UUID surveyDetailId = UUID.fromString(cursor.getString(idIndex));
        ContainerType containerType = containerTypeRepository.findById(cursor.getInt(containerTypeIdIndex));
        int totalContainer = cursor.getInt(containerCountIndex);
        int larvaFoundContainer = cursor.getInt(containerHaveLarvaIndex);
        return new SurveyDetail(surveyDetailId, containerType, totalContainer, larvaFoundContainer);
    }
}
