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

package th.or.nectec.tanrabad.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ContainerTypeTest {

    private final int containerTypeId1 = 1;
    private final String containerTypeName1 = "น้ำใช้";
    private final ContainerType containerType1 = new ContainerType(containerTypeId1, containerTypeName1);

    @Test
    public void testGetName() {
        assertEquals(containerTypeName1, containerType1.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(containerTypeId1, containerType1.getId());
    }

    @Test
    public void containerWithDifferentNameMustNotEquals() {
        ContainerType containerType2 = new ContainerType(containerTypeId1, "แจกัน");
        assertNotEquals(containerType1, containerType2);
    }

    @Test
    public void containerWithDifferentIdMustNotEquals() {
        ContainerType containerType2 = new ContainerType(3, containerTypeName1);
        assertNotEquals(containerType1, containerType2);
    }

    @Test
    public void containerWithTheSameNameAndIdMustEquals() {
        ContainerType containerType2 = new ContainerType(containerTypeId1, containerTypeName1);
        assertEquals(containerType1, containerType2);

    }
}