package th.or.nectec.tanrabad.survey;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.Container;
import th.or.nectec.tanrabad.domain.ContainerController;
import th.or.nectec.tanrabad.domain.ContainerPresenter;
import th.or.nectec.tanrabad.domain.ContainerRepository;

public class SurveyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        final TextView containerList = (TextView) findViewById(R.id.container_list);

        ContainerController containerController = new ContainerController(new ContainerRepository() {
            @Override
            public List<Container> find() {
                ArrayList<Container> containers = new ArrayList<>();
                containers.add(new Container(1));
                containers.add(new Container(2));
                containers.add(new Container(3));
                return containers;
            }
        }, new ContainerPresenter() {
            @Override
            public void showContainerList(List<Container> containers) {
                String containerListStr = "";
                for (Container eachContainer : containers) {
                    containerListStr += eachContainer.toString() + "\n";
                }

                containerList.setText(containerListStr);
            }

            @Override
            public void showContainerNotFound() {
                containerList.setText("ไม่เจอรายการภาชนะ");
            }
        });

        containerController.showList();

    }

}
