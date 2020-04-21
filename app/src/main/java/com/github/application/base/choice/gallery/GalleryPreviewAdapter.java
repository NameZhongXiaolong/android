package com.github.application.base.choice.gallery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

/**
 * Created by ZhongXiaolong on 2020/4/21 11:41.
 */
public class GalleryPreviewAdapter extends RecyclerView.Adapter<GalleryPreviewAdapter.PreviewPhotoHolder> {

    private List<String> mData;
    private final int dp60 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Resources.getSystem().getDisplayMetrics());
    private final int dp10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics());
    private GalleryPreviewAdapter.OnItemClickListener mOnItemClickListener;
    private int mCheckedPosition = -1;

    GalleryPreviewAdapter(List<String> data, OnItemClickListener onItemClickListener) {
        mData = data;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PreviewPhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        ImageView imageView = new ImageView(context);
        imageView.setPadding(3, 3, 3, 3);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(dp60, dp60);
        params.setMargins(dp10, dp10, dp10, dp10);
        imageView.setLayoutParams(params);
        return new PreviewPhotoHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewPhotoHolder previewPhotoHolder, int position) {
        ImageView itemView = (ImageView) previewPhotoHolder.itemView;
        Picasso.get().load(new File(mData.get(position))).resize(dp60, dp60).centerCrop().into(itemView);
        itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(mData.get(position), position));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#EDEDED"));
        gradientDrawable.setStroke(3, mCheckedPosition == position ? Color.parseColor("#FF0C0C") : Color.TRANSPARENT);
        itemView.setBackground(gradientDrawable);
    }

    void setChecked(int checkedPosition) {
        int tmpPosition = mCheckedPosition;
        mCheckedPosition = checkedPosition;
        if (tmpPosition != mCheckedPosition) {
            notifyItemChanged(tmpPosition);
            notifyItemChanged(mCheckedPosition);
        }
    }

    int getCheckedPosition() {
        return mCheckedPosition;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    interface OnItemClickListener {
        void onItemClick(String photo, int position);
    }

    static class PreviewPhotoHolder extends RecyclerView.ViewHolder{

         PreviewPhotoHolder(@NonNull ImageView itemView) {
            super(itemView);
        }
    }
}
