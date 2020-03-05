package com.github.application.view;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.application.utils.UnitUtils;
import com.github.application.view.picasso.CornerRadiusTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by ZhongXiaolong on 2019/12/20 17:29.
 * 图片九宫格自定义Layout
 */
public class NineGridlayout extends ViewGroup {

    /**
     * 列数
     */
    private int mSpan = 3;
    /**
     * Item 水平之间的间距
     */
    private int mHorizontalSpace;
    /**
     * Item 垂直之间的间距
     */
    private int mVerticalSpace;
    /**
     * 最大的Item数量
     */
    private int mMaxItem = 9;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    protected Adapter mAdapter;
    protected DataSetObserver mObserver;
    private boolean mCreate;

    public NineGridlayout(Context context) {
        this(context, null);
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        mHorizontalSpace = UnitUtils.px(3);
        mVerticalSpace = UnitUtils.px(3);
    }

    /**
     * 1. 获取自已宽度
     * 2. 根据自己的宽度，计算子View的宽度
     * 3. 对比一下子View的个数和最大限制子View的个数
     * 4. 计算自己的高度
     * 5. 给自己设置宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取控件的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 测量子View的宽高
        int childCount = getChildCount();

        //设置一行显示几个
        if (childCount <= 1) mSpan = 1;
        else if (childCount == 2) mSpan = 2;
        else mSpan = 3;

        // 计算单个子View的宽度
        int itemWidth = (width - getPaddingLeft() - getPaddingRight() - mHorizontalSpace * (mSpan - 1)) / mSpan;
        // 计算一下最大的条目数量
        childCount = Math.min(childCount, mMaxItem);
        if (childCount <= 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int itemSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            measureChild(child, itemSpec, mSpan == 1 ? MeasureSpec.makeMeasureSpec((int) (itemWidth * 0.6),
                    MeasureSpec.EXACTLY) : itemSpec);
        }
        int height = itemWidth * (childCount % mSpan == 0 ? (childCount / mSpan) : (childCount / mSpan + 1))
                + mVerticalSpace * ((childCount - 1) / mSpan);
        if (mSpan == 1) height = (int) (itemWidth * 0.6);
        // 指定自己的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!mCreate) {
            int childCount = getChildCount();
            // 计算一下最大的条目数量
            childCount = Math.min(childCount, mMaxItem);
            if (childCount <= 0) {
                return;
            }
            int cl = getPaddingLeft();
            int ct = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                child.layout(cl, ct, cl + width, ct + height);
                // 累加宽度
                cl += width + mHorizontalSpace;
                // 如果是换行
                if ((i + 1) % mSpan == 0) {
                    // 重置左边的位置
                    cl = getPaddingLeft();
                    // 叠加高度
                    ct += height + mVerticalSpace;
                }
                mAdapter.bindView(i, getChildAt(i));
            }
            mCreate = true;
        }
    }


    /**
     * 设置Adapter
     */
    public void setAdapter(Adapter adapter) {
        // 移除监听
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
            mAdapter = null;
            mObserver = null;
        }
        if (adapter == null) {
            throw new NullPointerException("FlowBaseAdapter is null");
        }
        mAdapter = adapter;
        resetLayout();
        mObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                resetLayout();
            }
        };
        mAdapter.registerDataSetObserver(mObserver);
    }

    /**
     * 重新添加布局
     */
    protected void resetLayout() {
        mCreate = false;
        removeAllViews();
        int count = mAdapter.getCount();
        count = Math.min(count, mMaxItem);
        for (int i = 0; i < count; i++) {
            final int position = i;
            View view = mAdapter.getView(position, this);
            addView(view);
            view.setOnClickListener(v -> {
                if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(position, v);
            });

            view.setOnLongClickListener(v -> {
                if (mOnItemLongClickListener != null) {
                    return mOnItemLongClickListener.onItemClick(position, v);
                }
                return false;
            });
        }
    }

    /**
     * 设置横向间距
     */
    public void setHorizontalSpace(int horizontalSpace) {
        mHorizontalSpace = horizontalSpace;
    }

    /**
     * 设置纵向间距
     */
    public void setVerticalSpace(int verticalSpace) {
        mVerticalSpace = verticalSpace;
    }

    /**
     * 设置间距
     */
    public void setSpace(int space) {
        mVerticalSpace = space;
        mHorizontalSpace = space;
    }

    /**
     * item点击监听
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * item长按时间
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 适配器
     */
    public static abstract class Adapter {
        private DataSetObservable mObservable = new DataSetObservable();

        /**
         * 数量
         */
        public abstract int getCount();

        /**
         * 条目的布局
         */
        public abstract View getView(int position, ViewGroup parent);

        /**
         * 绑定数据
         */
        public abstract void bindView(int position, View item);

        /**
         * 注册数据监听
         */
        public void registerDataSetObserver(DataSetObserver observer) {
            mObservable.registerObserver(observer);
        }

        /**
         * 移除数据监听
         */
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        /**
         * 内容改变
         */
        public void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }
    }

    /**
     * item点击事件监听
     */
    public interface OnItemClickListener {

        void onItemClick(int position, View view);
    }

    /**
     * item长按时间
     */
    public interface OnItemLongClickListener {

        boolean onItemClick(int position, View view);

    }

    /**
     * 图片适配器,继承{@link Adapter},满足图片九宫格(大部分需求就是这样)
     */
    public static class SimpleImageAdapter extends Adapter {

        private GradientDrawable mGradientDrawable;
        private List<String> mImageUrls;
        private float mCornerRadius;

        /**
         * @param imageUrls 图片路径
         */
        public SimpleImageAdapter(List<String> imageUrls) {
            mImageUrls = imageUrls;
            mCornerRadius = UnitUtils.px(3);
            mGradientDrawable = new GradientDrawable();
            mGradientDrawable.setColor(Color.parseColor("#30333333"));
            mGradientDrawable.setCornerRadius(mCornerRadius);
        }

        /**
         * @param imageUrls             图片路径
         * @param imageViewCornerRadius 图片圆角
         * @param placeholderColor      图片加载/错误背景颜色
         */
        public SimpleImageAdapter(List<String> imageUrls, float imageViewCornerRadius, @ColorInt int placeholderColor) {
            mImageUrls = imageUrls;
            mCornerRadius = imageViewCornerRadius;
            mGradientDrawable = new GradientDrawable();
            mGradientDrawable.setColor(placeholderColor);
            mGradientDrawable.setCornerRadius(mCornerRadius);
        }

        @Override
        public int getCount() {
            return mImageUrls != null ? mImageUrls.size() : 0;
        }

        @Override
        public View getView(int position, ViewGroup parent) {
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            return imageView;
        }

        @Override
        public void bindView(int position, View item) {
            ImageView imageView = ((ImageView) item);
            String pathname = mImageUrls.get(position);
            File file = new File(pathname);
            RequestCreator load = file.exists() ? Picasso.get().load(file) : Picasso.get().load(pathname);

            load.placeholder(mGradientDrawable)
                    .error(mGradientDrawable)
                    .resize(item.getWidth(), item.getHeight())
                    .centerCrop()
                    .transform(new CornerRadiusTransform((int) mCornerRadius))
                    .into(imageView);
        }
    }
}
