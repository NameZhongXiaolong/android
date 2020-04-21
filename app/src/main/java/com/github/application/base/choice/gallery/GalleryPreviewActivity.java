package com.github.application.base.choice.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.application.R;
import com.github.application.main.MainApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

/**
 * Created by ZhongXiaolong on 2020/4/20 18:00.
 */
public class GalleryPreviewActivity extends AppCompatActivity {

    private List<String> mChoicePhotos;
    private List<String> mPhotos;
    private ViewPager mViewPager;
    private GalleryPreviewAdapter mAdapter;
    private CheckBox mCheckBox;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private int mMaxChoice;

    public static void start(Activity activity, List<String> photos, List<String> choicePhotos, int checkedPosition, int max) {
        Intent starter = new Intent(activity, GalleryPreviewActivity.class);
        starter.putExtra("photos", new Gson().toJson(photos));
        starter.putExtra("choicePhotos", new Gson().toJson(choicePhotos));
        starter.putExtra("checked", checkedPosition);
        starter.putExtra("max", max);
        activity.startActivityForResult(starter, 100);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);

        String photoJson = getIntent().getStringExtra("photos");
        String choicePhotoJson = getIntent().getStringExtra("choicePhotos");
        int position = getIntent().getIntExtra("checked", 0);
        mMaxChoice = getIntent().getIntExtra("max", 0);
        mPhotos = new Gson().fromJson(photoJson, new TypeToken<List<String>>() {}.getType());
        mChoicePhotos = new Gson().fromJson(choicePhotoJson, new TypeToken<List<String>>() {}.getType());

        mToolbar = findViewById(R.id.tool_bar);
        mViewPager = findViewById(R.id.view_pager);
        mCheckBox = findViewById(R.id.check_box);
        mRecyclerView = findViewById(R.id.list);

        findViewById(R.id.button).setOnClickListener(this::onCompleteClick);

        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new GalleryPreviewAdapter(mChoicePhotos, this::onItemClick);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setChecked(mChoicePhotos.indexOf(mPhotos.get(position)));
        Objects.requireNonNull(getSupportActionBar()).setTitle(((position + 1) + "/" + mPhotos.size()));

        mCheckBox.setChecked(mAdapter.getCheckedPosition() >= 0);
        mCheckBox.setOnCheckedChangeListener(this::onCheckedChanged);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new PhotoAdapter(getSupportFragmentManager(), mPhotos));
        mViewPager.setCurrentItem(position, false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mAdapter.setChecked(mChoicePhotos.indexOf(mPhotos.get(position)));
                mCheckBox.setChecked(mAdapter.getCheckedPosition() >= 0);
                mToolbar.setTitle(((position + 1) + "/" + mPhotos.size()));
            }
        });
    }

    private void onItemClick(String photo, int position) {
        int index = mPhotos.indexOf(photo);
        if (index >= 0) {
            mViewPager.setCurrentItem(index, true);
            mAdapter.setChecked(position);
            mCheckBox.setChecked(mAdapter.getCheckedPosition() >= 0);
        }
    }

    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String photo = mPhotos.get(mViewPager.getCurrentItem());
        if (isChecked) {
            if (!mChoicePhotos.contains(photo)) {
                if (mChoicePhotos.size() < mMaxChoice) {
                    mChoicePhotos.add(photo);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setChecked(mChoicePhotos.size() - 1);
                    mRecyclerView.scrollToPosition(mChoicePhotos.size() - 1);
                } else {
                    MainApplication.errToast("最多只能选择" + mMaxChoice + "张图片");
                    mCheckBox.setChecked(false);
                }
            }
        } else {
            if (mChoicePhotos.contains(photo)) {
                mChoicePhotos.remove(photo);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void onCompleteClick(View view) {
        Intent data = new Intent();
        data.putExtra("choicePhotos", new Gson().toJson(mChoicePhotos));
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class PhotoAdapter extends FragmentStatePagerAdapter{

        private List<String> mPhotos;
         PhotoAdapter(FragmentManager fm,List<String> photos) {
            super(fm);
            mPhotos = photos;
        }

        @Override
        public Fragment getItem(int i) {
            return GalleryPreviewFragment.newInstance(mPhotos.get(i));
        }

        @Override
        public int getCount() {
            return mPhotos != null ? mPhotos.size() : 0;
        }
    }
}