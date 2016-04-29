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

package org.tanrabad.survey.entity.stub;

import org.tanrabad.survey.entity.lookup.ContainerType;

public class ContainerTypeStub {

    public static final ContainerType น้ำใช้ = new ContainerType(1, "น้ำใช้", 1);
    public static final ContainerType น้ำดื่ม = new ContainerType(2, "น้ำดื่ม", 2);
    public static final ContainerType แจกัน = new ContainerType(3, "แจกัน", 3);
    public static final ContainerType ที่รองกันมด = new ContainerType(4, "ที่รองกันมด", 4);
    public static final ContainerType จานรองกระถาง = new ContainerType(5, "จานรองกระถาง", 5);
    public static final ContainerType อ่างบัว_ไม้น้ำ = new ContainerType(6, "อ่างบัว/ไม้น้ำ", 6);
    public static final ContainerType ยางรถยนต์เก่า = new ContainerType(7, "ยางรถยนต์เก่า", 7);
    public static final ContainerType กากใบพืช = new ContainerType(8, "กากใบพืช", 8);
    public static final ContainerType ภาชนะที่ไม่ใช้ = new ContainerType(9, "ภาชนะที่ไม่ใช้", 9);
    public static final ContainerType อื่นๆ = new ContainerType(10, "อื่นๆ(ที่ไม่ใช้ประโยชน์)", 255);
    public static final ContainerType น้ำเลี้ยงสัตว์ = new ContainerType(12, "น้ำเลี้ยงสัตว์", 11);
    public static final ContainerType ตู้เย็น = new ContainerType(11, "ตู้เย็น", 10);
}
