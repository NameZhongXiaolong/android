package priv.xiaolong.app.basics.material.design;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import indi.dependency.packet.base.BaseActivity;
import priv.xiaolong.app.R;

/**
 * MaterialDesign+四大组件
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:03.
 */
public class _MaterialDesignAct extends BaseActivity {

    private CoordinatorLayout mClParent;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_material_design);

        mClParent = findViewById(R.id.coordinator_layout);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        final NavigationView navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("material design");
        getSupportActionBar().setSubtitle("四大组件");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);

        //设置状态栏和ToolBar颜色
        int color = ContextCompat.getColor(this, R.color.materialDesign);
        setWindowStatusBarColor(color);
        collapsingToolbarLayout.setContentScrimColor(color);

        //设置ToolBar返回键点击展开侧滑菜单
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name).syncState();

        //设置FloatingActionButton点击事件
        findViewById(R.id.fab).setOnClickListener(v -> Snackbar.make(mClParent, "悬浮按钮", Snackbar.LENGTH_LONG).show());

        //设置NavigationViewde子项目(item)点击事件
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getTitle().equals("退出")) {
                _MaterialDesignAct.this.finish();
            } else {
                mDrawerLayout.closeDrawer(Gravity.START);
                Snackbar.make(mClParent, item.getTitle(), Snackbar.LENGTH_LONG).show();
            }
            return true;
        });

        //初始化Tablayout和ViewPager
        final String[] title = {"material design", "Activity", "Service", "Broadcast receivers", "content provider"};
        final Fragment[] fm = {new MaterialDesignFm(), new ActivityFm(),
                new ServiceFm(), new BroadcastReceiversFm(), new ContentProviderFm()};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fm[position];
            }

            @Override
            public int getCount() {
                return fm.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, 0, "exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            //设置父容器,提示,时长
            Snackbar.make(mClParent, "确定退出 ?", Snackbar.LENGTH_INDEFINITE)
                    //设置动作标题和点击事件
                    .setAction("退出", view -> finish())
                    //设置动作标题颜色
                    .setActionTextColor(Color.parseColor("#FF0000"))
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
