package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.BaseSuperFragment;
import com.github.application.main.Constants;
import com.github.application.utils.UnitUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by ZhongXiaolong on 2019/7/8 21:06.
 */
public class PictureFm extends BaseSuperFragment {

    private PhotoAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_view, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PhotoAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private class PhotoAdapter extends BaseAdapter<String> {

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView image = new ImageView(parent.getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setAdjustViewBounds(true);
            image.setId(R.id.image);
            RecyclerView.LayoutParams lp;
            lp = new RecyclerView.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT);
            lp.bottomMargin = UnitUtils.dp(8);
            image.setLayoutParams(lp);
            return BaseHolder.instance(image);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            ImageView image = holder.findViewById(R.id.image);
            Picasso.with(getContext()).load(new File(get(position)))
                    .resize(UnitUtils.displayWidth(),UnitUtils.displayWidth()).into(image);
        }
    }

}
