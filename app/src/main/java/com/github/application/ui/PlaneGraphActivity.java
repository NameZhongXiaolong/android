package com.github.application.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.base.choice.gallery.ChoiceGallery;
import com.github.application.main.MainApplication;
import com.github.application.utils.UnitUtils;
import com.github.application.view.picasso.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2020/4/17 11:11.
 *
 * 多图片拼接
 */
public class PlaneGraphActivity extends MultipleThemeActivity {

    private ImageView mIvContent;
    private ImageView mIvBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_graph);

        mIvBackground = findViewById(R.id.image1);
        mIvContent = findViewById(R.id.image2);
        findViewById(R.id.view).setVisibility(hasNavigationBar() ? View.VISIBLE : View.GONE);
        mIvBackground.setOnClickListener(v -> new ChoiceGallery(this).setMaxChoice(1).setCallback(this::onBackgroundChoiceGalleryComplete).start());
        mIvContent.setOnClickListener(v -> new ChoiceGallery(this).setMaxChoice(1).setCallback(this::onContentChoiceGalleryComplete).start());
        findViewById(R.id.parent).setOnLongClickListener(this::showButtonDialog);
        mIvBackground.setOnLongClickListener(v -> findViewById(R.id.parent).performLongClick());
        mIvContent.setOnLongClickListener(v -> findViewById(R.id.parent).performLongClick());

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#0984E3"));
        gradientDrawable.setShape(GradientDrawable.OVAL);
        mIvContent.setImageDrawable(gradientDrawable);
    }

    public void onBackgroundChoiceGalleryComplete(List<String> photos) {
        String url = photos.get(0);
        Picasso.get().load(new File(url)).into(mIvBackground);

    }

    public void onContentChoiceGalleryComplete(List<String> photos) {
        String url = photos.get(0);
        Picasso.get().load(new File(url))
                .transform(new CircleTransform())
                .resize(UnitUtils.px(120), UnitUtils.px(120))
                .centerCrop()
                .into(mIvContent);
    }

    /**
     * 显示底部弹出按钮弹窗
     */
    private boolean showButtonDialog(View view){
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_bottom_note);
        //设置大背景颜色
        View container = dialog.findViewById(R.id.design_bottom_sheet);
        if (container != null) container.setBackgroundColor(Color.TRANSPARENT);
        dialog.show();

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button button_1 = dialog.findViewById(R.id.button_1);
        if (btnCancel != null && button_1 != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            button_1.setOnClickListener(v -> {
                dialog.dismiss();
                screenshotLoad(view);
                MainApplication.outToast("保存成功");
            });
        }
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
