package com.github.application.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.main.Constants;
import com.github.application.utils.UnitUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhongXiaolong on 2019/7/4 14:51.
 *
 * 图片选择器
 */
public class ChoiceGallery extends Dialog {

    public interface OnResultListener{
        void onResult(List<String> photos);
    }

    private ProgressDialog mProgressDialog;
    private Map<String, List<String>> mGruopMap;
    private PhotoAdapter mPhotoAdapter;

    public ChoiceGallery(@NonNull Context context) {
        super(context, R.style.AppTheme_CircumorbitalRing);

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
            getWindow().setStatusBarColor(Color.parseColor("#333333"));
        }

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setId(R.id.list);
        setContentView(recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mPhotoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(mPhotoAdapter);
        mProgressDialog = new ProgressDialog(getContext()).setTipsMsg("正在扫描图片", true);
        mGruopMap = new HashMap<>();
        recyclerView.post(this::getImages);

    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContext().getContentResolver();

        //只查询jpeg和png的图片
        Cursor cursor = contentResolver.query(imageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg","image/jpg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

        if(cursor == null){
            return;
        }

        while (cursor.moveToNext()) {
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();


            //根据父路径名将图片放入到mGruopMap中
            if (!mGruopMap.containsKey(parentName)) {
                List<String> chileList = new ArrayList<>();
                chileList.add(path);
                mGruopMap.put(parentName, chileList);
            } else {
                mGruopMap.get(parentName).add(path);
            }
        }

        //通知Handler扫描图片完成
        Log.d("ChoiceGallery", mGruopMap.toString());
        for (Map.Entry<String, List<String>> stringListEntry : mGruopMap.entrySet()) {

            mPhotoAdapter.addAll(stringListEntry.getValue());
        }
        cursor.close();
    }

    private class PhotoAdapter extends BaseAdapter<String>{

        final int size = UnitUtils.displayWidth() / 4;

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SquareImageView image = new SquareImageView(parent.getContext());
            RecyclerView.LayoutParams lp;
            lp = new RecyclerView.LayoutParams(Constants.MATCH_PARENT, Constants.MATCH_PARENT);
            lp.setMargins(5, 5, 5, 5);
            image.setLayoutParams(lp);
            image.setBackgroundColor(Color.parseColor("#EDEDED"));
            image.setId(R.id.image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return BaseHolder.instance(image);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            ImageView image = holder.findViewById(R.id.image);
            final File file = new File(get(position));
            Picasso.get().load(file).resize(size,size).centerCrop().into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    image.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            });
//            image.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = new ImageView(v.getContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(imageView);
                    dialog.show();
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
