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
import android.database.sqlite.SQLiteOpenHelper;
import org.joda.time.DateTime;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class SurveyCursorMapper implements CursorMapper<Survey> {

    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private SurveyRepository surveyRepository;
    private int idIndex;
    private int buildingIdIndex;
    private int personCountIndex;
    private int latIndex;
    private int lngIndex;
    private int createTimeIndex;
    private int updateTimeIndex;
    private int surveyorIndex;

    public SurveyCursorMapper(Cursor cursor, UserRepository userRepository, BuildingRepository buildingRepository, SurveyRepository surveyRepository) {
        this.userRepository = userRepository;
        this.buildingRepository = buildingRepository;
        this.surveyRepository = surveyRepository;
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(SurveyColumn.ID);
        buildingIdIndex = cursor.getColumnIndex(SurveyColumn.BUILDING_ID);
        personCountIndex = cursor.getColumnIndex(SurveyColumn.PERSON_COUNT);
        latIndex = cursor.getColumnIndex(SurveyColumn.LATITUDE);
        lngIndex = cursor.getColumnIndex(SurveyColumn.LONGITUDE);
        createTimeIndex = cursor.getColumnIndex(SurveyColumn.CREATE_TIME);
        updateTimeIndex = cursor.getColumnIndex(SurveyColumn.UPDATE_TIME);
        surveyorIndex = cursor.getColumnIndex(SurveyColumn.SURVEYOR);
    }

    @Override
    public Survey map(Cursor cursor) {
        UUID surveyID = UUID.fromString(cursor.getString(idIndex));
        User user = new User(cursor.getString(surveyorIndex));
        Building building = buildingRepository.findByUUID(UUID.fromString(cursor.getString(buildingIdIndex)));
        Survey survey = new Survey(surveyID, user, building);
        survey.setLocation(new Location(cursor.getDouble(latIndex), cursor.getDouble(lngIndex)));
        survey.setResidentCount(cursor.getInt(personCountIndex));
        survey.setIndoorDetail(surveyRepository.getSurveyDetail(surveyID, 1));
        survey.setOutdoorDetail(surveyRepository.getSurveyDetail(surveyID, 2));
        survey.setStartTimestamp(new DateTime(cursor.getString(createTimeIndex)));
        survey.setFinishTimestamp(new DateTime(cursor.getString(updateTimeIndex)));
        return survey;
    }
}
