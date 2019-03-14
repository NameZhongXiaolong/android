package priv.xiaolong.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;

import java.util.ArrayList;

import indi.dependency.packet.base.BaseActivity;
import indi.dependency.packet.map.KeyValue;
import priv.xiaolong.app.basics.BasicFragment;
import priv.xiaolong.app.head.HeadFragment;
import priv.xiaolong.app.senior.SeniorFragment;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements HeadFragment.OnSelectMenuListener {

    /*SharedPrefs名*/private final String SHAREDPREFS = this.getClass().getName();
    /*默认的Fragment下标*/private final String HOME_PAGE_INDEX = "home_page";
    /*Fragment(主页)*/private static KeyValue<String, Fragment> mMainPage = new KeyValue<>();

    static {
        mMainPage.put("Basic", new BasicFragment());
        mMainPage.put("Senior", new SeniorFragment());
    }

    private SharedPreferences mSp;
    private DrawerLayout mDrawerLayout;
    private ImageView mIvHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getWindow().setBackgroundDrawableResource(R.color.white);

        mSp = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);

        int homepageIndex = mSp.getInt(HOME_PAGE_INDEX, 0);
        if (homepageIndex >= mMainPage.size() || homepageIndex < 0) homepageIndex = 0;

        //侧滑菜单以及设置侧滑菜单点击时切换主页
        HeadFragment headFragment = HeadFragment.newInstance((ArrayList<String>) mMainPage.getKeys(), homepageIndex);
        headFragment.setOnSelectMenuListener(this);
        getFmTransaction().add(R.id.content_hean, headFragment).commitAllowingStateLoss();

        //默认主页(从SharedPreference中读取)
        getFmTransaction().add(R.id.content_main, mMainPage.getValue(homepageIndex)).commitAllowingStateLoss();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mIvHead.setOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /** 返回头像 */
    public ImageView getHeadImage() {
        return mIvHead;
    }

    /**
     * 设置主页
     */
    @Override
    public void onSelect(String key) {
        mSp.edit().putInt(HOME_PAGE_INDEX, mMainPage.indexOf(key)).commit();
        getFmTransaction().replace(R.id.content_main, mMainPage.getValue(key)).commitAllowingStateLoss();
        mDrawerLayout.closeDrawers();
    }

////////////////////////////////////////////授权/////////////////////////////////////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();
        verifyPermissions(this);
    }

    //android M以上权限获取（运行时权限）
    private static final int REQUEST_PERMISSION = 4526;


    /**
     * 授权
     *
     * @param activity
     */
    private void verifyPermissions(Activity activity) {
        final String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.SEND_SMS,
//            Manifest.permission.CAMERA,
//            Manifest.permission.CALL_PHONE,
        };
        for (int i = 0; i < permissions.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(activity, permissions[i]);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showPermissionAsk();
                    break;
                }
            }
        }
    }

    /**
     * 显示授权提示框
     */
    private void showPermissionAsk() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("一定要给上述的权限,否则不能使用!")
                .setPositiveButton("确定", (dialog, which) -> { startSystemPermissions(); })
                .setOnKeyListener((dialog, keyCode, event) -> noPermissionExit(keyCode))
                .setCancelable(false)
                .show();
    }

    /**
     * 进入系统授权页面
     */
    private void startSystemPermissions() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivityForResult(localIntent, 100);
    }

    /**
     * 不给权限不能使用直接退出应用
     */
    private boolean noPermissionExit(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.this.finish();
            return true;
        } else return false;
    }

}
