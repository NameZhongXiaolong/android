package com.github.application.base.choice.gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.application.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by ZhongXiaolong on 2020/4/20 15:52.
 */
public class PhotoPreviewDialogFragment extends AppCompatDialogFragment {

    private String mPhoto;
    private CallBack mCallBack;

    public static PhotoPreviewDialogFragment newInstance(String photo, boolean isChecked) {
        Bundle args = new Bundle();
        args.putString("photo", photo);
        args.putBoolean("checked", isChecked);
        PhotoPreviewDialogFragment fragment = new PhotoPreviewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoPreviewDialogFragment setCallBack(CallBack callBack) {
        mCallBack = callBack;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AppCompatDialog(requireContext(), android.R.style.Theme_Material_Light_NoActionBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        return inflater.inflate(R.layout.fragment_photo_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhoto = getArguments() != null ? getArguments().getString("photo", "") : "";
        boolean isChecked = getArguments() != null && getArguments().getBoolean("checked");

        Toolbar toolbar = view.findViewById(R.id.tool_bar);
        Button button = view.findViewById(R.id.btn_choice_complete);
        ImageView imageView = view.findViewById(R.id.image);

        toolbar.setTitle("图片预览");
        toolbar.setNavigationOnClickListener(v -> dismiss());

        button.setText(isChecked ? "取消" : "选择");
        button.setTag(isChecked);
        button.setOnClickListener(this::onCompleteClick);

        File file = new File(mPhoto);
        if (file.exists()) Picasso.get().load(file).into(imageView);
    }

    private void onCompleteClick(View view) {
        if (mCallBack != null) {
            mCallBack.onCallBack(mPhoto);
        }
        dismiss();
    }

    interface CallBack{
        void onCallBack(String photo);
    }
}
