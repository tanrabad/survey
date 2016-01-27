package th.or.nectec.tanrabad.survey.job;

import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.service.UploadRestService;

import java.util.List;

public class PutDataJob<T> implements Job {

    public static final int ID = 90001;
    UploadRestService<T> uploadRestService;
    private ChangedRepository<T> changedRepository;

    public PutDataJob(ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        this.changedRepository = changedRepository;
        this.uploadRestService = uploadRestService;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() {
        List<T> changedList = changedRepository.getChanged();
        if (changedList == null)
            return;
        for (T t : changedList) {
            if (uploadRestService.put(t))
                changedRepository.markUnchanged(t);
        }
    }
}
