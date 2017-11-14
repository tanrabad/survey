package org.tanrabad.survey.presenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tanrabad.survey.R;

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
}
