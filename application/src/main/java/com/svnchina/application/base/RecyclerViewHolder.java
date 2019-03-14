package com.svnchina.application.base;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder
        extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private BaseRecyclerAdapter.OnItemClickListener mListener;
    private BaseRecyclerAdapter.OnItemLongClickListener mOnItemLongClickListener;
    private View mContentView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mContentView = itemView;
        setWaterBackground(itemView);
    }

    public RecyclerViewHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onClickListener) {
        super(itemView);
        this.mListener = onClickListener;
        mContentView = itemView;
        setWaterBackground(itemView);
        itemView.setOnClickListener(this);

    }

    public RecyclerViewHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onClickListener,
                              BaseRecyclerAdapter.OnItemLongClickListener onLongClickListener) {
        super(itemView);
        this.mListener = onClickListener;
        mOnItemLongClickListener = onLongClickListener;
        mContentView = itemView;
        setWaterBackground(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    /**
     * 设置水波纹背景
     */
    private void setWaterBackground(View view) {
        if (view.getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = view.getContext().getTheme();
            int top = view.getPaddingTop();
            int bottom = view.getPaddingBottom();
            int left = view.getPaddingLeft();
            int right = view.getPaddingRight();
            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                view.setBackgroundResource(typedValue.resourceId);
            }
            view.setPadding(left, top, right, bottom);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = getAdapterPosition();
            if (position >= 0) {
                mListener.onItemClick(this, position);
            }
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (mOnItemLongClickListener != null) {
            int position = getAdapterPosition();
            if (position >= 0) {
                mOnItemLongClickListener.onItemLongClick(this, position);
            }
        }
        return true;
    }

    public <T extends View> T findViewById(@IdRes int viewId) {
        if (viewId == 0) {
            return (T) new View(mContentView.getContext());
        }
        T view = (T) itemView.findViewById(viewId);
        return (T) view;
    }

    public RecyclerViewHolder text(@IdRes int id, CharSequence sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    public RecyclerViewHolder text(@IdRes int id, @StringRes int stringRes) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(stringRes);
        }
        return this;
    }

    public RecyclerViewHolder textColorId(@IdRes int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    public RecyclerViewHolder textSizeId(@IdRes int id, int size) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(size);
        }
        return this;
    }

    public RecyclerViewHolder image(@IdRes int id, @DrawableRes int drawable) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(drawable);
        }
        return this;
    }

    public RecyclerViewHolder image(@IdRes int id, Bitmap bm) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bm);
        }
        return this;
    }

    public View getContentView() {
        return mContentView;
    }
}