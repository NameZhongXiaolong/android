package com.github.application.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.base.BaseSuperFragment;

/**
 * Created by ZhongXiaolong on 2019/3/25 13:10.
 *
 * 获取设别信息
 */
public class PhoneInfoFragment extends BaseSuperFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getContext());
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(Color.parseColor("#333333"));
        final int padding = dp2px(10);
        textView.setPadding(padding + 3, padding, padding, padding);
        //lineSpacingExtra
        textView.setLineSpacing(8,1.0f);
        textView.setId(R.id.text);
        scrollView.addView(textView);
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = view.findViewById(R.id.text);

        //分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        text.setText("分辨率: " + width + "×" + height+"\n\n");
        text.append("内核版本: "+"API."+ Build.VERSION.SDK_INT+"\n\n");
        text.append("主板: " + Build.BOARD+"\n\n");
        text.append("系统启动程序版本号: " + Build.BOOTLOADER+"\n\n");
        text.append("系统定制商: " + Build.BRAND+"\n\n");
        text.append("cpu指令集: " + Build.CPU_ABI+"\n\n");
        text.append("cpu指令集2: " + Build.CPU_ABI2+"\n\n");
        text.append("设置参数: " + Build.DEVICE+"\n\n");
        text.append("显示屏参数: " + Build.DISPLAY+"\n\n");
        text.append("www.2cto.com无线电固件版本: " + Build.getRadioVersion()+"\n\n");
        text.append("硬件识别码: " + Build.FINGERPRINT+"\n\n");
        text.append("硬件名称: " + Build.HARDWARE+"\n\n");
        text.append("HOST: " + Build.HOST+"\n\n");
        text.append("修订版本列表: " + Build.ID+"\n\n");
        text.append("硬件制造商: " + Build.MANUFACTURER+"\n\n");
        text.append("版本: " + Build.MODEL+"\n\n");
        text.append("硬件序列号: " + Build.SERIAL+"\n\n");
        text.append("手机制造商: " + Build.PRODUCT+"\n\n");
        text.append("描述Build的标签: " + Build.TAGS+"\n\n");
        text.append("TIME: " + Build.TIME+"\n\n");
        text.append("builder类型: " + Build.TYPE+"\n\n");
        text.append("USER: " + Build.USER+"\n\n");
        text.append("INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n\n");
    }

}
