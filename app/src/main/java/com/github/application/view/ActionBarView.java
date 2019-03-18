package com.github.application.view;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.base.BaseSuperActivity;
import com.github.application.main.Constants;
import com.github.application.utils.UnitUtils;

/**
 * Created by ZhongXiaolong on 2019/3/11 10:21.
 */
public class ActionBarView extends FrameLayout {

    private View mViewDivider;
    private TextView mTvTitle;
    private ImageButton mIbNavigation;
    private ActionMenuView mActionMenuView;

    public ActionBarView(@NonNull Context context) {
        this(context, null);
    }

    /*
        <attr name="titleText" format="string"/>
        <attr name="titleTextColor" format="color"/>
        <attr name="titleTextSize" format="dimension"/>
        <attr name="dividerHeight" format="dimension"/>
        <attr name="dividerColor" format="color"/>
        <attr name="navigationIcon" format="reference"/>
     */
    public ActionBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

        //设置默认属性
        int titleTextColor = Color.parseColor("#EEFFFFFF");
        int navigationIcon = R.drawable.ic_back_white;

        int dividerColor = Color.parseColor("#DEDEDE");
        int titleTextSize = 0;
        int dividerHeight = 1;

        //白色背景颜色设置默认的返回键和字体颜色
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            int color = ((ColorDrawable) background).getColor();
            //透明度
            final int alpha = Color.alpha(color);

            boolean dark = alpha <= 48;

            //透明度>0.2就看rgb,透明度<=0.2就设置黑色
            if (!dark) {
                final double darkness = 1 -
                        (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
                dark = darkness < 0.2;
            }
            if (getContext() instanceof BaseSuperActivity) {
                ((BaseSuperActivity) getContext()).setStatusBarDark(dark);
            }
            if (dark) {
                titleTextColor = Color.parseColor("#EE333333");
                navigationIcon = R.drawable.ic_back_black;
            }

        }

        if (background == null) {
            //设置状态栏颜色
            titleTextColor = Color.parseColor("#EE333333");
            navigationIcon = R.drawable.ic_back_black;
            if (getContext() instanceof BaseSuperActivity) {
                ((BaseSuperActivity) getContext()).setStatusBarDark(true);
            }
        }

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ActionBarView);
        String titleText = attributes.getString(R.styleable.ActionBarView_titleText);
        titleTextColor = attributes.getColor(R.styleable.ActionBarView_titleTextColor, titleTextColor);
        titleTextSize = attributes.getDimensionPixelSize(R.styleable.ActionBarView_titleTextSize, titleTextSize);
        dividerHeight = (int) attributes.getDimension(R.styleable.ActionBarView_dividerHeight, dividerHeight);
        dividerColor = attributes.getColor(R.styleable.ActionBarView_dividerColor, dividerColor);
        navigationIcon = attributes.getResourceId(R.styleable.ActionBarView_navigationIcon, navigationIcon);
        boolean statusPadding = attributes.getBoolean(R.styleable.ActionBarView_setStatusBarPadding, true);

        if (!TextUtils.isEmpty(titleText)) mTvTitle.setText(titleText);
        if (titleTextSize > 0) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        } else {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        mTvTitle.setTextColor(titleTextColor);
        //设置title正正居中
        if (getPaddingLeft() > getPaddingRight()) {
            mTvTitle.setPadding(0, 0, getPaddingLeft() - getPaddingRight(), 0);
        }
        if (getPaddingRight() > getPaddingLeft()) {
            mTvTitle.setPadding(getPaddingRight() - getPaddingLeft(), 0, 0, 0);
        }

        //设置默认icon和paddingLeft
        mIbNavigation.setImageResource(navigationIcon);
        if (navigationIcon == R.drawable.ic_back_black || navigationIcon == R.drawable.ic_back_white) {
            if (getPaddingLeft() == 0) {
                int dp10 = UnitUtils.dp2px(context, 10);
                mIbNavigation.setPadding(dp10, 0, dp10, 0);
            }
        }

        ViewGroup.LayoutParams lp = mViewDivider.getLayoutParams();
        lp.height = dividerHeight>8?8:dividerHeight;
        mViewDivider.setLayoutParams(lp);
        mViewDivider.setBackgroundColor(dividerColor);

        attributes.recycle();
        int minHeight = (int) getContext().getResources().getDimension(R.dimen.actionBarSize);
        if (statusPadding) {
            int paddingTop = (int) (getPaddingTop() + getContext().getResources().getDimension(R.dimen.statusBarSize));
            setPadding(getLeft(), paddingTop, getRight(), getBottom());
            minHeight = minHeight + paddingTop;
        }
        setMinimumHeight(minHeight);

        //修改图标颜色
//        mIbNavigation.setColorFilter(Color.RED);
    }

    private void init() {
        mViewDivider = new View(getContext());
        mTvTitle = new TextView(getContext());
        mIbNavigation = new ImageButton(getContext());
        mActionMenuView = new ActionMenuView(getContext());

        LayoutParams dividerLp = new LayoutParams(Constants.MATCH_PARENT, 1);
        dividerLp.gravity = Gravity.BOTTOM;
        addView(mViewDivider, dividerLp);

        LayoutParams titleLp = new LayoutParams(Constants.WRAP_CONTENT, Constants.WRAP_CONTENT);
        titleLp.gravity = Gravity.CENTER;
        addView(mTvTitle, titleLp);

        LayoutParams navigationLp = new LayoutParams(Constants.WRAP_CONTENT, Constants.WRAP_CONTENT);
        navigationLp.gravity = Gravity.CENTER_VERTICAL;
        mIbNavigation.setBackgroundColor(Color.TRANSPARENT);
        addView(mIbNavigation, navigationLp);

        LayoutParams menuLp = new LayoutParams(Constants.WRAP_CONTENT, Constants.WRAP_CONTENT);
        menuLp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        addView(mActionMenuView, menuLp);

        mNavigationClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }).start();
            }
        };

        mIbNavigation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNavigationClick != null) mNavigationClick.onClick(v);
            }
        });
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (left > right) {
            mTvTitle.setPadding(0, 0, left - right, 0);
        }

        if (left < right) {
            mTvTitle.setPadding(right - left, 0, 0, 0);
        }
    }

    //navigationIcon
    private View.OnClickListener mNavigationClick;

    public void setNavigationIcon(@DrawableRes int res) {
        mIbNavigation.setImageResource(res);
    }

    public void setNavigationClickListener(View.OnClickListener listener) {
        mNavigationClick = listener;
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setTitleSize(float size) {
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTitleColor(@ColorInt int color) {
        mTvTitle.setTextColor(color);
    }

    public void setDivider(@ColorInt int color, int height) {
        ViewGroup.LayoutParams lp = mViewDivider.getLayoutParams();
        lp.height = height;
        mViewDivider.setLayoutParams(lp);
        mViewDivider.setBackgroundColor(color);
    }

    public Menu getMenu() {
        return mActionMenuView.getMenu();
    }

    public void addMenuItem(int id, String title, @DrawableRes int drawable, int actionEnum) {
        getMenu().add(id, id, id, title).setIcon(drawable).setShowAsAction(actionEnum);
    }

    public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener listener) {
        mActionMenuView.setOnMenuItemClickListener(listener);
    }


}
