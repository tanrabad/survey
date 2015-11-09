package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SurveyNewListTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void testHappyPath() throws Exception {
        final ContainerRepository containerRepository = context.mock(ContainerRepository.class);
        final ContainerPresenter containerPresenter = context.mock(ContainerPresenter.class);

        final ArrayList<Container> containers = new ArrayList<>();
        containers.add(new Container(1));
        containers.add(Container.fromId(2));

        context.checking(new Expectations(){{
            allowing(containerRepository).find();
            will(returnValue(containers));
        oneOf(containerPresenter).showContainerList(with(containers));
        }
        });

        ContainerController containerController = new ContainerController(containerRepository, containerPresenter);
        containerController.showContainerList();


    }

    public  interface ContainerRepository{

        List<Container> find();
    }

    public interface ContainerPresenter{

        void showContainerList(List<Container> containers);
    }

    private class ContainerController {
        private ContainerRepository containerRepository;
        private ContainerPresenter containerPresenter;


        public ContainerController(ContainerRepository containerRepository, ContainerPresenter containerPresenter) {

            this.containerRepository = containerRepository;
            this.containerPresenter = containerPresenter;
        }

        public void showContainerList() {
            List<Container> containers = containerRepository.find();
            containerPresenter.showContainerList(containers);

        }
    }
}
