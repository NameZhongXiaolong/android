package com.github.application.base.choice.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.utils.UnitUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ZhongXiaolong on 2020/4/21 11:10.
 */
public class GalleryPreviewFragment extends Fragment implements Transformation {

    public static GalleryPreviewFragment newInstance(String photo) {
        Bundle args = new Bundle();
        args.putString("photo", photo);
        GalleryPreviewFragment fragment = new GalleryPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(requireContext());
        ImageView imageView = new ImageView(requireContext());
        imageView.setId(R.id.image);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        frameLayout.addView(imageView, new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER));
        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = view.findViewById(R.id.image);

        String photo = getArguments() != null ? getArguments().getString("photo", "") : "";
        File file = new File(photo);

        if (file.exists()) {
            Picasso.get().load(file).transform(this).into(imageView);
        }
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth = UnitUtils.displayWidth();

        if (source.getWidth() == 0) {
            return source;
        }

        //如果图片小于设置的宽度，则返回原图
        if (source.getWidth() < targetWidth) {
            return source;
        } else {
            //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            if (targetHeight != 0 && targetWidth != 0) {
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    source.recycle();
                }
                return result;
            } else {
                return source;
            }
        }
    }

    @Override
    public String key() {
        return "transformation" + " desiredWidth";
    }
}
