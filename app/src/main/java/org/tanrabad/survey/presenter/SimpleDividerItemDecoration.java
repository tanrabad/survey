package org.tanrabad.survey.presenter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.tanrabad.survey.utils.android.ResourceUtils;
import th.or.nectec.tanrabad.survey.R;

import static android.support.v7.widget.RecyclerView.ItemDecoration;
import static android.support.v7.widget.RecyclerView.State;

class SimpleDividerItemDecoration extends ItemDecoration {
    private Drawable divider;

    public SimpleDividerItemDecoration(Context context) {
        divider = new ResourceUtils(context).getDrawable(R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
