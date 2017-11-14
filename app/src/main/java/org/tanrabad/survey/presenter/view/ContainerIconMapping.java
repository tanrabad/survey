package org.tanrabad.survey.presenter.view;

import android.support.annotation.DrawableRes;
import android.util.SparseIntArray;

import org.tanrabad.survey.R;
import org.tanrabad.survey.entity.lookup.ContainerType;


class ContainerIconMapping {
    private static SparseIntArray containerIconMapper = getIconMapping();

    private static SparseIntArray getIconMapping() {
        SparseIntArray iconMapper = new SparseIntArray();
        iconMapper.put(1, R.mipmap.ic_container_earthen_jar);
        iconMapper.put(2, R.mipmap.ic_container_bottle);
        iconMapper.put(3, R.mipmap.ic_container_vase);
        iconMapper.put(4, R.mipmap.ic_container_ant_tray);
        iconMapper.put(5, R.mipmap.ic_container_pot_saucer);
        iconMapper.put(6, R.mipmap.ic_container_lotus);
        iconMapper.put(7, R.mipmap.ic_container_tire);
        iconMapper.put(8, R.mipmap.ic_container_leaf);
        iconMapper.put(9, R.mipmap.ic_container_garbages);
        iconMapper.put(10, R.mipmap.ic_container_bucket);
        iconMapper.put(11, R.mipmap.ic_container_bowl);
        iconMapper.put(12, R.mipmap.ic_container_fridge);
        return iconMapper;
    }

    @DrawableRes
    static int getContainerIcon(ContainerType containerType) {
        return getContainerIcon(containerType.getId());
    }

    @DrawableRes
    static int getContainerIcon(int containerType) {
        if (containerIconMapper.get(containerType, -1) == -1)
            return R.mipmap.ic_building_home_black;
        return containerIconMapper.get(containerType);
    }

}
