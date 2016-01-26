package th.or.nectec.tanrabad.survey.service;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ErrorResponse {
    @JsonField
    String response;
    @JsonField
    Message message;

    @JsonObject
    public static class Message {
        @JsonField
        String type;
        @JsonField
        String detail;
    }
}
