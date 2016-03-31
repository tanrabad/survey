package th.or.nectec.tanrabad.survey.job;

import java.io.IOException;
import java.util.List;

import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.service.UploadRestService;

public class PutDataJob<T> extends UploadJob<T> {

    public PutDataJob(int jobId, ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        super(jobId, changedRepository, uploadRestService);
    }

    @Override
    public boolean uploadData(UploadRestService<T> uploadRestService, T data) throws IOException {
        return uploadRestService.put(data);
    }

    @Override
    public List<T> getUpdatedData(ChangedRepository changedRepository) {
        return changedRepository.getChanged();
    }
}
