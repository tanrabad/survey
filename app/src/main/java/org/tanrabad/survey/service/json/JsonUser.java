package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.tanrabad.survey.entity.User;

@JsonObject
public class JsonUser {
    @JsonField
    public String username;
    @JsonField
    public String firstname;
    @JsonField
    public String lastname;
    @JsonField
    public int organizationId;

    public static JsonUser parse(User user) {
        JsonUser json = new JsonUser();
        json.firstname = user.getFirstname();
        json.lastname = user.getLastname();
        json.username = user.getUsername();
        json.organizationId = user.getOrganizationId();
        return json;
    }
}
