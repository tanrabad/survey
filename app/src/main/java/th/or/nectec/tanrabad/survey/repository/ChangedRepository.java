package th.or.nectec.tanrabad.survey.repository;

import java.util.List;

public interface ChangedRepository<T> {
    List<T> getAdd();

    List<T> getChanged();
}
