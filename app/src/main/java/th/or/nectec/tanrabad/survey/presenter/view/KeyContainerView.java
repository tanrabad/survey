package th.or.nectec.tanrabad.survey.presenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import th.or.nectec.tanrabad.entity.stub.ContainerTypeStub;
import th.or.nectec.tanrabad.survey.R;

import java.util.HashMap;

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

    public void setContainerType(String containerTypeName) {
        containerIcon.setImageResource(ContainerIconMapping.getContainerIcon(containerTypeName));
        containerType.setText(containerTypeName);
    }

    private static class ContainerIconMapping {
        private static HashMap<String, Integer> containerIconMapper = getIconMapping();

        private static HashMap<String, Integer> getIconMapping() {
            HashMap<String, Integer> iconMapper = new HashMap<>();
            iconMapper.put(ContainerTypeStub.น้ำใช้.getName(), R.mipmap.ic_container_earthen_jar);
            iconMapper.put(ContainerTypeStub.น้ำดื่ม.getName(), R.mipmap.ic_container_bottle);
            iconMapper.put(ContainerTypeStub.แจกัน.getName(), R.mipmap.ic_container_vase);
            iconMapper.put(ContainerTypeStub.ที่รองกันมด.getName(), R.mipmap.ic_container_ant_tray);
            iconMapper.put(ContainerTypeStub.จานรองกระถาง.getName(), R.mipmap.ic_container_pot_saucer);
            iconMapper.put(ContainerTypeStub.อ่างบัว_ไม้น้ำ.getName(), R.mipmap.ic_container_lotus);
            iconMapper.put(ContainerTypeStub.ยางรถยนต์เก่า.getName(), R.mipmap.ic_container_tire);
            iconMapper.put(ContainerTypeStub.กากใบพืช.getName(), R.mipmap.ic_container_leaf);
            iconMapper.put(ContainerTypeStub.ภาชนะที่ไม่ใช้.getName(), R.mipmap.ic_container_garbages);
            iconMapper.put(ContainerTypeStub.อื่นๆ.getName(), R.mipmap.ic_container_bowl);
            return iconMapper;
        }

        private static int getContainerIcon(String containerTypeName) {
            if (!containerIconMapper.containsKey(containerTypeName))
                return R.mipmap.ic_building_home_black;
            return containerIconMapper.get(containerTypeName);
        }
    }
}
