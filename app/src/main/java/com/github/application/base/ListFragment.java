package com.github.application.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;

import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/12 12:59.
 * <p>
 * 列表Fragment,RecyclerView已经设置好BaseAdapter,并且对沉浸式主题底部navigationBar适配
 */
public abstract class ListFragment<T> extends BaseSuperFragment {

    private RecyclerView mRecyclerView;
    private BaseAdapter<T> mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setId(R.id.list);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.list);
        mAdapter = new BaseAdapter<T>() {

            final int mNavigationBar = 9;

            @NonNull
            @Override
            public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == mNavigationBar) {
                    return BaseHolder.instance(parent, R.layout.navigation);
                }
                return ListFragment.this.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
                if (getItemViewType(position) == mNavigationBar) {
                    return;
                }
                ListFragment.this.onBindViewHolder(holder, position);
            }

            @Override
            public int getItemViewType(int position) {
                if (position == getItemCount() - 1) {
                    return mNavigationBar;
                }
                return super.getItemViewType(position);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount() + 1;
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public final ListFragment add(T data) {
        mAdapter.add(mAdapter.getItemCount() - 1, data);
        return this;
    }

    public final ListFragment addAll(List<T> data) {
        mAdapter.addAll(mAdapter.getItemCount() - 1, data);
        return this;
    }

    public final ListFragment add(int position, T data) {
        if (position < mAdapter.getItemCount() - 1) {
            mAdapter.add(position, data);
        } else {
            mAdapter.add(data);
        }
        return this;
    }

    public final ListFragment addAll(int position, List<T> data) {
        if (position < mAdapter.getItemCount() - 1) {
            mAdapter.addAll(position, data);
        } else {
            mAdapter.addAll(data);
        }
        return this;
    }

    public final ListFragment remove(int position) {
        mAdapter.remove(position);
        return this;
    }

    public final T get(int position) {
        return mAdapter.get(position);
    }

    public final int getItemCount() {
        return mAdapter.getItemCount();
    }

    public abstract BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(@NonNull BaseHolder holder, int position);

}
