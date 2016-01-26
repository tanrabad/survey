package th.or.nectec.tanrabad.survey.service;

public interface UploadRestService<T> {
    boolean postData(T data);
}
