package com.github.application.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.BasePagerAdapter;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.data.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/12 11:29.
 */
public class SettingThemeActivity extends MultipleThemeActivity implements BaseHolder.OnClickListener {

    private int mCurrentIndex;
    private ViewPager mViewPager;
    private List<List<Theme>> mPagerData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        mViewPager = findViewById(R.id.pager);
        mPagerData = new ArrayList<>();


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

        final int heightPixels = getResources().getDisplayMetrics().heightPixels;

        final int itemHeight = dp2px(48);

        int itemSize = (heightPixels - itemHeight) / itemHeight;
        while (list.size() > 0) {
            mPagerData.add(list.subList(0, itemSize));
            list = list.subList(itemSize, list.size());
            if (list.size() < itemSize) {
                mPagerData.add(list);
                break;
            }
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final RadioGroup radioGroup = findViewById(R.id.radio_group);
        List<Fragment> fragments = new ArrayList<>();
        for (List<Theme> pagerDatum : mPagerData) {
            fragments.add(SettingThemeFm.newInstance(pagerDatum).setOnClickListener(this));
            RadioButton rbtn = new RadioButton(this);
            rbtn.setButtonDrawable(new ColorDrawable());
            rbtn.setBackgroundResource(R.drawable.rbtn_line);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(dp2px(30), dp2px(6));
            lp.setMargins(dp2px(3), 0, dp2px(3), 0);
            radioGroup.addView(rbtn, lp);
        }

        mViewPager.setAdapter(new BasePagerAdapter(this, fragments));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                radioGroup.getChildAt(position).performClick();
            }
        });
        mViewPager.setCurrentItem(mCurrentIndex);
        radioGroup.getChildAt(mCurrentIndex).performClick();
    }

    @Override
    public void onClick(View item, int position) {
        String theme = mPagerData.get(mViewPager.getCurrentItem()).get(position).getTheme();
        Toast.makeText(this, theme, Toast.LENGTH_SHORT).show();
    }
}
