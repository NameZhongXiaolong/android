package com.github.application.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/12/30 16:50.
 *
 * 图片选择Adapter
 */
public class NoteChoicePhotoAdapter extends BaseAdapter<String> {

    private int mItemSize;
    private BaseHolder.OnClickListener mOnItemClickListener;

    public NoteChoicePhotoAdapter(int itemSize) {
        mItemSize = itemSize;
    }

    public NoteChoicePhotoAdapter setOnItemClickListener(BaseHolder.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return BaseHolder.instance(viewGroup, R.layout.item_choice_image);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
        Context context = baseHolder.getItemView().getContext();
        ImageView imageView = baseHolder.findViewById(R.id.image);
        ImageButton button = baseHolder.findViewById(R.id.button);
        button.setOnClickListener(v -> remove(i));
        String url = i < mData.size() ? get(i) : null;

        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.mipmap.ic_add_to);
            button.setVisibility(View.INVISIBLE);
        }else{
            button.setVisibility(View.VISIBLE);
            File file = new File(url);
            Picasso.get().load(file).resize(mItemSize,mItemSize).centerCrop().into(imageView);
        }

        baseHolder.setOnClickListener((item, position) -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public List<String> getChoiceData(){
        return new ArrayList<>(mData);
    }
}
