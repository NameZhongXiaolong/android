package com.github.application.base.choice.gallery;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.utils.UnitUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2020/4/16 11:13.
 *
 * 图片Adapter
 */
final class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<String> mData;
    private List<String> mChoicePhotos;
    private OnItemClickListener mOnItemClickListener;

    PhotoAdapter(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mData = new ArrayList<>();
        mChoicePhotos = new ArrayList<>();
    }

    /**
     * 重新设置图片路径
     */
    public PhotoAdapter setData(List<String> data) {
        mData.clear();
        if (data != null) mData.addAll(data);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 设置选中图片
     */
     void setChoicePhotos(List<String> choicePhotos) {
        mChoicePhotos.clear();
         if (choicePhotos != null) mChoicePhotos.addAll(choicePhotos);
        notifyDataSetChanged();
     }

    /**
     * 获取选中图片
     */
    List<String> getChoicePhotos() {
        return mChoicePhotos;
    }

    /**
     * 获取选中图片数量
     */
    int getChoicePhotoCount(){
        return mChoicePhotos.size();
    }

    /**
     * 判断是否选中
     */
    boolean getChecked(int position) {
        return mChoicePhotos.contains(mData.get(position));
    }

    /**
     * 设置是否选中
     */
    void setChecked(int position, boolean isChecked) {
        String path = mData.get(position);
        if (isChecked) {
            if (!mChoicePhotos.contains(path)) {
                //添加成功刷新状态
                mChoicePhotos.add(path);
                notifyItemChanged(position);
            }
        } else {
            int index = mChoicePhotos.indexOf(path);
            if (index >= 0) {
                //删除成功刷新状态
                mChoicePhotos.remove(index);
                notifyItemChanged(position);
                //重新设置选中图片的排序
                for (int i = index; i < mChoicePhotos.size(); i++) {
                    notifyItemChanged(mData.indexOf(mChoicePhotos.get(i)));
                }
            }
        }
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new PhotoHolder(viewGroup, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        final String path = mData.get(position);
        final int indexOfChoicePhotos = mChoicePhotos.indexOf(path);
        holder.setImage(path);
        holder.setButtonText(indexOfChoicePhotos >= 0 ? String.valueOf(indexOfChoicePhotos + 1) : null);
        holder.setButtonForeground(Color.parseColor(indexOfChoicePhotos >= 0 ? "#80000000" : "#00000000"));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 点击事件
     */
    interface OnItemClickListener {
        void onItemClick(PhotoHolder holder, int position);
    }

    /**
     * Holder
     */
    static class PhotoHolder extends RecyclerView.ViewHolder {

        private final ImageView mImage;
        private final Button mButton;
        private final static int IMAGE_SIZE = UnitUtils.displayWidth() / 4;

        private PhotoHolder(@NonNull ViewGroup parent, final OnItemClickListener l) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_photo, parent, false));
            mImage = itemView.findViewById(R.id.image);
            mButton = itemView.findViewById(R.id.button);
            mButton.setOnClickListener(v -> l.onItemClick(this, getAdapterPosition()));
        }

        private void setImage(String path) {
            Picasso.get().load(new File(path)).resize(IMAGE_SIZE, IMAGE_SIZE).centerCrop().into(mImage);
        }

        private void setButtonText(String text) {
            mButton.setText(text);
        }

        private void setButtonForeground(int color) {
            mButton.setBackgroundColor(color);
        }
    }
}
