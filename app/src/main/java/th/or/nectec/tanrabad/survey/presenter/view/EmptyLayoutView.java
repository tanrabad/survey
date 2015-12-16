package th.or.nectec.tanrabad.survey.presenter.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.or.nectec.tanrabad.survey.R;

public class EmptyLayoutView extends LinearLayout {
    private ImageView emptyIconView;
    private TextView emptyTextView;
    private Button emptyButtonView;

    public EmptyLayoutView(Context context) {
        this(context, null);
    }

    public EmptyLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.empty_layout, this);
    }

    private void initInstances() {
        emptyTextView = (TextView) findViewById(R.id.empty_text);
        emptyButtonView = (Button) findViewById(R.id.empty_button);
        emptyIconView = (ImageView) findViewById(R.id.empty_icon);
    }

    public void setEmptyText(String emptyText) {
        emptyTextView.setText(emptyText);
    }

    public void setEmptyText(@StringRes int emptyTextID) {
        emptyTextView.setText(emptyTextID);
    }

    public void setEmptyIcon(@DrawableRes int drawableID) {
        emptyIconView.setImageResource(drawableID);
    }

    public void setEmptyButtonText(String emptyButtonText) {
        emptyButtonView.setText(emptyButtonText);
    }

    public void setEmptyButtonText(String emptyButtonText, OnClickListener onClickListener) {
        emptyButtonView.setText(emptyButtonText);
        emptyButtonView.setOnClickListener(onClickListener);
    }

    public void setEmptyButtonText(@StringRes int emptyButtonTextID) {
        emptyButtonView.setText(emptyButtonTextID);
    }

    public void setEmptyButtonText(@StringRes int emptyButtonTextID, OnClickListener onClickListener) {
        emptyButtonView.setText(emptyButtonTextID);
        emptyButtonView.setOnClickListener(onClickListener);
    }

    public void setEmptyIconVisibility(boolean isVisible) {
        if (isVisible) {
            emptyIconView.setVisibility(VISIBLE);
        } else {
            emptyIconView.setVisibility(GONE);
        }
    }

    public void setEmptyButtonVisibility(boolean isVisible) {
        if (isVisible) {
            emptyButtonView.setVisibility(VISIBLE);
        } else {
            emptyButtonView.setVisibility(GONE);
        }
    }
}
