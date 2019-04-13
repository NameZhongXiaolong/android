package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.BaseSuperFragment;

import java.util.Arrays;

/**
 * Created by ZhongXiaolong on 2019/4/11 18:13.
 */
public class RecyclerViewDemoFm extends BaseSuperFragment {

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
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TestAdapter testAdapter = new TestAdapter();
        recyclerView.setAdapter(testAdapter);


        testAdapter.addAll(Arrays.asList("a", "d", "c", "d"));
    }

    private class TestAdapter extends BaseAdapter<String> {
        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 10) {
                return BaseHolder.instance(parent, R.layout.item_test);
            }
            return BaseHolder.instance(parent, R.layout.item_menu_1);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            if (getItemViewType(position) != 10) {
                holder.text(R.id.text, get(position));
            }else{
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position==getItemCount()-1) return 10;
            return super.getItemViewType(position);
        }
    }

}
