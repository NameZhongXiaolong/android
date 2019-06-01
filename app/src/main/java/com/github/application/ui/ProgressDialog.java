package com.github.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.main.Constants;
import com.github.application.utils.UnitUtils;

/**
 * Created by ZhongXiaolong on 2019/3/23 2:30.
 */
public class ProgressDialog extends Dialog {

    private TextView mTextView;
    private int mColor;
    private float mCornerRadius;

    public ProgressDialog(@NonNull Context context) {
        this(context, Color.WHITE, 1);
    }

    public ProgressDialog(@NonNull Context context, @ColorInt int color, float cornerRadiusDp) {
        this(context, 0, color, cornerRadiusDp);
    }

    public ProgressDialog(@NonNull Context context, int themeResId, @ColorInt int color, float cornerRadiusDp) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mColor = color;
        mCornerRadius = cornerRadiusDp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = UnitUtils.dp2px(285);
        final int padding = UnitUtils.dp2px(8);
        mCornerRadius = UnitUtils.dp2px(mCornerRadius);

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setMinimumHeight(width / 2);
        container.setMinimumWidth(width);

        container.setPadding(padding, padding, padding, padding);
        container.setGravity(Gravity.CENTER);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(this.mCornerRadius);
        gradientDrawable.setColor(mColor);
        container.setBackground(gradientDrawable);


        final SharedPreferences sharedPrefs = getContext().getSharedPreferences("main_theme", 0);
        int theme = sharedPrefs.getInt("theme", R.style.AppTheme);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), theme);
        ProgressBar progress = new ProgressBar(contextThemeWrapper);
        container.addView(progress, new LinearLayout.LayoutParams(Constants.WRAP_CONTENT, Constants.WRAP_CONTENT));

        mTextView = new TextView(getContext());
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTextView.setTextColor(Color.parseColor("#333333"));
        container.addView(mTextView, new LinearLayout.LayoutParams(Constants.WRAP_CONTENT, Constants.WRAP_CONTENT));
        mTextView.setPadding(0, padding, 0, 0);

        setContentView(container);
    }

    /**
     * 设置提示
     *
     * @param msg          提示
     * @param showEllipsis 是否显示省略号
     *
     * @return
     */
    public ProgressDialog setTipsMsg(CharSequence msg, boolean showEllipsis) {
        if (mTextView == null) {
            create();
        }
        mTextView.setVisibility(TextUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(msg)) mTextView.setText(msg);
        if (showEllipsis) {
            int i = UnitUtils.dp2px(18);
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_ellipsis);
            drawable.setBounds(0, 0, i, i);
            mTextView.setCompoundDrawables(null, null, drawable, null);
            mTextView.setPadding(i, 0, 0, 0);
        }else{
            mTextView.setCompoundDrawables(null, null, null, null);
            mTextView.setPadding(0, 0, 0, 0);
        }
        return this;
    }

    public ProgressDialog setTipsMsg(CharSequence msg) {
        return setTipsMsg(msg, false);
    }
}
