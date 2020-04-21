package com.github.application.base.choice.gallery;

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
import com.github.application.main.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by ZhongXiaolong on 2020/4/21 11:10.
 */
public class GalleryPreviewFragment extends Fragment {

    public static GalleryPreviewFragment newInstance(String photo) {
        Bundle args = new Bundle();
        args.putString("photo",photo);
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
        frameLayout.addView(imageView, new FrameLayout.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT, Gravity.CENTER));
        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = view.findViewById(R.id.image);

        String photo = getArguments() != null ? getArguments().getString("photo","") : "";
        File file = new File(photo);
        if (file.exists()) {
            Picasso.get().load(file).into(imageView);
        }
    }
}
