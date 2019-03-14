package com.svnchina.application.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.svnchina.application.R;
import com.svnchina.application.base.BaseSuperActivity;
import com.svnchina.application.base.SimpleTextWatcher;

/**
 * Created by ZhongXiaolong on 2018/5/28 9:14.
 * 钟钟小小
 */
public class UnitConversionActivity extends BaseSuperActivity {

    private EditText mEtInput;
    private String mNum = "";
    private static final int DP2PX = 1;
    private static final int PX2DP = 2;
    private static final int SP2PX = 3;
    private static final int PX2SP = 4;

    public static Intent dp2px(Context context) {
        Intent starter = new Intent(context, UnitConversionActivity.class);
        starter.putExtra(KEY_INTEGER, DP2PX);
        return starter;
    }

    public static Intent px2dp(Context context) {
        Intent starter = new Intent(context, UnitConversionActivity.class);
        starter.putExtra(KEY_INTEGER, PX2DP);
        return starter;
    }

    public static Intent sp2px(Context context) {
        Intent starter = new Intent(context, UnitConversionActivity.class);
        starter.putExtra(KEY_INTEGER, SP2PX);
        return starter;
    }

    public static Intent px2sp(Context context) {
        Intent starter = new Intent(context, UnitConversionActivity.class);
        starter.putExtra(KEY_INTEGER, PX2SP);
        return starter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#60000000")));
        getWindow().setStatusBarColor(Color.parseColor("#AAAAAA"));

        setContentView(R.layout.activity_unit_conversion);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        TextView tvUnit = findViewById(R.id.tv_unit);
        final TextView tvRes = findViewById(R.id.tv_result);

        final int intExtra = getIntent().getIntExtra(KEY_INTEGER, 0);
        String text = "px";
        if (intExtra == DP2PX) text = "dp";
        if (intExtra == SP2PX) text = "sp";
        tvUnit.setText(text);

        mEtInput = findViewById(R.id.et_input);
        mEtInput.addTextChangedListener(new SimpleTextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 1) return;
                if (intExtra == DP2PX) tvRes.setText(dp2px(Integer.valueOf(s.toString())) + "px");
                if (intExtra == SP2PX) tvRes.setText(sp2px(Integer.valueOf(s.toString())) + "px");
                if (intExtra == PX2DP) tvRes.setText(px2dp(Integer.valueOf(s.toString())) + "dp");
                if (intExtra == PX2SP) tvRes.setText(px2sp(Integer.valueOf(s.toString())) + "sp");
            }
        });
;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

}
