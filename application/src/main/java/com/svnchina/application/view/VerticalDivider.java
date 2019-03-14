package com.svnchina.application.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ZhongXiaolong on 2018/1/16 20:12.
 * <p>
 * RecyclerView
 * LinearLayoutManager LinearLayoutManager.VERTICAL
 * 分割线
 */
public class VerticalDivider extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private Paint dividerPaint;

    public VerticalDivider(Context context, @ColorInt int color, int dividerHeight) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        this.dividerHeight = dp2px(context, dividerHeight);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                int childCount = parent.getChildCount();
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();

                for (int i = 0; i < childCount; i++) {
                    View view = parent.getChildAt(i);
                    float top = view.getTop();
                    float bottom = top + dividerHeight;
                    c.drawRect(left, top, right, bottom, dividerPaint);
                    if (i == childCount - 1) {
                        top = view.getBottom();
                        bottom = top + dividerHeight;
                        c.drawRect(left, top, right, bottom, dividerPaint);
                    }
                }
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
