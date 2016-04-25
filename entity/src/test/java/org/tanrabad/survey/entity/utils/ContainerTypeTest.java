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

package org.tanrabad.survey.entity.utils;

import org.junit.Test;
import org.tanrabad.survey.entity.lookup.ContainerType;
import org.tanrabad.survey.entity.stub.ContainerTypeStub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ContainerTypeTest {

    private static final String แจกัน = "แจกัน";
    private static final String น้ำใข้ = "น้ำใช้";
    private static final int ID_น้ำใช้ = 1;

    private final ContainerType containerType = new ContainerType(ID_น้ำใช้, น้ำใข้);

    @Test
    public void testGetName() {
        assertEquals(น้ำใข้, containerType.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(ID_น้ำใช้, containerType.getId());
    }

    @Test
    public void containerWithDifferentNameMustNotEquals() {
        ContainerType otherContainer = new ContainerType(ID_น้ำใช้, แจกัน);

        assertNotEquals(containerType, otherContainer);
    }

    @Test
    public void containerWithDifferentIdMustNotEquals() {
        ContainerType otherContainer = new ContainerType(3, น้ำใข้);

        assertNotEquals(containerType, otherContainer);
    }

    @Test
    public void containerWithTheSameNameAndIdMustEquals() {
        ContainerType otherContainer = new ContainerType(ID_น้ำใช้, น้ำใข้);

        assertEquals(containerType, otherContainer);
    }

    @Test
    public void testCompare() throws Exception {
        assertEquals(0, ContainerTypeStub.น้ำดื่ม.compareTo(ContainerTypeStub.น้ำดื่ม));
    }

    @Test
    public void testCompareLessThen() throws Exception {
        assertEquals(-1, ContainerTypeStub.น้ำใช้.compareTo(ContainerTypeStub.ยางรถยนต์เก่า));
    }

    @Test
    public void testCompareGreaterThen() throws Exception {
        assertEquals(1, ContainerTypeStub.อื่นๆ.compareTo(ContainerTypeStub.น้ำดื่ม));
    }
}
