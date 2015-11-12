package th.or.nectec.tanrabad.survey.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.survey.R;

public class SurveyContainerView extends LinearLayout {
    ContainerType containerType;
    private android.widget.ImageView containerIconView;
    private TextView containerTypeView;
    private EditText totalContainerView;
    private EditText foundContainerView;

    public SurveyContainerView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public SurveyContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_container_layout, this);
    }

    private void initInstances() {
        this.containerTypeView = (TextView) findViewById(R.id.container_type);
        this.containerIconView = (ImageView) findViewById(R.id.container_icon);
        this.foundContainerView = (EditText) findViewById(R.id.found_container);
        this.totalContainerView = (EditText) findViewById(R.id.total_container);
    }

    public void setContainerType(ContainerType container) {
        containerType = container;
        containerTypeView.setText(container.getName());
    }

    public SurveyDetail getSurveyDetail() {
        String totalStr = totalContainerView.getText().toString();
        int total = TextUtils.isEmpty(totalStr) ? 0 : Integer.valueOf(totalStr);
        String foundStr = foundContainerView.getText().toString();
        int found = TextUtils.isEmpty(foundStr) ? 0 : Integer.valueOf(totalStr);
        SurveyDetail surveyDetail = new SurveyDetail(containerType, total, found);
        return surveyDetail;
    }
}
