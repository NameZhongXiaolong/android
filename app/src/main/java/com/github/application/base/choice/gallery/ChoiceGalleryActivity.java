package com.github.application.base.choice.gallery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
import static com.github.application.base.choice.gallery.PhotoAdapter.OnItemClickType.CHECKED;

/**
 * Created by ZhongXiaolong on 2019/12/30 17:16.
 *
 * 使用方法{@link ChoiceGallery},不要直接调用本Activity
 */
public class ChoiceGalleryActivity extends AppCompatActivity {

    private PhotoAdapter mPhotoAdapter;
    private Button mBtnChoiceComplete;
    private Button mBtnPreview;
    private ListView mListView;
    private Button mBtnListView;
    private ViewGroup mListViewParent;
    private TextView mTvTitle;
    private ImageButton mIbTitleIcon;
    private String mTag;
    private int mMaxChoice;
    private long mCatalogSelectClickTimeMillis;
    private ProgressDialog mProgressDialog;

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
        mBtnChoiceComplete = findViewById(R.id.button_1);
        mBtnPreview = findViewById(R.id.button_2);
        mListView = findViewById(R.id.list_view);
        mListViewParent = findViewById(R.id.container);
        mBtnListView = findViewById(R.id.button_3);
        mTvTitle = findViewById(R.id.text);
        mIbTitleIcon = findViewById(R.id.image_button);

        //onClick
        mBtnListView.setOnClickListener(this::onCatalogSelectClick);
        mTvTitle.setOnClickListener(this::onCatalogSelectClick);
        mIbTitleIcon.setOnClickListener(this::onCatalogSelectClick);
        mBtnChoiceComplete.setOnClickListener(this::onCompleteButtonClick);

        //get intent data
        mTag = getIntent().getStringExtra("tag");
        mMaxChoice = getIntent().getIntExtra("maxChoice", 0);
        List<String> choicePhoto = getIntent().getStringArrayListExtra("choiceList");

        //标题
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //照片
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mPhotoAdapter = new PhotoAdapter(this::onPhotoItemClick);
        mPhotoAdapter.setChoicePhotos(choicePhoto);
        recyclerView.setAdapter(mPhotoAdapter);

        //完成按钮
        mBtnChoiceComplete.setText(("完成(" + mPhotoAdapter.getChoicePhotoCount() + "/" + mMaxChoice + ")"));
        mBtnChoiceComplete.setEnabled(mPhotoAdapter.getChoicePhotoCount() > 0);
        mBtnPreview.setEnabled(mBtnChoiceComplete.isEnabled());

        //预览按钮
        int[] colors = new int[]{Color.parseColor("#333333"), Color.parseColor("#CCCCCC")};
        int[][] states = {{android.R.attr.state_enabled}, {}};
        mBtnPreview.setTextColor(new ColorStateList(states, colors));
        mBtnPreview.setOnClickListener(this::onStartPreview);

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
        mListView.setChoiceMode(CHOICE_MODE_SINGLE);
        ArrayAdapter<PhotoData> catalogAdapter = new ArrayAdapter<>(this, R.layout.item_gallery_catalog, R.id.text);
        mListView.setAdapter(catalogAdapter);
        catalogAdapter.addAll(data);
        mListView.setOnItemClickListener((parent, view, position, id) -> onCatalogItemClick(catalogAdapter, position));
        GradientDrawable listViewBackground = new GradientDrawable();
        float dp10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, getResources().getDisplayMetrics());
        listViewBackground.setCornerRadii(new float[]{0, 0, 0, 0, dp10, dp10, dp10, dp10});
        listViewBackground.setColor(Color.WHITE);
        mListView.setBackground(listViewBackground);

        //显示预览按钮
        findViewById(R.id.linear_layout).setVisibility(View.VISIBLE);

        //显示目录展开按钮
        mIbTitleIcon.setVisibility(View.VISIBLE);

        //设置默认点击项
        mListView.performItemClick(mListView.getChildAt(0), 0, 0);

    }

    /**
     * 目录Item点击事件
     */
    private void onCatalogItemClick(ArrayAdapter<PhotoData> catalogAdapter, int position) {
        mPhotoAdapter.setData(Objects.requireNonNull(catalogAdapter.getItem(position)).getPhotoList());
        mTvTitle.setText(Objects.requireNonNull(catalogAdapter.getItem(position)).getChineseCatalog());
        if (mListView.getVisibility() == View.VISIBLE) mListView.postDelayed(() -> onCatalogSelectClick(null), 200);
    }

    /**
     * 照片点击事件
     */
    private void onPhotoItemClick(PhotoAdapter.OnItemClickType type, int position) {
        if (type == CHECKED) {
            boolean checked = mPhotoAdapter.getChecked(position);
            if (!checked && mPhotoAdapter.getChoicePhotoCount() >= mMaxChoice) {
                MainApplication.errToast("最多只能选择" + mMaxChoice + "张图片");
            } else {
                mPhotoAdapter.setChecked(position, !checked);
                mBtnChoiceComplete.setText(("完成(" + mPhotoAdapter.getChoicePhotoCount() + "/" + mMaxChoice + ")"));
                mBtnChoiceComplete.setEnabled(mPhotoAdapter.getChoicePhotoCount() > 0);
                mBtnPreview.setEnabled(mBtnChoiceComplete.isEnabled());
            }
        } else {
            GalleryPreviewActivity.start(this, mPhotoAdapter.getData(), mPhotoAdapter.getChoicePhotos(), position, mMaxChoice);
        }
    }

    /**
     * 预览
     */
    private void onStartPreview(View view) {
        List<String> choicePhotos = mPhotoAdapter.getChoicePhotos();
        GalleryPreviewActivity.start(this, choicePhotos, choicePhotos, 0, mMaxChoice);
    }

    /**
     * 完成点击事件
     */
    private void onCompleteButtonClick(View view) {
        ChoiceGalleryReceiver.post(this, mTag, mPhotoAdapter.getChoicePhotos());
        finish();
    }

    /**
     * 显示/隐藏目录
     */
    private void onCatalogSelectClick(View view) {
        //防止连续点击
        long catalogSelectClickTimeMillis = System.currentTimeMillis();
        if (catalogSelectClickTimeMillis - mCatalogSelectClickTimeMillis < 350) {
            return;
        }
        mCatalogSelectClickTimeMillis = catalogSelectClickTimeMillis;

        Animation anim = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIbTitleIcon.setRotation((mIbTitleIcon.getRotation() + 180) % 360 == 0 ? 0 : 180);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setDuration(300); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        mIbTitleIcon.startAnimation(anim);

        if (mListView.getVisibility() == View.VISIBLE) {
            mListViewParent.postDelayed(() -> mListViewParent.setBackgroundColor(Color.TRANSPARENT), 300);
            TranslateAnimation translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, -1.0f);
            translate.setDuration(300);
            mListView.startAnimation(translate);
            mListView.setVisibility(View.GONE);
            mBtnListView.postDelayed(() -> mBtnListView.setVisibility(View.GONE), 200);
            mListViewParent.postDelayed(() -> mListViewParent.setVisibility(View.GONE), 350);
        } else {
            mListViewParent.setVisibility(View.VISIBLE);
            mListViewParent.postDelayed(() -> mListViewParent.setBackgroundColor(Color.parseColor("#80000000")), 200);
            TranslateAnimation translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f);
            translate.setDuration(300);
            mListView.startAnimation(translate);
            mListView.setVisibility(View.VISIBLE);
            mBtnListView.postDelayed(() -> mBtnListView.setVisibility(View.VISIBLE), 500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPreviewActivity.Code.REQUEST_CODE && data != null) {
            if (resultCode == GalleryPreviewActivity.Code.RESULT_OK) {
                //完成,回调
                List<String> photos = data.getStringArrayListExtra("choicePhotos");
                ChoiceGalleryReceiver.post(this, mTag, photos);
                finish();
            }
            if (resultCode == GalleryPreviewActivity.Code.RESULT_CANCEL) {
                //取消,更新 mPhotoAdapter
                List<String> photos = data.getStringArrayListExtra("choicePhotos");
                mPhotoAdapter.setChoicePhotos(photos);
                mBtnChoiceComplete.setText(("完成(" + mPhotoAdapter.getChoicePhotoCount() + "/" + mMaxChoice + ")"));
                mBtnChoiceComplete.setEnabled(mPhotoAdapter.getChoicePhotoCount() > 0);
                mBtnPreview.setEnabled(mBtnChoiceComplete.isEnabled());
            }
        }
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
        if (mListView.getVisibility() == View.VISIBLE) {
            onCatalogSelectClick(null);
        } else {
            super.onBackPressed();
            //返回也发送广播,触发注销广播方法
            ChoiceGalleryReceiver.post(this, mTag, new ArrayList<>());
        }
    }
}
