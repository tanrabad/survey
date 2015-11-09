package th.or.nectec.tanrabad.domain;

import java.util.List;

public interface ContainerPresenter {

    void showContainerList(List<Container> containers);

    void showContainerNotFound();
}
