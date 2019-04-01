package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseSuperFragment;
import com.github.application.view.LoadButton;
import com.github.application.view.listener.OnLoadButtonClickListener;

/**
 * Created by ZhongXiaolong on 2018/12/3 17:54.
 *
 * 登录界面
 */
public class LoginFm extends BaseSuperFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_login, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LoadButton loadButton = view.findViewById(R.id.lbtn_login);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadButton.start();
            }
        });

        loadButton.setLoadButtonListener(new OnLoadButtonClickListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationFinish() {
//                loadButton.resetWH();
            }

            @Override
            public void onAnimationCancel() {

            }
        });
    }
}
