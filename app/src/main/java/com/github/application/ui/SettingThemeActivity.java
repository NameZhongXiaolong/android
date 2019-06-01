package com.github.application.ui;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.data.Theme;
import com.github.application.main.Constants;
import com.github.application.utils.UnitUtils;

import java.util.ArrayList;
import java.util.List;

import static com.github.application.utils.ColorUtils.isDarkColor;

/**
 * Created by ZhongXiaolong on 2019/3/12 11:29.
 * <p>
 * 设置主题
 */
public class SettingThemeActivity extends MultipleThemeActivity {

    private int mScrollSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        List<Theme> list = new ArrayList<>();
        list.add(new Theme("BrightYarrow", R.style.AppTheme_BrightYarrow));
        list.add(new Theme("RobinisEGGBlue", R.style.AppTheme_RobinisEGGBlue));
        list.add(new Theme("LightGreenishBlue", R.style.AppTheme_LightGreenishBlue));
        list.add(new Theme("MintLeaf", R.style.AppTheme_MintLeaf));
        list.add(new Theme("SourLemon", R.style.AppTheme_SourLemon));
        list.add(new Theme("FirstDate", R.style.AppTheme_FirstDate));
        list.add(new Theme("ORangeVille", R.style.AppTheme_ORangeVille));
        list.add(new Theme("ElectroBlue", R.style.AppTheme_ElectroBlue));
        list.add(new Theme("ShyMoment", R.style.AppTheme_ShyMoment));
        list.add(new Theme("ExodusFruit", R.style.AppTheme_ExodusFruit));
        list.add(new Theme("PicoPink", R.style.AppTheme_PicoPink));
        list.add(new Theme("PrunusAvium", R.style.AppTheme_PrunusAvium));
        list.add(new Theme("LavenderTea", R.style.AppTheme_LavenderTea));
        list.add(new Theme("PixelatedGrass", R.style.AppTheme_PixelatedGrass));
        list.add(new Theme("MagentaPurple", R.style.AppTheme_MagentaPurple));
        list.add(new Theme("CircumorbitalRing", R.style.AppTheme_CircumorbitalRing));
        list.add(new Theme("BlueMartina", R.style.AppTheme_BlueMartina));

        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        for (Theme theme : list) {
            //设置主题
            final int themeRes = theme.getThemeRes();
            ContextThemeWrapper contextTheme = new ContextThemeWrapper(getContext(), themeRes);

            //创建item
            TextView itemTheme = new TextView(contextTheme);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    Constants.MATCH_PARENT, UnitUtils.getActionBarSize());
            int margin = dp2px(10);
            lp.setMargins(margin, margin, margin, 0);
            itemTheme.setGravity(Gravity.CENTER);
            itemTheme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            //设置theme中的主题颜色
            TypedArray typedArray = contextTheme.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
            int color = typedArray.getColor(0, Color.TRANSPARENT);
            itemTheme.setBackgroundColor(color);

            //设置标题
            itemTheme.setText(theme.getTheme());
            itemTheme.setTextColor(Color.parseColor(isDarkColor(color) ? "#EEFFFFFF" : "#EE333333"));

            //添加
            linearLayout.addView(itemTheme, lp);

            itemTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendThemeChangeBroadcast(themeRes);
                }
            });
        }

        View viewBottom = new View(getContext());
        viewBottom.setMinimumHeight(UnitUtils.px(hasNavigationBar() ? 58 : 10));
        linearLayout.addView(viewBottom);

        NestedScrollView nestedScrollView = findViewById(R.id.scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mScrollSize = mScrollSize + scrollY;
            }
        });

        nestedScrollView.scrollTo(0, mScrollSize);
    }

}
