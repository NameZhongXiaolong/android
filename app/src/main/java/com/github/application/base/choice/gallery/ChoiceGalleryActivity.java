package com.github.application.base.choice.gallery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.application.R;
import com.github.application.main.MainApplication;
import com.github.application.ui.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;

/**
 * Created by ZhongXiaolong on 2019/12/30 17:16.
 *
 * 使用方法{@link ChoiceGallery},不要直接调用本Activity
 */
public class ChoiceGalleryActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private PhotoAdapter mPhotoAdapter;
    private Button mBtnChoiceComplete;
    private String mTag;
    private int mMaxChoice;
    private SlidingPaneLayout mSlidingPaneLayout;

    static void start(ChoiceGallery choiceGallery) {
        Intent starter = new Intent(choiceGallery.getContext(), ChoiceGalleryActivity.class);
        String tag = "tag" + System.currentTimeMillis();
        starter.putExtra("tag", tag);
        starter.putExtra("maxChoice", choiceGallery.getMaxChoice());
        starter.putStringArrayListExtra("choiceList", new ArrayList<>(choiceGallery.getChoiceList()));
        choiceGallery.getContext().startActivity(starter);
        new ChoiceGalleryReceiver(choiceGallery.getContext(), tag, choiceGallery.getCallback()).register();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_gallery);

        //find view
        Toolbar toolbar = findViewById(R.id.tool_bar);
        RecyclerView recyclerView = findViewById(R.id.list);
        mSlidingPaneLayout = findViewById(R.id.container);
        mBtnChoiceComplete = findViewById(R.id.btn_choice_complete);
        mBtnChoiceComplete.setOnClickListener(this::onCompleteButtonClick);

        //get intent data
        mTag = getIntent().getStringExtra("tag");
        mMaxChoice = getIntent().getIntExtra("maxChoice", 0);
        List<String> choicePhoto = getIntent().getStringArrayListExtra("choiceList");

        //标题
        toolbar.setTitle("选择图片");
        setSupportActionBar(toolbar);

        //照片
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mPhotoAdapter = new PhotoAdapter(this::onPhotoItemClick);
        mPhotoAdapter.setChoicePhotos(choicePhoto);
        recyclerView.setAdapter(mPhotoAdapter);

        //完成按钮
        mBtnChoiceComplete.setText((mPhotoAdapter.getChoicePhotoCount() + "/" + mMaxChoice));
        int[] colors = new int[]{Color.parseColor("#333333"), Color.parseColor("#CCCCCC")};
        int[][] states = {{android.R.attr.state_enabled}, {}};
        mBtnChoiceComplete.setBackgroundTintList(new ColorStateList(states, colors));
        mBtnChoiceComplete.setEnabled(mPhotoAdapter.getChoicePhotoCount() > 0);

        mProgressDialog = new ProgressDialog(this).setTipsMsg("正在扫描图片", true);

        //注册接收事件
        EventBus.getDefault().register(this);

        //扫描图片
        startService(new Intent(this, ScanningLocalPhotoService.class));

    }

    /**
     * 扫面本地图片完成
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanningLocalPhotoCompleteEvent(List<PhotoData> data) {
        EventBus.getDefault().unregister(this);

        //初始化目录
        ListView listView = findViewById(R.id.list_view);
        listView.setChoiceMode(CHOICE_MODE_SINGLE);
        ArrayAdapter<PhotoData> catalogAdapter = new ArrayAdapter<>(this, R.layout.item_gallery_catalog, R.id.text);
        listView.setAdapter(catalogAdapter);
        catalogAdapter.addAll(data);
        listView.setOnItemClickListener((parent, view, position, id) -> onCatalogItemClick(catalogAdapter, position));

        //
        listView.performItemClick(listView.getChildAt(0), 0, 0);
    }

    /**
     * 目录点击事件
     */
    private void onCatalogItemClick(ArrayAdapter<PhotoData> catalogAdapter, int position) {
        mPhotoAdapter.setData(Objects.requireNonNull(catalogAdapter.getItem(position)).getPhotoList());
    }

    /**
     * 照片点击事件
     */
    private void onPhotoItemClick(PhotoAdapter.PhotoHolder holder, int position) {
        boolean checked = mPhotoAdapter.getChecked(position);
        if (!checked && mPhotoAdapter.getChoicePhotoCount() >= mMaxChoice) {
            MainApplication.errToast("最多只能选择" + mMaxChoice + "张图片");
        } else {
            mPhotoAdapter.setChecked(position, !checked);
            mBtnChoiceComplete.setText((mPhotoAdapter.getChoicePhotoCount() + "/" + mMaxChoice));
            mBtnChoiceComplete.setEnabled(mPhotoAdapter.getChoicePhotoCount() > 0);
        }

        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        }
    }

    /**
     * 完成点击事件
     */
    private void onCompleteButtonClick(View view) {
        ChoiceGalleryReceiver.post(this, mTag, mPhotoAdapter.getChoicePhotos());
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

    @Override
    public void onBackPressed() {
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        } else {
            super.onBackPressed();
            //返回也发送广播,触发注销广播方法
            ChoiceGalleryReceiver.post(this, mTag, new ArrayList<>());
        }
    }
}
