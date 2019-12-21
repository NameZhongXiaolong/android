package com.github.application.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.utils.UnitUtils;
import com.github.application.view.ActionBarView;
import com.github.application.view.picasso.CornerRadiusTransform;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by ZhongXiaolong on 2019/4/12 9:18.
 */
public class TestActivity extends MultipleThemeActivity implements ActionBarView.MenuItemClickListener {

    private EditText mEditText;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetFont(v);
            }
        });

        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        actionBarView.addMenuItem(0,android.R.drawable.ic_input_add,this);


        mEditText = findViewById(R.id.edit);
        mImageView = findViewById(R.id.image_view);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp2px(8));
        gradientDrawable.setColor(Color.parseColor("#EDEDED"));
        Picasso.get()
                .load("https://raw.githubusercontent.com/NameZhongXiaolong/beauty/master/one/image_c.jpg")
                .placeholder(gradientDrawable)
                .error(gradientDrawable)
                .transform(new CornerRadiusTransform(dp2px(8)))
                .resize(UnitUtils.displayWidth(), UnitUtils.px(200))
                .centerCrop(Gravity.START)
                .into(mImageView);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#F5F5F5"));
        background.setCornerRadius(dp2px(8));
        mEditText.setBackground(background);
        String textStr = "北京市发布霾黄色预警，<h3><font color='#ff0000'>外出携带好</font></h3>口罩";
        SpannableString spannableString = new SpannableString(mEditText.getText().toString());
        mEditText.setText(Html.fromHtml(textStr));
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable drawable = getResources().getDrawable(id);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
        mEditText.append(Html.fromHtml("<img src='" + R.mipmap.ic_launcher + "'/>", imageGetter, null));
        mEditText.setText(spannableString);
        mEditText.setSelection(mEditText.length());
    }

    private void onSetFont(View v) {
        final Integer[] items = {16, 18, 19, 20, 21};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.item_menu_3, R.id.text, items);
        new AlertDialog.Builder(this).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float textSize = mEditText.getTextSize();
                float newTextSize = sp2px(items[which]);

                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(newTextSize / textSize);
                SpannableString text = new SpannableString(mEditText.getText().toString());
                text.setSpan(sizeSpan,mEditText.getSelectionStart(), mEditText.getSelectionEnd(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mEditText.setText(text);
            }
        }).show();
    }

    @Override
    public void onMenuItemClick(int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }else{
            Intent intent = new Intent();
            // 开启Pictures画面Type设定为image
            intent.setType("image/*");
            // 使用Intent.ACTION_GET_CONTENT这个Action
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // 取得相片后返回本画面
            startActivityForResult(intent, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1000){
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        // 开启Pictures画面Type设定为image
                        intent.setType("image/*");
                        // 使用Intent.ACTION_GET_CONTENT这个Action
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        // 取得相片后返回本画面
                        startActivityForResult(intent, 100);
                    }else{
                        Toast.makeText(this, "请先授权", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选取图片
        if (requestCode == 100&&resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            //图片裁剪
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Drawable d = getResources().getDrawable(R.mipmap.ic_launcher);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

                ImageSpan imageSpan = new ImageSpan(this, bitmap);
//                imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                SpannableString image = new SpannableString("a");
                image.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mEditText.append("\r");
                mEditText.append(image);
                mEditText.append("\n");
                mEditText.setSelection(mEditText.length());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap scaleBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }

        int newWidth = getResources().getDisplayMetrics().widthPixels;

        int height = origin.getHeight();
        int width = origin.getWidth();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = height * width/scaleWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    //    public ImageSpan(Context context, Bitmap b, int verticalAlignment) {
//        super(verticalAlignment);
//        mContext = context;
//        mDrawable = context != null
//                ? new BitmapDrawable(context.getResources(), b)
//                : new BitmapDrawable(b);
//        int width = mDrawable.getIntrinsicWidth();
//        int height = mDrawable.getIntrinsicHeight();
//        mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
//    }
}
