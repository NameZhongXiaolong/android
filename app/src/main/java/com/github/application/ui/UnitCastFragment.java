package com.github.application.ui;

import android.app.AlertDialog;
import android.view.View;

import com.github.application.utils.UnitUtils;

/**
 * Created by ZhongXiaolong on 2019/3/25 13:27.
 * <p>
 * 单位换算
 */
public class UnitCastFragment extends SimpleListFragment {

    private UnitCastDialogF mDp2px;
    private UnitCastDialogF mSp2px;
    private UnitCastDialogF mPx2dp;
    private UnitCastDialogF mPx2sp;
    private AlertDialog mDisplay;

    @Override
    void onCreateList() {
        final String display = "屏幕分辨率: " + UnitUtils.displayWidth() + " × " + UnitUtils.displayHeight();
        add(display);
        add("dp转px");
        add("sp转px");
        add("px转dp");
        add("px转sp");

        mDisplay = new AlertDialog.Builder(getContext()).setTitle("message").setMessage(display)
                .setPositiveButton("确定", null).create();

        mDp2px = UnitCastDialogF.newInstance(UnitCastDialogF.TAG_DP2PX);
        mSp2px = UnitCastDialogF.newInstance(UnitCastDialogF.TAG_SP2PX);
        mPx2dp = UnitCastDialogF.newInstance(UnitCastDialogF.TAG_PX2DP);
        mPx2sp = UnitCastDialogF.newInstance(UnitCastDialogF.TAG_PX2SP);
    }

    @Override
    void onClick(View item, int position) {
        String title = get(position);
        if ("dp转px".equals(title)) {
            mDp2px.show(getChildFragmentManager(), "px");
        }else if ("sp转px".equals(title)){
            mSp2px.show(getChildFragmentManager(), "sp2px");
        } else if ("px转dp".equals(title)) {
            mPx2dp.show(getChildFragmentManager(), "dp");
        } else if ("px转sp".equals(title)) {
            mPx2sp.show(getChildFragmentManager(), "px2sp");

        }else{
            mDisplay.show();
        }
    }
}
