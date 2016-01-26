package th.or.nectec.tanrabad.survey.job;

import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.service.UploadRestService;

import java.util.List;

public class PostDataJob<T> implements Job {

    private ChangedRepository<T> changedRepository;
    private UploadRestService<T> uploadRestService;

    public PostDataJob(ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        this.changedRepository = changedRepository;
        this.uploadRestService = uploadRestService;
    }

    @Override
    public int id() {
        return 90000;
    }

    @Override
    public void execute() throws JobException {
        List<T> addList = changedRepository.getAdd();
        if (addList == null)
            return;
        for (T eachData : addList) {
            if (uploadRestService.postData(eachData))
                changedRepository.markUnchanged(eachData);
        }
    }
}
