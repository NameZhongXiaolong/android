package com.svnchina.application.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.svnchina.application.R;
import com.svnchina.application.activity.NoteDetailsActivity;
import com.svnchina.application.base.BaseRecyclerAdapter;
import com.svnchina.application.base.RecyclerViewFragment;
import com.svnchina.application.base.RecyclerViewHolder;
import com.svnchina.application.view.VerticalDivider;

import java.util.Arrays;

/**
 * Created by ZhongXiaolong on 2018/2/2 15:50.
 * <p>
 * 笔记
 */
public class NoteFm extends RecyclerViewFragment implements BaseRecyclerAdapter.OnItemClickListener {

    enum Note {
        ActivityStart("Activity启动模式", "2.txt"),
        RecyclerViewScroll("NestedScrollView和RecyclerView滑动冲突", "3.txt"),
        Beauty("女优大全", "1.txt"),;

        String title;
        String assets;

        Note(String title, String assets) {
            this.title = title;
            this.assets = assets;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseRecyclerAdapter<Note> adapter;
        adapter = new BaseRecyclerAdapter<>(R.layout.simple_text, (h, m, p) -> h.text(R.id.text, m.title));
        adapter.addAllItem(Arrays.asList(Note.values()));
        adapter.setOnItemClickListener(this);
        addItemDecoration(new VerticalDivider(getContext(), Color.parseColor("#EDEDED"), 1));
        setAdapter(adapter);
    }

    @Override
    public void onItemClick(RecyclerViewHolder holder, int position) {
        final Note note = Note.values()[position];
        NoteDetailsActivity.start(getContext(), note.title, note.assets);
    }

}
