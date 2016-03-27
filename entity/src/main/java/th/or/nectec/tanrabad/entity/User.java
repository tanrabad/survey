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

package th.or.nectec.tanrabad.entity;

public class User {
    private final String username;
    private String firstname;
    private String lastname;
    private String password;
    private String phoneNumber;
    private String email;
    private String avatarFileName;
    private String healthRegionCode;
    private int organizationId;
    private String apiFilter;

    public User(String username) {
        this.username = username;
    }

    public static User fromUsername(String username) {
        return new User(username);
    }

    public UserType getUserType() {
        return UserType.RESEARCH;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getHealthRegionCode() {
        return healthRegionCode;
    }

    public void setHealthRegionCode(String healthRegionCode) {
        this.healthRegionCode = healthRegionCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarFileName() {
        return avatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    public String getUsername() {
        return username;
    }

    public String getApiFilter() {
        return apiFilter;
    }

    public void setApiFilter(String apiFilter) {
        this.apiFilter = apiFilter;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + organizationId;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        if (organizationId != user.organizationId) return false;
        if (!username.equals(user.username)) return false;
        if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null)
            return false;
        return !(lastname != null ? !lastname.equals(user.lastname) : user.lastname != null);
    }

    public enum UserType {
        OPERATION("_operation"),
        RESEARCH("_research");

        private final String typeName;

        UserType(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName;
        }
    }

}
