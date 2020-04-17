package com.github.application.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.base.choice.gallery.ChoiceGallery;
import com.github.application.utils.DialogUtils;
import com.github.application.utils.UnitUtils;
import com.github.application.view.ActionBarView;
import com.github.application.view.FreeImageView;
import com.github.application.view.picasso.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2020/4/17 11:11.
 * <p>
 * 多图片拼接
 */
public class PlaneGraphActivity extends MultipleThemeActivity {

    private ImageView mIvBackground;
    private FrameLayout mFlParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_graph);

        mIvBackground = findViewById(R.id.image1);
        mFlParent = findViewById(R.id.parent);
        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        findViewById(R.id.view).setVisibility(hasNavigationBar() ? View.VISIBLE : View.GONE);
        actionBarView.addMenuItem(0, R.drawable.ic_ellipsis,
                id -> DialogUtils.bottomSheetMenu(this, this::onAddImageClick, "设置背景", "添加图片").show());

    }

    /**
     * 添加图片
     */
    private void onAddImageClick(Dialog dialog, int position, String menu) {
        dialog.dismiss();
        if (position == 0) {
            new ChoiceGallery(this).setMaxChoice(1).setCallback(this::onBackgroundChoiceGalleryComplete).start();
        }
        if (position == 1) {
            new ChoiceGallery(this).setMaxChoice(9).setCallback(this::onContentChoiceGalleryComplete).start();
        }
    }

    public void onBackgroundChoiceGalleryComplete(List<String> photos) {
        String url = photos.get(0);
        Picasso.get().load(new File(url)).into(mIvBackground);

    }

    public void onContentChoiceGalleryComplete(List<String> photos) {
        for (String photo : photos) {
            FreeImageView image = new FreeImageView(this);
            mFlParent.addView(image, new FrameLayout.LayoutParams(UnitUtils.px(120), UnitUtils.px(120), Gravity.CENTER));
            Picasso.get().load(new File(photo))
                    .transform(new CircleTransform())
                    .resize(UnitUtils.px(120), UnitUtils.px(120))
                    .centerCrop()
                    .into(image);
        }
    }

    /**
     * 显示底部弹出按钮弹窗
     */
    private boolean showButtonDialog(View view) {
        DialogUtils.bottomSheetMenu(this, new DialogUtils.OnDialogCallBack() {
            @Override
            public void onButtonClick(Dialog dialog, int position, String menu) {
                Toast.makeText(PlaneGraphActivity.this, menu, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }, "保存", "不要", "照片").show();
        return true;
    }

    private void screenshotLoad(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.TRANSPARENT);
        view.draw(c);

        String child = "Screenshots/" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), child);

        try {
            boolean mkdirs = true;
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                mkdirs = file.getParentFile().mkdirs();
            }

            if (mkdirs && file.createNewFile()) {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                //通知系统刷新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + file.getAbsolutePath())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
