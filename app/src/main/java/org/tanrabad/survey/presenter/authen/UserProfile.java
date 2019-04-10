/*
 * Copyright (c) 2018 NECTEC
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

package org.tanrabad.survey.presenter.authen;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.IOException;

@JsonObject
public class UserProfile {

    @JsonField
    String email;
    @JsonField
    public String name;
    @JsonField(name = "given_name")
    String givenName;
    @JsonField
    String orgId;
    @JsonField
    public String orgName;
    @JsonField
    String orgTambonCode;
    @JsonField
    String orgAmphurCode;
    @JsonField
    String orgAddress;
    @JsonField(name = "param")
    String paramStr;
    @JsonField(name = "user_name")
    public String userName;
    @JsonField
    String emailVerified;
    @JsonField
    public String userState;

    public boolean isDefinedEmail() {
        return emailVerified != null;
    }

    public boolean isEmailVerified() {
        return "true".equals(emailVerified);
    }

    public boolean isActive() {
        return "active".equals(userState);
    }

    String getFirstName() {
        return givenName;
    }

    String getLastName() {
        return name.replace(givenName, "").trim();
    }

    Param getParam() {
        try {
            return LoganSquare.parse(paramStr, Param.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserProfile fromJson(String json) throws IOException {
        return LoganSquare.parse(json, UserProfile.class);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", orgId='" + orgId + '\'' +
            ", orgName='" + orgName + '\'' +
            ", paramStr='" + paramStr + '\'' +
            ", userName='" + userName + '\'' +
            '}';
    }
}
