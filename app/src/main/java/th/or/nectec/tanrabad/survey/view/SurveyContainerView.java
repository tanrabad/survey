package th.or.nectec.tanrabad.survey.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.survey.R;

public class SurveyContainerView extends LinearLayout {
    private ContainerType containerType;

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
        containerTypeView = (TextView) findViewById(R.id.container_type);
        foundContainerView = (EditText) findViewById(R.id.found_larvae_container);
        foundContainerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                increaseTotalWhenFoundMoreThanTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totalContainerView = (EditText) findViewById(R.id.total_container);
        totalContainerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                decreaseFoundWhenTotalLessThanFound();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void increaseTotalWhenFoundMoreThanTotal() {
        if (getFoundValue() > getTotalValue()) {
            totalContainerView.setText(String.valueOf(getFoundValue()));
        }
    }

    private void decreaseFoundWhenTotalLessThanFound() {
        if (getTotalValue() < getFoundValue()) {
            foundContainerView.setText(String.valueOf(getTotalValue()));
        }
    }

    public void setContainerType(ContainerType container) {
        containerType = container;
        containerTypeView.setText(container.getName());
        //ImageView containerIconView = (ImageView) findViewById(R.id.container_icon);
    }

    public SurveyDetail getSurveyDetail() {
        return new SurveyDetail(containerType, getTotalValue(), getFoundValue());
    }

    public void setSurveyDetail(SurveyDetail surveyDetail) {
        int totalContainer = surveyDetail.getTotalContainer();
        if (totalContainer > 0)
            totalContainerView.setText(String.valueOf(totalContainer));

        int foundLarvaContainer = surveyDetail.getFoundLarvaContainer();
        if (foundLarvaContainer > 0)
            foundContainerView.setText(String.valueOf(surveyDetail.getFoundLarvaContainer()));
    }

    private int getFoundValue() {
        String foundStr = foundContainerView.getText().toString();
        return TextUtils.isEmpty(foundStr) ? 0 : Integer.valueOf(foundStr);
    }

    private int getTotalValue() {
        String totalStr = totalContainerView.getText().toString();
        return TextUtils.isEmpty(totalStr) ? 0 : Integer.valueOf(totalStr);
    }

    public boolean isValid() {
        if (getFoundValue() > getTotalValue()) {
            setBackgroundColor(getResources().getColor(R.color.pink_transparent_30));
            return false;
        } else {
            setBackgroundColor(Color.TRANSPARENT);
            return true;
        }

    }
}
