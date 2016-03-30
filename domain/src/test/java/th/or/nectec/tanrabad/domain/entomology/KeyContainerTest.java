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

package th.or.nectec.tanrabad.domain.entomology;

import org.junit.Test;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.stub.ContainerTypeStub;
import th.or.nectec.tanrabad.entity.utils.SurveyBuilder;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class KeyContainerTest {


    @Test
    public void testFindOfOneSurvey() throws Exception {
        Survey survey = new SurveyBuilder().setBuilding(Building.withName("1"))
                .setResident(4)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.น้ำดื่ม, 4, 2)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.ที่รองกันมด, 4, 4)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.จานรองกระถาง, 10, 5)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.ภาชนะที่ไม่ใช้, 3, 3)
                .build();
        KeyContainer keyContainer = new KeyContainer(survey);
        keyContainer.calculate();

        assertEquals(ContainerTypeStub.จานรองกระถาง, keyContainer.indoorNumberOne());
        assertEquals(ContainerTypeStub.ที่รองกันมด, keyContainer.indoorNumberTwo());
        assertEquals(ContainerTypeStub.น้ำดื่ม, keyContainer.indoorNumberThree());
        assertEquals(ContainerTypeStub.ภาชนะที่ไม่ใช้, keyContainer.outdoorNumberOne());
        assertEquals(null, keyContainer.outdoorNumberTwo());
        assertEquals(null, keyContainer.outdoorNumberThree());
    }

    @Test
    public void testFindOfThreeSurvey() throws Exception {
        Survey survey = new SurveyBuilder().setBuilding(Building.withName("1"))
                .setResident(4)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.น้ำดื่ม, 4, 4)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.ที่รองกันมด, 4, 4)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.แจกัน, 5, 5)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.อ่างบัว_ไม้น้ำ, 6, 6)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.น้ำใช้, 3, 3)
                .build();
        Survey survey2 = new SurveyBuilder().setBuilding(Building.withName("1"))
                .setResident(1)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.น้ำดื่ม, 10, 10)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.ที่รองกันมด, 4, 4)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.แจกัน, 10, 10)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.ยางรถยนต์เก่า, 8, 8)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.ภาชนะที่ไม่ใช้, 3, 3)
                .build();
        Survey survey3 = new SurveyBuilder().setBuilding(Building.withName("1"))
                .setResident(5)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.น้ำดื่ม, 2, 2)
                .addIndoorDetail(UUID.randomUUID(), ContainerTypeStub.น้ำใช้, 13, 13)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.จานรองกระถาง, 9, 9)
                .addOutdoorDetail(UUID.randomUUID(), ContainerTypeStub.กากใบพืช, 2, 2)
                .build();

        ArrayList<Survey> surveys = new ArrayList<>();
        surveys.add(survey);
        surveys.add(survey2);
        surveys.add(survey3);
        KeyContainer keyContainer = new KeyContainer(surveys);
        keyContainer.calculate();

        assertEquals(ContainerTypeStub.น้ำดื่ม, keyContainer.indoorNumberOne());
        assertEquals(ContainerTypeStub.แจกัน, keyContainer.indoorNumberTwo());
        assertEquals(ContainerTypeStub.น้ำใช้, keyContainer.indoorNumberThree());
        assertEquals(ContainerTypeStub.จานรองกระถาง, keyContainer.outdoorNumberOne());
        assertEquals(ContainerTypeStub.ยางรถยนต์เก่า, keyContainer.outdoorNumberTwo());
        assertEquals(ContainerTypeStub.อ่างบัว_ไม้น้ำ, keyContainer.outdoorNumberThree());
    }
}
