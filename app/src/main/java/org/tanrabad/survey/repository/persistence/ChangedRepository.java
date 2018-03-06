package org.tanrabad.survey.repository.persistence;

import java.util.List;

public interface ChangedRepository<T> {
    List<T> getAdd();

    List<T> getChanged();

    boolean markUnchanged(T data);
}
