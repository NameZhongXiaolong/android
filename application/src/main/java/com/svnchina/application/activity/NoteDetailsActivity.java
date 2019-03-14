package com.svnchina.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.svnchina.application.R;
import com.svnchina.application.base.SideslipSignOutActivity;
import com.svnchina.application.base.RecyclerViewHolder;
import com.svnchina.application.base.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2017/12/13 16:04.
 * <p>
 * 笔记详情
 */
public class NoteDetailsActivity extends SideslipSignOutActivity {

    public static void start(Context context, String title, String assets) {
        Intent starter = new Intent(context, NoteDetailsActivity.class);
        starter.putExtra(KEY, title);
        starter.putExtra(KEY_STRING, assets);
        context.startActivity(starter);
    }

    private final int ITEM_IMAGE = 3;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setSupportActionBar(R.id.action_bar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(KEY));

        mList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.fm_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            InputStream is = getContext().getAssets().open(getIntent().getStringExtra(KEY_STRING));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                mList.add(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            recyclerView.setAdapter(new ListAdapter());
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int item = R.layout.simple_text;
            if (viewType == ITEM_IMAGE) item = R.layout.simple_image;
            return new RecyclerViewHolder(getLayoutInflater().inflate(item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            if (getItemViewType(position) != ITEM_IMAGE) holder.text(R.id.text, mList.get(position));
            else holder.image(R.id.image, Utils.getImageFromAssetsFile(getContext(), mList.get(position)));
        }

        @Override
        public int getItemViewType(int position) {
            if (mList.get(position).contains("image")) return ITEM_IMAGE;
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}
