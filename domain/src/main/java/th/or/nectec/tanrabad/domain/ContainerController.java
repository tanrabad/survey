package th.or.nectec.tanrabad.domain;

import java.util.List;

class ContainerController {
    private ContainerRepository containerRepository;
    private ContainerPresenter containerPresenter;


    public ContainerController(ContainerRepository containerRepository, ContainerPresenter containerPresenter) {

        this.containerRepository = containerRepository;
        this.containerPresenter = containerPresenter;
    }

    public void showList() {
        List<Container> containers = containerRepository.find();

        if (containers == null) {
            containerPresenter.showContainerNotFound();
        } else {
            containerPresenter.showContainerList(containers);
        }
    }
}
