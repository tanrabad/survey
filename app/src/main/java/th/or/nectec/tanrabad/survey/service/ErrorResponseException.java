package th.or.nectec.tanrabad.survey.service;

public class ErrorResponseException extends RuntimeException {
    private ErrorResponse errorResponse;

    public ErrorResponseException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
