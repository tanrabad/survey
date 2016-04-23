package org.tanrabad.survey.presenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import th.or.nectec.tanrabad.entity.stub.ContainerTypeStub;
import th.or.nectec.tanrabad.survey.R;

public class KeyContainerView extends RelativeLayout {
    private ImageView containerIcon;
    private TextView containerType;

    public KeyContainerView(Context context) {
        this(context, null);
    }

    public KeyContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_key_container, this);
    }

    private void initInstances() {
        containerIcon = (ImageView) findViewById(R.id.container_icon);
        containerType = (TextView) findViewById(R.id.container_type);
    }

    public void setContainerType(int containerTypeId, String containerTypeName) {
        containerIcon.setImageResource(ContainerIconMapping.getContainerIcon(containerTypeId));
        containerType.setText(containerTypeName);
    }

    private static class ContainerIconMapping {
        private static SparseArray<Integer> containerIconMapper = getIconMapping();

        private static SparseArray<Integer> getIconMapping() {
            SparseArray<Integer> iconMapper = new SparseArray<>();
            iconMapper.put(ContainerTypeStub.น้ำใช้.getId(), R.mipmap.ic_container_earthen_jar);
            iconMapper.put(ContainerTypeStub.น้ำดื่ม.getId(), R.mipmap.ic_container_bottle);
            iconMapper.put(ContainerTypeStub.แจกัน.getId(), R.mipmap.ic_container_vase);
            iconMapper.put(ContainerTypeStub.ที่รองกันมด.getId(), R.mipmap.ic_container_ant_tray);
            iconMapper.put(ContainerTypeStub.จานรองกระถาง.getId(), R.mipmap.ic_container_pot_saucer);
            iconMapper.put(ContainerTypeStub.อ่างบัว_ไม้น้ำ.getId(), R.mipmap.ic_container_lotus);
            iconMapper.put(ContainerTypeStub.ยางรถยนต์เก่า.getId(), R.mipmap.ic_container_tire);
            iconMapper.put(ContainerTypeStub.กากใบพืช.getId(), R.mipmap.ic_container_leaf);
            iconMapper.put(ContainerTypeStub.ภาชนะที่ไม่ใช้.getId(), R.mipmap.ic_container_garbages);
            iconMapper.put(ContainerTypeStub.อื่นๆ.getId(), R.mipmap.ic_container_bowl);
            return iconMapper;
        }

        private static int getContainerIcon(int containerId) {
            return containerIconMapper.get(containerId, R.mipmap.ic_building_home_black);
        }
    }
}
