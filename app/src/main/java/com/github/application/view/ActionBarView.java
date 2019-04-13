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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout mActionMenuView;
    @ColorInt private int mContentColor;
    private MenuItemClickListener mMenuItemClickListener;

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
        //默认ContentColor是白色
        mContentColor = Color.parseColor("#EEFFFFFF");
        int navigationIcon = R.drawable.ic_back;

        int dividerColor = Color.parseColor("#DEDEDE");
        int titleTextSize = 0;
        int dividerHeight = 1;

        //浅色背景(透明度低于<0.2f)设置黑色的返回键\字体颜色\状态栏
        Drawable background = getBackground();

        if (background == null) {
            //设置默认背景颜色
            TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{R.attr.colorAccent});
            int color = typedArray.getColor(0, Color.TRANSPARENT);
            background = new ColorDrawable(color);
            setBackground(background);
        }

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

            //设置状态栏图标颜色
            if (getContext() instanceof BaseSuperActivity) {
                ((BaseSuperActivity) getContext()).setStatusBarDark(dark);
            }

            //设置深色颜色
            if (dark) {
                mContentColor = Color.parseColor("#EE333333");
            }

        }

        //读取xml属性
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ActionBarView);
        String titleText = attributes.getString(R.styleable.ActionBarView_titleText);
        mContentColor = attributes.getColor(R.styleable.ActionBarView_contentColor, mContentColor);
        titleTextSize = attributes.getDimensionPixelSize(R.styleable.ActionBarView_titleTextSize, titleTextSize);
        dividerHeight = (int) attributes.getDimension(R.styleable.ActionBarView_dividerHeight, dividerHeight);
        dividerColor = attributes.getColor(R.styleable.ActionBarView_dividerColor, dividerColor);
        navigationIcon = attributes.getResourceId(R.styleable.ActionBarView_navigationIcon, navigationIcon);
        boolean statusPadding = attributes.getBoolean(R.styleable.ActionBarView_setStatusBarPadding, true);
        attributes.recycle();

        //读取xml属性结束,设置属性
        if (!TextUtils.isEmpty(titleText)) mTvTitle.setText(titleText);
        if (titleTextSize > 0) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        } else {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        mTvTitle.setTextColor(mContentColor);
        //设置title正正居中
        if (getPaddingLeft() > getPaddingRight()) {
            mTvTitle.setPadding(0, 0, getPaddingLeft() - getPaddingRight(), 0);
        }
        if (getPaddingRight() > getPaddingLeft()) {
            mTvTitle.setPadding(getPaddingRight() - getPaddingLeft(), 0, 0, 0);
        }

        //设置默认icon和paddingLeft
        mIbNavigation.setImageResource(navigationIcon);
        if (navigationIcon == R.drawable.ic_back) {
            if (getPaddingLeft() == 0) {
                int dp10 = UnitUtils.dp2px(context, 10);
                mIbNavigation.setPadding(dp10, 0, dp10, 0);
            }
        }
        mIbNavigation.setColorFilter(mContentColor);

        //设置分割线,最高为8px
        ViewGroup.LayoutParams lp = mViewDivider.getLayoutParams();
        lp.height = dividerHeight > 8 ? 8 : dividerHeight;
        mViewDivider.setLayoutParams(lp);
        mViewDivider.setBackgroundColor(dividerColor);

        //设置statusBarPadding,并且设置最小高度
        int minHeight = (int) getContext().getResources().getDimension(R.dimen.actionBarSize);
        if (statusPadding) {
            int paddingTop = (int) (getPaddingTop() + getContext().getResources().getDimension(R.dimen.statusBarSize));
            setPadding(getLeft(), paddingTop, getRight(), getBottom());
            minHeight = minHeight + paddingTop;
        }
        setMinimumHeight(minHeight);
    }

    private void init() {
        mViewDivider = new View(getContext());

        mTvTitle = new TextView(getContext());

        mIbNavigation = new ImageButton(getContext());
        mIbNavigation.setAdjustViewBounds(true);
        mIbNavigation.setScaleType(ImageView.ScaleType.FIT_XY);

        mActionMenuView = new LinearLayout(getContext());
        mActionMenuView.setOrientation(LinearLayout.HORIZONTAL);

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

        mNavigationClick = new NavigationClickListener() {
            @Override
            public void onNavigationClick(ImageButton button) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }).start();
            }
        };
;

        mIbNavigation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNavigationClick != null) mNavigationClick.onNavigationClick((ImageButton) v);
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
    private NavigationClickListener mNavigationClick;

    public void setNavigationIcon(@DrawableRes int res) {
        mIbNavigation.setImageResource(res);
    }

    public void setNavigationClickListener(NavigationClickListener listener) {
        mNavigationClick = listener;
    }

    public void setNavigationDrawableRes(@DrawableRes int drawableRes) {
        mIbNavigation.setImageResource(drawableRes);
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setTitleSizePx(float size) {
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTitleSizeSp(float size) {
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setContentColor(@ColorInt int color) {
        mContentColor = color;
        mIbNavigation.setColorFilter(mContentColor);
        mTvTitle.setTextColor(mContentColor);
        for (int i = 0; i < mActionMenuView.getChildCount(); i++) {
            View childView = mActionMenuView.getChildAt(0);
            if (childView instanceof ImageButton) {
                ((ImageButton) childView).setColorFilter(mContentColor);
            }
        }
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

    public ActionBarView setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
        return this;
    }

    public void addMenuItem(final int id, @DrawableRes int drawable) {
        addMenuItem(id, drawable, null);
    }

    public void addMenuItem(final int id, @DrawableRes int drawable, final MenuItemClickListener l) {
        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setAdjustViewBounds(true);
        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
        imageButton.setImageResource(drawable);
        imageButton.setColorFilter(mContentColor);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constants.WRAP_CONTENT, Constants.WRAP_CONTENT);
        lp.setMargins(0, 0, UnitUtils.dp2px(getContext(), 10), 0);
        mActionMenuView.addView(imageButton, lp);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onMenuItemClick(id);
                    return;
                }
                if (mMenuItemClickListener != null) {
                    mMenuItemClickListener.onMenuItemClick(id);
                }
            }
        });
    }


    public interface MenuItemClickListener {
        void onMenuItemClick(int id);
    }

    public interface NavigationClickListener{
        void onNavigationClick(ImageButton button);
    }
}
