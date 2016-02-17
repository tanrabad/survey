package th.or.nectec.tanrabad.survey.job;

import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.service.RestServiceException;
import th.or.nectec.tanrabad.survey.service.UploadRestService;

import java.io.IOException;
import java.util.List;

public class PutDataJob<T> implements Job {

    public static final int ID = 90001;
    UploadRestService<T> uploadRestService;
    private ChangedRepository<T> changedRepository;
    private IOException ioException;
    private RestServiceException restServiceException;
    private int successCount = 0;
    private int ioExceptionCount = 0;
    private int restServiceExceptionCount = 0;

    public PutDataJob(ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        this.changedRepository = changedRepository;
        this.uploadRestService = uploadRestService;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() throws Exception {
        List<T> changedList = changedRepository.getChanged();
        if (changedList == null)
            return;
        for (T t : changedList) {
            try {
                if (uploadRestService.put(t)) {
                    changedRepository.markUnchanged(t);
                    successCount++;
                }
            } catch (IOException exception) {
                ioException = exception;
                ioExceptionCount++;
            } catch (RestServiceException exception) {
                restServiceException = exception;
                restServiceExceptionCount++;
            }
        }

        throwBufferException();
    }

    private void throwBufferException() throws IOException {
        if (ioException != null) {
            throw ioException;
        } else if (restServiceException != null) {
            throw restServiceException;
        }
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getIoExceptionCount() {
        return ioExceptionCount;
    }

    public int getRestServiceExceptionCount() {
        return restServiceExceptionCount;
    }
}
