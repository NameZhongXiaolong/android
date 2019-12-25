package com.github.application.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.widget.ImageButton;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.ui.SettingActivity;
import com.github.application.utils.UnitUtils;
import com.github.application.view.ActionBarView;

/**
 * Main
 * <p>
 * 一些动画
 * //从上往下弹出的显示动画
 * TranslateAnimation translate = new TranslateAnimation(
 * Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, -1.0f,
 * Animation.RELATIVE_TO_SELF, 0.0f);
 * <p>
 * /*
 * //从下往上收缩的隐藏动画
 * TranslateAnimation translate = new TranslateAnimation(
 * Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, -1.0f);
 * <p>
 * //从下往上弹出的显示动画
 * TranslateAnimation animation2 = new TranslateAnimation(
 * Animation.RELATIVE_TO_PARENT, 0.0f,
 * Animation.RELATIVE_TO_PARENT, 0.0f,
 * Animation.RELATIVE_TO_PARENT, 1.0f,
 * Animation.RELATIVE_TO_PARENT, 0.0f);
 * <p>
 * //从上往下收缩的隐藏动画
 * TranslateAnimation animation2 = new TranslateAnimation(
 * Animation.RELATIVE_TO_PARENT, 0.0f,
 * Animation.RELATIVE_TO_PARENT, 0.0f,
 * Animation.RELATIVE_TO_PARENT, 0.0f,
 * Animation.RELATIVE_TO_PARENT, 1.0f);
 * translate.setDuration(500);
 */
public class MainActivity extends MultipleThemeActivity
        implements
        ActionBarView.MenuItemClickListener,
        ActionBarView.NavigationClickListener {

    private SlidingPaneLayout mSlidingPaneLayout;
    private boolean mWriteExternalStorage;//是否获取存储权限
    private AlertDialog mPermissionDialog;//权限弹出框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查权限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            create();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
            mPermissionDialog = new AlertDialog
                    .Builder(this)
                    .setTitle("提示")
                    .setMessage("需要存储权限才能使用app")
                    .setCancelable(false)
                    .setPositiveButton("去设置", (dialog, which) -> {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                            }
                        }else{
                            Intent starter = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            String pkg = "com.android.settings";
                            String cls = "com.android.settings.applications.InstalledAppDetails";
                            starter.setComponent(new ComponentName(pkg, cls));
                            starter.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(starter);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("退出", (dialog, which) -> {
                        dialog.dismiss();
                        new Handler().postDelayed(this::finish, 300);
                    })
                    .create();
            mPermissionDialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        create();
                    } else {
                        mPermissionDialog.show();
                        mPermissionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#E84393"));
                        mPermissionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#999999"));
                    }
                    break;
                }
            }
        }
    }

    /**
     * 检查权限
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (!mWriteExternalStorage) {
            int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                create();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mPermissionDialog.show();
                }
            }
        }
    }

    /**
     * 初始化
     */
    private void create() {
        mWriteExternalStorage = true;
        UnitUtils.displayHeight(this);
        setContentView(R.layout.activity_main);
        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        actionBarView.setNavigationClickListener(this);
        actionBarView.addMenuItem(0, R.drawable.ic_setting, this);
        mSlidingPaneLayout = findViewById(R.id.sliding_pane_layout);
        MainFragment fragment = new MainFragment();
        getFragmentTransaction().add(R.id.fm_container, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onMenuItemClick(int id) {
        startActivity(SettingActivity.class);
    }

    @Override
    public void onNavigationClick(ImageButton button) {
//        mSlidingPaneLayout.openPane();
        startActivity(MainActivity.class);
    }
}
