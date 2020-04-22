package com.github.application.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.base.BaseSuperFragment;
import com.github.application.base.MultipleThemeActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2020/4/22 15:30.
 *
 * 图片预览/删除
 *
 */
public class PhotoPreviewActivity extends MultipleThemeActivity {

    private List<String> mPhotoList;
    private ViewPager mViewPager;
    private TextView mTextView;
    private String mReceiverTag;

    /**
     * 图片预览
     * @param context 上下文
     * @param photoUrl 图片路径
     * @param currentItem 选中项目
     */
    public static void start(Context context, List<String> photoUrl, int currentItem) {
        Intent starter = new Intent(context, PhotoPreviewActivity.class);
        starter.putStringArrayListExtra("data", new ArrayList<>(photoUrl));
        starter.putExtra("currentItem", currentItem);
        context.startActivity(starter);
    }

    /**
     * 图片预览,带删除功能
     * @param context 上下文
     * @param photoUrl 图片路径
     * @param currentItem 选中项目
     * @param callBack 修改回调,用到广播和广播接收,但使用者只关心{@link CompleteCallBack}实现即可
     */
    public static void start(Context context, List<String> photoUrl, int currentItem, CompleteCallBack callBack) {
        String tag = "tag" + System.currentTimeMillis();
        Intent starter = new Intent(context, PhotoPreviewActivity.class);
        starter.putStringArrayListExtra("data", new ArrayList<>(photoUrl));
        starter.putExtra("currentItem", currentItem);
        starter.putExtra("receiver_tag", tag);
        context.startActivity(starter);
        new CompleteReceiver(context, tag, callBack).register();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        final int currentItem = getIntent().getIntExtra("currentItem", 0);
        mReceiverTag = getIntent().getStringExtra("receiver_tag");
        mPhotoList = getIntent().getStringArrayListExtra("data");

        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new PhotoAdapter(getSupportFragmentManager(), mPhotoList));
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTextView = findViewById(R.id.text);
                mTextView.setText(((position + 1) + "/" + mPhotoList.size()));
            }
        });

        mTextView = findViewById(R.id.text);
        mTextView.setText(((currentItem + 1) + "/" + mPhotoList.size()));

        View deleteButton = findViewById(R.id.button);
        deleteButton.setVisibility(TextUtils.isEmpty(mReceiverTag) ? View.INVISIBLE : View.VISIBLE);
        deleteButton.setOnClickListener(this::onDeleteClick);
    }

    private void onDeleteClick(View view) {
        new CompatAlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要删除这张图片 ?")
                .setPositiveButton("确定", (dialog, v) -> deletePhoto())
                .setNegativeButton("取消", null)
                .show();
    }

    private void deletePhoto(){
        int currentItem = mViewPager.getCurrentItem();
        mPhotoList.remove(currentItem);
        if (mPhotoList.size() > 0) {
            mViewPager.setAdapter(new PhotoAdapter(getSupportFragmentManager(), mPhotoList));
            mViewPager.setCurrentItem(currentItem);
            mTextView.setText(((mViewPager.getCurrentItem() + 1) + "/" + mPhotoList.size()));
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(mReceiverTag)) {
            //设置Action
            Intent intent = new Intent(CompleteReceiver.ACTION);

            //传递数据
            intent.putExtra("tag", mReceiverTag);
            intent.putStringArrayListExtra("data", new ArrayList<>(mPhotoList));

            //发送广播
            sendBroadcast(intent);
        }
        finish();
    }

    private static class PhotoAdapter extends FragmentStatePagerAdapter{

        List<String> mPhotoList;

        private PhotoAdapter(FragmentManager fm, List<String> photoList) {
            super(fm);
            mPhotoList = photoList;
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(mPhotoList.get(position));
        }

        @Override
        public int getCount() {
            return mPhotoList != null ? mPhotoList.size() : 0;
        }
    }

    public static class PhotoFragment extends BaseSuperFragment{

         static PhotoFragment newInstance(String photoUrl) {
            Bundle args = new Bundle();
            args.putString("url",photoUrl);
            PhotoFragment fragment = new PhotoFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
            FrameLayout frameLayout = new FrameLayout(requireContext());
            ImageView imageView = new ImageView(requireContext());
            imageView.setId(R.id.image);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            frameLayout.addView(imageView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            return frameLayout;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ImageView imageView = view.findViewById(R.id.image);

            String photo = getArguments() != null ? getArguments().getString("url","") : "";
            File file = new File(photo);
            if (file.exists()) {
                Picasso.get().load(file).into(imageView);
            }else{
                Picasso.get().load(photo).into(imageView);
            }
        }
    }

    private static class CompleteReceiver extends BroadcastReceiver {
        private static final String ACTION = "com.github.application.ui.PhotoPreviewActivity.CompleteReceiver";
        private Context mContext;
        private CompleteCallBack mCompleteCallBack;
        private String mTag;

        public CompleteReceiver(Context context, String tag, CompleteCallBack call) {
            mContext = context;
            mTag = tag;
            mCompleteCallBack = call;
        }

        public void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION);
            mContext.registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> data = intent.getStringArrayListExtra("data");
            String tag = intent.getStringExtra("tag");
            if (mTag.equalsIgnoreCase(tag)) {
                if (data != null) {
                    mCompleteCallBack.onComplete(data);
                }
                mContext.unregisterReceiver(this);
            }
        }
    }

    interface CompleteCallBack{
        void onComplete(List<String> photoList);
    }
}
