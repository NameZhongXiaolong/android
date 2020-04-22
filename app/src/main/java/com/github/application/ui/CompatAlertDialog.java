package com.github.application.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.github.application.R;

import static android.view.ViewGroup.LayoutParams;

/**
 * Created by ZhongXiaolong on 2020/3/6 10:56.
 * <p>
 * 类似 AlertDialog,对 AlertDialog 扩展
 * <p>
 */
public class CompatAlertDialog extends AppCompatDialog {

    @IdRes public static final int BUTTON_POSITIVE = R.id.button_1;
    @IdRes public static final int BUTTON_NEGATIVE = R.id.button_2;

    private Builder mBuilder;

    private CompatAlertDialog(Builder builder) {
        super(builder.mContext);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable());
        }

        //parent
        LinearLayout view = new LinearLayout(getContext());
        view.setGravity(Gravity.CENTER);
        view.setOrientation(LinearLayout.VERTICAL);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, displayMetrics);
        int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, displayMetrics);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
        int viewDivider = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(width, LayoutParams.WRAP_CONTENT));
        view.setMinimumHeight(minHeight);
        view.setPadding(padding, padding, padding, viewDivider);

        GradientDrawable background = new GradientDrawable();
        background.setColor(mBuilder.mColor);
        background.setCornerRadius(mBuilder.mCornerRadius);
        view.setBackground(background);
        view.setGravity(Gravity.START);

        //title
        if (!TextUtils.isEmpty(mBuilder.mTitle)) {
            TextView tvTitle = new TextView(getContext());
            tvTitle.setId(R.id.text);
            tvTitle.setTextSize(mBuilder.mTitleStyle.mTextSize);
            tvTitle.setTextColor(mBuilder.mTitleStyle.mTextColor);
            tvTitle.setText(mBuilder.mTitle);
            tvTitle.setPadding(0, 0, 0, viewDivider);
            tvTitle.setMaxLines(1);
            view.addView(tvTitle);
        }

        //message
        TextView tvMessage = new TextView(getContext());
        tvMessage.setId(R.id.text1);
        tvMessage.setTextSize(mBuilder.mMessageStyle.mTextSize);
        tvMessage.setTextColor(mBuilder.mMessageStyle.mTextColor);
        tvMessage.setMaxLines(8);
        tvMessage.setEllipsize(TextUtils.TruncateAt.END);
        tvMessage.setText(mBuilder.mMessage);
        view.addView(tvMessage, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        //subMessage
        if (!TextUtils.isEmpty(mBuilder.mSubMessage)) {
            TextView tvSubMessage = new TextView(getContext());
            tvSubMessage.setId(R.id.text2);
            tvSubMessage.setTextSize(mBuilder.mSubMessageStyle.mTextSize);
            tvSubMessage.setTextColor(mBuilder.mSubMessageStyle.mTextColor);
            tvSubMessage.setPadding(0, viewDivider, 0, 0);
            tvSubMessage.setMaxLines(8);
            tvSubMessage.setEllipsize(TextUtils.TruncateAt.END);
            tvSubMessage.setText(mBuilder.mSubMessage);
            view.addView(tvSubMessage,
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        //间距
        Space space = new Space(getContext());
        space.setMinimumHeight(viewDivider);
        view.addView(space, new LinearLayout.LayoutParams(1, 0, 1));

        //按钮区域
        LinearLayout buttonGroup = new LinearLayout(getContext());
        buttonGroup.setOrientation(LinearLayout.HORIZONTAL);

        buttonGroup.addView(new Space(getContext()), new LinearLayout.LayoutParams(0, 1, 1));

        //negative
        if (!TextUtils.isEmpty(mBuilder.mNegativeText)) {
            TextView tvNegative = new TextView(getContext());
            tvNegative.setId(BUTTON_NEGATIVE);
            tvNegative.setTextSize(mBuilder.mNegativeStyle.mTextSize);
            tvNegative.setTextColor(mBuilder.mNegativeStyle.mTextColor);
            tvNegative.setText(mBuilder.mNegativeText);
            tvNegative.setPadding(viewDivider, viewDivider, viewDivider, viewDivider);
            tvNegative.setOnClickListener(v -> {
                if (mBuilder.mNegativeClickListener != null) {
                    mBuilder.mNegativeClickListener.onClick(this, v);
                }
                dismiss();
            });
            buttonGroup.addView(tvNegative);
        }

        //positive
        if (!TextUtils.isEmpty(mBuilder.mPositiveText)) {
            TextView tvPositive = new TextView(getContext());
            tvPositive.setId(BUTTON_POSITIVE);
            tvPositive.setTextSize(mBuilder.mPositiveStyle.mTextSize);
            tvPositive.setTextColor(mBuilder.mPositiveStyle.mTextColor);
            tvPositive.setText(mBuilder.mPositiveText);
            LinearLayout.LayoutParams lp;
            lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.leftMargin = viewDivider * 2;
            tvPositive.setPadding(viewDivider, viewDivider, viewDivider, viewDivider);
            tvPositive.setOnClickListener(v -> {
                if (mBuilder.mPositiveClickListener != null) {
                    mBuilder.mPositiveClickListener.onClick(this, v);
                }
                dismiss();
            });
            buttonGroup.addView(tvPositive, lp);
        }

        if (buttonGroup.getChildCount() == 0) buttonGroup.setPadding(0, viewDivider, 0, 0);

        view.addView(buttonGroup, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        setContentView(view);

    }

    /**
     * 获取标题View
     */
    public TextView getTitleView() {
        return findViewById(R.id.text);
    }

    /**
     * 获取消息View
     */
    public TextView getMessageView() {
        return findViewById(R.id.text1);
    }

    /**
     * 获取副消息VIew
     */
    public TextView getSubMessageView() {
        return findViewById(R.id.text2);
    }

    /**
     * 构建
     */
    public static class Builder {

        private Context mContext;
        private String mTitle;
        private String mMessage;
        private String mSubMessage;
        private String mPositiveText;
        private String mNegativeText;
        private TextStyle mTitleStyle;
        private TextStyle mMessageStyle;
        private TextStyle mSubMessageStyle;
        private TextStyle mPositiveStyle;
        private TextStyle mNegativeStyle;
        private OnClickListener mPositiveClickListener;
        private OnClickListener mNegativeClickListener;
        private boolean mCancelable;
        private int mColor;
        private int mCornerRadius;

        /**
         * builder模式构建
         */
        public Builder(Context context) {
            mContext = context;
            mCancelable = true;
            mCornerRadius = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            mColor = Color.WHITE;
        }

        /**
         * 设置标题
         */
        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        /**
         * 设置消息
         */
        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        /**
         * 设置副消息
         */
        public Builder setSubMessage(String subMessage) {
            mSubMessage = subMessage;
            return this;
        }

        /**
         * 设置 Positive 按钮
         *
         * @param positiveText 文字描述
         * @param l            点击事件
         */
        public Builder setPositiveButton(String positiveText, OnClickListener l) {
            mPositiveText = positiveText;
            mPositiveClickListener = l;
            return this;
        }

        /**
         * 设置 Negative 按钮
         *
         * @param negativeText 文字描述
         * @param l            点击事件
         */
        public Builder setNegativeButton(String negativeText, OnClickListener l) {
            mNegativeText = negativeText;
            mNegativeClickListener = l;
            return this;
        }

        /**
         * 设置标题文字样式
         *
         * @param textSize  字号,单位sp
         * @param textColor 字体颜色
         */
        public Builder setTitleStyle(int textSize, @ColorInt int textColor) {
            mTitleStyle = new TextStyle(textSize > 0 ? textSize : 16, textColor);
            return this;
        }

        /**
         * 设置消息文字样式
         *
         * @param textSize  字号,单位sp
         * @param textColor 字体颜色
         */
        public Builder setMessageStyle(int textSize, @ColorInt int textColor) {
            mMessageStyle = new TextStyle(textSize > 0 ? textSize : 14, textColor);
            return this;
        }

        /**
         * 设置副消息文字样式
         *
         * @param textSize  字号,单位sp
         * @param textColor 字体颜色
         */
        public Builder setSubMessageStyle(int textSize, @ColorInt int textColor) {
            mSubMessageStyle = new TextStyle(textSize > 0 ? textSize : 14, textColor);
            return this;
        }

        /**
         * 设置Positive按钮文字样式
         *
         * @param textSize  字号,单位sp
         * @param textColor 字体颜色
         */
        public Builder setPositiveStyle(int textSize, @ColorInt int textColor) {
            mPositiveStyle = new TextStyle(textSize > 0 ? textSize : 14, textColor);
            return this;
        }

        /**
         * 设置Negative按钮文字样式
         *
         * @param textSize  字号,单位sp
         * @param textColor 字体颜色
         */
        public Builder setNegativeStyle(int textSize, @ColorInt int textColor) {
            mNegativeStyle = new TextStyle(textSize > 0 ? textSize : 14, textColor);
            return this;
        }

        /**
         * 设置背景
         *
         * @param backgroundColor        背景颜色
         * @param backgroundCornerRadius 背景圆角
         */
        public Builder setBackground(int backgroundColor, int backgroundCornerRadius) {
            mColor = backgroundColor;
            mCornerRadius = backgroundCornerRadius;
            return this;
        }

        /**
         * 外部是否可以点击隐藏,默认true
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * 构建弹窗
         */
        public CompatAlertDialog create() {
            if (mTitleStyle == null) {
                mTitleStyle = new TextStyle(16, Color.BLACK);
            }
            if (mMessageStyle == null) {
                mMessageStyle = new TextStyle(14, Color.BLACK);
            }
            if (mSubMessageStyle == null) {
                mSubMessageStyle = new TextStyle(14, Color.BLACK);
            }
            if (mNegativeStyle == null) {
                mNegativeStyle = new TextStyle(14, Color.parseColor("#7CB3F1"));
            }
            if (mPositiveStyle == null) {
                mPositiveStyle = new TextStyle(14, Color.parseColor("#7CB3F1"));
            }
            CompatAlertDialog dialog = new CompatAlertDialog(this);
            if (!mCancelable) {
                dialog.setCancelable(false);
            }
            return dialog;
        }

        /**
         * 构建+显示弹窗
         */
        public CompatAlertDialog show() {
            CompatAlertDialog dialog = this.create();
            dialog.show();
            return dialog;
        }
    }

    /**
     * 设置草稿箱样式
     */
    public static class DraftBoxStyleBuilder {
        private Context mContext;
        private String mMessage;
        private OnClickListener mPositiveClickListener;
        private OnClickListener mNegativeClickListener;

        /**
         * builder模式,设置草稿箱样式(快捷设置,至设置消息)
         *
         * @param message 消息
         */
        public DraftBoxStyleBuilder(Context context, String message) {
            mContext = context;
            mMessage = message;
        }

        /**
         * 设置Positive按钮点击事件
         */
        public DraftBoxStyleBuilder setPositiveClick(OnClickListener positiveClickListener) {
            mPositiveClickListener = positiveClickListener;
            return this;
        }

        /**
         * 设置Negative按钮点击事件
         */
        public DraftBoxStyleBuilder setNegativeClick(OnClickListener negativeClickListener) {
            mNegativeClickListener = negativeClickListener;
            return this;
        }

        /**
         * 构建
         */
        public CompatAlertDialog builder() {
            CompatAlertDialog dialog = new CompatAlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setTitleStyle(16, Color.BLACK)
                    .setMessage(mMessage)
                    .setMessageStyle(14, Color.BLACK)
                    .setSubMessage("(如未保存，文档在草稿箱内)")
                    .setSubMessageStyle(12, Color.parseColor("#F17C7C"))
                    .setPositiveButton("保存", mPositiveClickListener)
                    .setNegativeButton("退出", mNegativeClickListener)
                    .create();

            //副消息根据消息长度居中
            dialog.setOnShowListener(d -> {
                int width = ((CompatAlertDialog) d).getMessageView().getWidth();
                TextView subMessageView = ((CompatAlertDialog) d).getSubMessageView();
                subMessageView.setMinWidth(width);
                subMessageView.setGravity(Gravity.CENTER);
            });
            return dialog;
        }
    }

    private static class TextStyle {

        private int mTextSize;
        private int mTextColor;

        /**
         * 字体样式bean
         */
        public TextStyle(int textSize, int textColor) {
            mTextSize = textSize;
            mTextColor = textColor;
        }
    }

    public interface OnClickListener {
        /**
         * 点击事件
         */
        void onClick(CompatAlertDialog dialog, View view);
    }
}
