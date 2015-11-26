/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.entity.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import th.or.nectec.tanrabad.entity.Building;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class HouseIndexTest {

    private static final float DELTA = 0.01f;

    @Test
    public void test50PercentHI() throws Exception {
        HouseIndex hi = new HouseIndex();
        hi.addSurvey(SurveyStub.withLarvae(Building.withName("5")));
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("1")));

        assertEquals(50.0f, hi.calculate(), DELTA);
    }

    @Test
    public void test25PercentHI() throws Exception {
        HouseIndex hi = new HouseIndex();
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("1")));
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("12")));
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("123")));
        hi.addSurvey(SurveyStub.withLarvae(Building.withName("5")));

        assertEquals(25.0f, hi.calculate(), DELTA);
    }

    @Test
    public void test0PercentHI() throws Exception {
        HouseIndex hi = new HouseIndex();
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("1")));
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("12")));
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("123")));

        assertEquals(0f, hi.calculate(), DELTA);
    }

    @Test
    public void test33PercentHI() throws Exception {

        HouseIndex hi = new HouseIndex();
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("1")));
        hi.addSurvey(SurveyStub.withoutLarvae(Building.withName("12")));
        hi.addSurvey(SurveyStub.withLarvae(Building.withName("5")));

        assertEquals(33.33f, hi.calculate(), DELTA);
    }

    @Test(expected = IllegalStateException.class)
    public void testCalculateWithoutSurveyMustThrowException() throws Exception {
        new HouseIndex().calculate();
    }
}