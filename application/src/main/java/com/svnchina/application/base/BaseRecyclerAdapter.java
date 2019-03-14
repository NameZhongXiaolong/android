package com.svnchina.application.base;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2018/1/26.
 *
 * RecyclerView.Adapter<RecyclerViewHolder>封装
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements ListAdapter {

    public interface BindViewHolder<T> {
        void onBindViewHolder(RecyclerViewHolder holder, T model, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerViewHolder holder, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerViewHolder holder, int position);
    }

    //<editor-fold desc="BaseRecyclerAdapter">

    private final int mLayoutId;
    private final List<T> mList;
    private int mLastPosition = -1;
    private boolean mOpenAnimationEnable = true;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private BindViewHolder mBindViewHolder;

    public BaseRecyclerAdapter(@LayoutRes int layoutId, BindViewHolder<T> bindViewHolder) {
        setHasStableIds(false);
        this.mList = new ArrayList<>();
        this.mLayoutId = layoutId;
        this.mBindViewHolder = bindViewHolder;
    }

    public BaseRecyclerAdapter( @LayoutRes int layoutId, BindViewHolder<T> bindViewHolder,
                                BaseRecyclerAdapter.OnItemClickListener listener) {
        setHasStableIds(false);
        setOnItemClickListener(listener);
        this.mList = new ArrayList<>();
        this.mLayoutId = layoutId;
        this.mBindViewHolder = bindViewHolder;
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutId, BindViewHolder<T> bindViewHolder,
                               BaseRecyclerAdapter.OnItemClickListener listener,
                               OnItemLongClickListener longClickListener) {
        setHasStableIds(false);
        setOnItemClickListener(listener);
        setOnItemLongClickListener(longClickListener);
        this.mList = new ArrayList<>();
        this.mLayoutId = layoutId;
        this.mBindViewHolder = bindViewHolder;
    }

    public BaseRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId, BindViewHolder<T> bindViewHolder) {
        setHasStableIds(false);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
        this.mBindViewHolder = bindViewHolder;
    }

    public BaseRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId, BindViewHolder<T> bindViewHolder,
                               BaseRecyclerAdapter.OnItemClickListener listener) {
        setHasStableIds(false);
        setOnItemClickListener(listener);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
        this.mBindViewHolder = bindViewHolder;
    }

    public BaseRecyclerAdapter(Collection<T> collection, @LayoutRes int layoutId, BindViewHolder<T> bindViewHolder,
                               BaseRecyclerAdapter.OnItemClickListener listener,
                               OnItemLongClickListener longClickListener) {
        setHasStableIds(false);
        setOnItemClickListener(listener);
        setOnItemLongClickListener(longClickListener);
        this.mList = new ArrayList<>(collection);
        this.mLayoutId = layoutId;
        this.mBindViewHolder = bindViewHolder;
    }
    //</editor-fold>


    private void addAnimate(RecyclerViewHolder holder, int postion) {
        if (mOpenAnimationEnable && mLastPosition < postion) {
            holder.itemView.setAlpha(0);
            holder.itemView.animate().alpha(1).start();
            mLastPosition = postion;
        }
    }

    //<editor-fold desc="RecyclerAdapter">
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false),
                mListener, mOnItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        mBindViewHolder.onBindViewHolder(holder, position < mList.size() ? mList.get(position) : null, position);
    }

//    protected abstract void onBindViewHolder(RecyclerViewHolder holder, T model, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimate(holder, holder.getLayoutPosition());
    }

    public void setOpenAnimationEnable(boolean enable) {
        this.mOpenAnimationEnable = enable;
    }

    //</editor-fold>

    //<editor-fold desc="API">

    public BaseRecyclerAdapter<T> setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
        return this;
    }

    public BaseRecyclerAdapter<T> setOnItemLongClickListener(BaseRecyclerAdapter.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    public BaseRecyclerAdapter<T> refresh(Collection<T> collection) {
        mList.clear();
        mList.addAll(collection);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        mLastPosition = -1;
        return this;
    }

    public BaseRecyclerAdapter<T> loadmore(Collection<T> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="ListAdapter">
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

//    public boolean hasStableIds() {
//        return false;
//    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyListDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerViewHolder holder;
        if (convertView != null) {
            holder = (RecyclerViewHolder) convertView.getTag();
        } else {
            holder = onCreateViewHolder(parent, getItemViewType(position));
            convertView = holder.itemView;
            convertView.setTag(holder);
        }
        onBindViewHolder(holder, position);
        addAnimate(holder, position);
        return convertView;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public void setItem(int position, T t) {
        mList.set(position, t);
    }

    public BaseRecyclerAdapter<T> addItem(T t) {
        mList.add(t);
        notifyDataSetInvalidated();
        return this;
    }

    public BaseRecyclerAdapter<T> addAllItem(Collection<T> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }

    public BaseRecyclerAdapter<T> clearItem() {
        mList.clear();
        notifyDataSetChanged();
        return this;
    }

    public int positionOf(T t) {
        return mList.indexOf(t);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    //</editor-fold>
}
