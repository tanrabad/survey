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

package th.or.nectec.tanrabad.entity.lookup;

public class ContainerType implements Comparable<ContainerType> {

    private final int id;
    private final String name;

    public ContainerType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ContainerType)) return false;
        ContainerType that = (ContainerType) other;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public String toString() {
        return "ContainerType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(ContainerType o) {
        if (id > o.getId()) return 1;
        else if (id < o.getId()) return -1;
        else return 0;
    }

    public int getId() {
        return id;
    }
}
