package th.or.nectec.tanrabad.survey.presenter;

import java.util.HashMap;

import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.survey.R;

public class ContainerIconMapping {
    HashMap<Integer, Integer> containerIconMapper = new HashMap<>();

    public ContainerIconMapping() {
        containerIconMapper.put(1, R.mipmap.jar);
        containerIconMapper.put(2, R.mipmap.bottle24);
        containerIconMapper.put(3, R.mipmap.glass37);
        containerIconMapper.put(4, R.mipmap.ant_tray);
        containerIconMapper.put(5, R.mipmap.ornament84);
        containerIconMapper.put(6, R.mipmap.lotus);
        containerIconMapper.put(7, R.mipmap.vehicle40);
        containerIconMapper.put(8, R.mipmap.leaf1);
        containerIconMapper.put(9, R.mipmap.garbages);
        containerIconMapper.put(10, R.mipmap.bowl3);
    }

    public int getContainerIcon(ContainerType containerType) {
        return containerIconMapper.get(containerType.getId());
    }
}