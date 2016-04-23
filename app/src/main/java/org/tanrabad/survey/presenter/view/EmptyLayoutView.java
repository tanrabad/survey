package org.tanrabad.survey.presenter.view;

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

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.empty_layout, this);
        emptyTextView = (TextView) findViewById(R.id.empty_text);
        emptyButtonView = (Button) findViewById(R.id.empty_button);
        emptyIconView = (ImageView) findViewById(R.id.empty_icon);
    }

    public void setEmptyText(String emptyText) {
        emptyTextView.setText(emptyText);
    }

    public void setEmptyText(@StringRes int emptyTextId) {
        emptyTextView.setText(emptyTextId);
    }

    public void setEmptyIcon(@DrawableRes int drawableId) {
        emptyIconView.setImageResource(drawableId);
    }

    public void setEmptyButtonText(@StringRes int emptyButtonTextId, OnClickListener onClickListener) {
        setEmptyButtonText(getContext().getString(emptyButtonTextId), onClickListener);
    }

    public void setEmptyButtonText(String emptyButtonText, OnClickListener onClickListener) {
        emptyButtonView.setText(emptyButtonText);
        emptyButtonView.setOnClickListener(onClickListener);
    }

    public void setEmptyIconVisibility(boolean isVisible) {
        emptyIconView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setEmptyButtonVisibility(boolean isVisible) {
        emptyButtonView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void showProgressBar() {
        findViewById(R.id.loading_view).setVisibility(VISIBLE);
        findViewById(R.id.empty_layout_view).setVisibility(GONE);
    }

    public void showEmptyLayout() {
        findViewById(R.id.loading_view).setVisibility(GONE);
        findViewById(R.id.empty_layout_view).setVisibility(VISIBLE);
    }

    public void hide() {
        findViewById(R.id.loading_view).setVisibility(GONE);
        findViewById(R.id.empty_layout_view).setVisibility(GONE);
    }
}
