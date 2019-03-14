package com.svnchina.application.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.svnchina.application.R;
import com.svnchina.application.base.BaseSuperFragment;

/**
 * Created by ZhongXiaolong on 2018/6/29 17:12.
 * <p>
 * 手机信息
 */
public class PhoneInfoFm extends BaseSuperFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(LayoutInflater.from(getContext()).inflate(R.layout.simple_text, scrollView, false));
        return scrollView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = view.findViewById(R.id.text);
        text.setTextColor(Color.parseColor("#333333"));

        //分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        text.setText("分辨率: " + width + "×" + height+"\n\n");
        text.append("内核版本: "+"API."+Build.VERSION.SDK_INT+"\n\n");
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
        text.append("INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n\n\n\n\n\n");


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public int[] getMetrics() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int[] metrics = {width, height};
        return metrics;
    }
}
