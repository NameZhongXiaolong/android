package com.github.application.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.data.Theme;

public class MainActivity extends MultipleThemeActivity implements BaseHolder.OnClickListener{

    private MenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MenuAdapter();
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setAdapter(mAdapter);
        mAdapter.add(new Theme("红色",R.style.RedTheme));
        mAdapter.add(new Theme("蓝色",R.style.BuleTheme));
        mAdapter.add(new Theme("灰色",R.style.GrayTheme));
    }

    @Override
    public void onClick(View item, int position) {
        int theme = mAdapter.get(position).getThemeRes();
        sendThemeChangeBroadcast(theme);
        setContentView(R.layout.activity_main);
    }

    public class MenuAdapter extends BaseAdapter<Theme> {

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return BaseHolder.instance(inflater(parent,R.layout.item_menu_2));
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            holder.text(R.id.text, get(position).getTheme());
            holder.setOnClickListener(MainActivity.this);
        }

    }

}
