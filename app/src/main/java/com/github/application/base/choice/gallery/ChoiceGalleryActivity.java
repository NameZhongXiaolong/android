package com.github.application.base.choice.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.main.Constants;
import com.github.application.main.MainApplication;
import com.github.application.ui.ProgressDialog;
import com.github.application.utils.UnitUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ZhongXiaolong on 2019/12/30 17:16.
 */
public class ChoiceGalleryActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private Map<String, List<String>> mGruopMap;
    private ChoiceGalleryActivity.PhotoAdapter mPhotoAdapter;
    private Button mBtnChoiceComplete;
    private String mTag;
    private int mMaxChoice;

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

        Toolbar toolbar = findViewById(R.id.tool_bar);
        mBtnChoiceComplete = findViewById(R.id.btn_choice_complete);
        RecyclerView recyclerView = findViewById(R.id.list);

        mTag = getIntent().getStringExtra("tag");
        mMaxChoice = getIntent().getIntExtra("maxChoice", 0);
        List<String> choiceList = getIntent().getStringArrayListExtra("choiceList");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("选择图片");
        }

        if (choiceList != null) {
            mBtnChoiceComplete.setText((choiceList.size() + "/" + mMaxChoice));
            mBtnChoiceComplete.setBackgroundColor(Color.parseColor(choiceList.size() > 0 ? "#333333" : "#CCCCCC"));
        }

        mBtnChoiceComplete.setOnClickListener(v -> {
            ChoiceGalleryReceiver.post(this, mTag, mPhotoAdapter.mChoicePhotos);
            finish();
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mPhotoAdapter = new ChoiceGalleryActivity.PhotoAdapter(choiceList);
        recyclerView.setAdapter(mPhotoAdapter);
        mProgressDialog = new ProgressDialog(this).setTipsMsg("正在扫描图片", true);
        mGruopMap = new HashMap<>();
        recyclerView.post(this::getImages);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = this.getContentResolver();

        //只查询jpeg和png的图片
        Cursor cursor = contentResolver.query(imageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/jpg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

        if (cursor == null) {
            return;
        }

        while (cursor.moveToNext()) {
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            //获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile != null) {
                String parentName = parentFile.getName();
                if (!mGruopMap.containsKey(parentName)) {
                    List<String> chileList = new ArrayList<>();
                    chileList.add(path);
                    mGruopMap.put(parentName, chileList);
                } else {
                    Objects.requireNonNull(mGruopMap.get(parentName)).add(path);
                }
            }
        }

        //通知Handler扫描图片完成
        for (Map.Entry<String, List<String>> stringListEntry : mGruopMap.entrySet()) {
            mPhotoAdapter.addAll(stringListEntry.getValue());
        }
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ChoiceGalleryReceiver.post(this, mTag, new ArrayList<>());
    }

    private class PhotoAdapter extends BaseAdapter<String> {

        final int size = UnitUtils.displayWidth() / 4;
        private List<String> mChoicePhotos;

        PhotoAdapter(List<String> choicePhotos) {
            mChoicePhotos = choicePhotos != null ? choicePhotos : new ArrayList<>();
        }

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FrameLayout itemView = new FrameLayout(parent.getContext());
            RecyclerView.LayoutParams lp;
            lp = new RecyclerView.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT);
            lp.setMargins(5, 5, 5, 5);
            itemView.setLayoutParams(lp);

            ChoiceGalleryActivity.SquareImageView image =
                    new ChoiceGalleryActivity.SquareImageView(parent.getContext());
            image.setBackgroundColor(Color.parseColor("#EDEDED"));
            image.setId(R.id.image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemView.addView(image, new FrameLayout.LayoutParams(Constants.MATCH_PARENT, Constants.MATCH_PARENT));

            Button clickedView = new Button(parent.getContext());
            clickedView.setBackgroundColor(Color.parseColor("#80000000"));
            clickedView.setTextSize(16);
            clickedView.setTextColor(Color.WHITE);
            clickedView.setPadding(UnitUtils.px(10), 0, UnitUtils.px(10), 0);
            clickedView.setGravity(Gravity.TOP | Gravity.END);
            clickedView.setId(R.id.button);
            clickedView.setVisibility(View.GONE);

            itemView.addView(clickedView, new FrameLayout.LayoutParams(Constants.MATCH_PARENT, Constants.MATCH_PARENT));

            return BaseHolder.instance(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            ImageView image = holder.findViewById(R.id.image);
            Button clickedView = holder.findViewById(R.id.button);
            final File file = new File(get(position));
            Picasso.get().load(file).resize(size, size).centerCrop().into(image);
            int index = mChoicePhotos.indexOf(get(position));
            clickedView.setVisibility(index >= 0 ? View.VISIBLE : View.GONE);
            clickedView.setText(String.valueOf(index + 1));

            image.setOnClickListener(v -> {
                if (mChoicePhotos.size() >= mMaxChoice) {
                    MainApplication.errToast("最多只能选择" + mMaxChoice + "张图片");
                    return;
                }
                mChoicePhotos.add(get(position));
                mBtnChoiceComplete.setText((mChoicePhotos.size() + "/" + mMaxChoice));
                notifyItemChanged(position);
                if (mChoicePhotos.size() == 1) {
                    mBtnChoiceComplete.setBackgroundColor(Color.parseColor("#333333"));
                }
            });
            clickedView.setOnClickListener(v -> {
                String removeData = get(position);
                int i = mChoicePhotos.indexOf(removeData);
                if (i >= 0) {
                    mChoicePhotos.remove(i);
                    notifyDataSetChanged();
                    int notifyItem = mData.indexOf(removeData);
                    if (notifyItem >= 0) notifyItemChanged(notifyItem);
                    for (int j = i; j < mChoicePhotos.size(); j++) {
                        notifyItem = mData.indexOf(mChoicePhotos.get(j));
                        if (notifyItem >= 0) notifyItemChanged(j);
                    }
                }
                mBtnChoiceComplete.setText((mChoicePhotos.size() + "/" + mMaxChoice));
                if (mChoicePhotos.size() == 0) {
                    mBtnChoiceComplete.setBackgroundColor(Color.parseColor("#CCCCCC"));
                }
            });
        }
    }

    private class SquareImageView extends AppCompatImageView {

        public SquareImageView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // 设置View宽高的测量值
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                    getDefaultSize(0, heightMeasureSpec));
            // 只有setMeasuredDimension调用之后，才能使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量出的宽高，以此之前调用这两个方法得到的值都会是0
            int childWidthSize = getMeasuredWidth();
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
