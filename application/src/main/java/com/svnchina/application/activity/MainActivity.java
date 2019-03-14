package com.svnchina.application.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.svnchina.application.R;
import com.svnchina.application.base.ImageUrls;
import com.svnchina.application.base.SideslipSignOutActivity;
import com.svnchina.application.base.Utils;
import com.svnchina.application.fragment.MainFragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends SideslipSignOutActivity {

    private boolean tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSlideBackFinish(false);

        final ImageView image = findViewById(R.id.image_view);

        ImageButton setting = findViewById(R.id.btn_setting);
        FrameLayout actionView = (FrameLayout) setting.getParent();
        setting.setOnClickListener(v -> startActivityForResult(new Intent(this, SettingsActivity.class), 100));

        if (!SettingsActivity.getShowBackground(this)) {
            image.setVisibility(View.INVISIBLE);
            actionView.setVisibility(View.VISIBLE);
            getFragmentTransaction().add(R.id.main_fragment, new MainFragment()).commitAllowingStateLoss();
        } else {
            tag = true;
            showBeauty(image, actionView);
        }
    }

    //显示美女背景图
    private void showBeauty(ImageView image, View actionView) {

        //全屏显示(隐藏状态栏和导航栏)
        image.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //显示状态栏和导航栏
        image.postDelayed(() -> image.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION), 2000);

        //显示标题
        actionView.postDelayed(() -> {
            //从上往下弹出的显示动画
            TranslateAnimation translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);

            /*
              //从下往上收缩的隐藏动画
              TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -1.0f);



            //从下往上弹出的显示动画
            TranslateAnimation animation2 = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f);

            //从上往下收缩的隐藏动画
           TranslateAnimation animation2 = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f);
             */

            translate.setDuration(500);

            actionView.startAnimation(translate);
            actionView.setVisibility(View.VISIBLE);
            getFragmentTransaction().setCustomAnimations(R.anim.bottom_show, 0)
                    .add(R.id.main_fragment, new MainFragment()).commitAllowingStateLoss();
        }, 2500);

        //读取保存的路径,File存在就加载图片
        final File f = new File(getSharedPrefs().getString(KEY, KEY));
        boolean exists = f.exists();
        if (exists) Picasso.with(this).load(f).into(image);

        List<String> images = new Random().nextBoolean() ? ImageUrls.getOneFile() : ImageUrls.getTwoFile();

        //下载图片
        new Thread(() -> {
            try {
                //保存所有启动页图片的根目录
                final File root = new File(getFilesDir(), "/image/");
                root.mkdirs();

                //将下载的图片转成Bitmap
                String path = images.get(new Random().nextInt(images.size()));
                Bitmap bitmap = Picasso.with(getContext()).load(path).get();

                //设置保存的文件位置
                File file = new File(root, Long.toString(System.currentTimeMillis()));

                //将Bitmap存在File中
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                Log.d("MainActivity", "下载完成 路径:" + file.getAbsolutePath());

                //判断里面的root目录文件>2就删除旧的文件
                List<File> files = new ArrayList<>(Arrays.asList(root.listFiles()));
                Collections.sort(files, (o1, o2) -> (int) (o2.lastModified() - o1.lastModified()));

                for (int i = 0; i < files.size(); i++) if (i > 1) files.get(i).delete();

                //下载完成,保存路径
                getSharedPrefs().edit().putString(KEY, file.getAbsolutePath()).apply();

                //图片没设1置成功时候再次设置
                if (!exists) runOnUiThread(() -> Picasso.with(getContext()).load(file).into(image));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        image.postDelayed(() -> image.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION), 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageView image = findViewById(R.id.image_view);
            image.setVisibility(SettingsActivity.getShowBackground(this) ? View.VISIBLE : View.INVISIBLE);
            if (!tag) image.setImageBitmap(Utils.getImageFromAssetsFile(this, "image002.png"));
        }
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(false);
        finish();
    }
}
