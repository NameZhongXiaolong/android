package com.github.application.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ZhongXiaolong on 2019/9/2 16:43.
 */
public class SquareItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpacing;
    private int mSpanCount ;
    private int mParentWidth;

    public SquareItemDecoration(int parentWidth,int spanCount,int spacing) {
        mSpacing = spacing;
        mSpanCount = spanCount;
        mParentWidth = parentWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        int column = position % mSpanCount; // item column
        outRect.left = mSpacing - column * mSpacing / mSpanCount; // spacing - column * ((1f
        // / spanCount) * spacing)
        outRect.right = (column + 1) * mSpacing / mSpanCount; // (column + 1) * ((1f /
        // spanCount) * spacing)
        if (position < mSpanCount) { // top edge
            outRect.top = mSpacing;
        }
        outRect.bottom = mSpacing; // item bottom
        //配置高度
        view.getLayoutParams().height =
                (mParentWidth / mSpanCount) - (outRect.left + outRect.right);
    }

}
