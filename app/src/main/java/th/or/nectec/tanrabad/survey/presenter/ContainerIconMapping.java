package th.or.nectec.tanrabad.survey.presenter;

import java.util.HashMap;

import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.survey.R;

public class ContainerIconMapping {
    HashMap<Integer, Integer> containerIconMapper = new HashMap<>();

    public ContainerIconMapping() {
        containerIconMapper.put(1, R.drawable.jar);
        containerIconMapper.put(2, R.drawable.bottle24);
        containerIconMapper.put(3, R.drawable.glass37);
        containerIconMapper.put(4, R.drawable.container1);
        containerIconMapper.put(5, R.drawable.ornament84);
        containerIconMapper.put(6, R.drawable.lotus);
        containerIconMapper.put(7, R.drawable.vehicle40);
        containerIconMapper.put(8, R.drawable.leaf1);
        containerIconMapper.put(9, R.drawable.garbage);
        containerIconMapper.put(10, R.drawable.bowl3);
    }

    public int getContainerIcon(ContainerType containerType) {
        return containerIconMapper.get(containerType.getId());
    }
}