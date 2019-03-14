package priv.xiaolong.app.basics.popup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import indi.dependency.packet.base.BaseActivity;
import indi.dependency.packet.util.ViewUtils;
import priv.xiaolong.app.R;

/**
 * 各种弹出框
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:06.
 */
public class _PopupAct extends BaseActivity {

    @ColorInt private final int COLOR_ONE = Color.rgb(230, 230, 250);
    @ColorInt private final int COLOR_TWO = Color.rgb(255, 245, 247);
    private boolean tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(LayoutInflater.from(this).inflate(R.layout.action_bar, root, false));
        NestedScrollView scrollView = new NestedScrollView(this);
        scrollView.setBackgroundColor(Color.parseColor("#F9F9F9"));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(layout, new ScrollView.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        root.addView(scrollView, new ScrollView.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        setContentView(root);

        setSupportActionBar(R.id.action_bar);
        getSupportActionBar().setTitle("弹出框");
        setWindowThemeColor(Color.rgb(222, 125, 44));

        //AlertDialog
        LinearLayout alertDialog = menuLayout(layout);
        ImpAlertDialogClickListener adcl = new ImpAlertDialogClickListener();
        createTitle(alertDialog, "AlertDialog(推荐用v7包的)");
        createButton(alertDialog, "show方法", adcl.ALERTDIALOG_SHOW).setOnClickListener(adcl);
        createButton(alertDialog, "create方法", adcl.ALERTDIALOG_CREATE).setOnClickListener(adcl);
        createButton(alertDialog, "设置透明背景", adcl.ALERTDIALOG_TRANSPARENT).setOnClickListener(adcl);
        createButton(alertDialog, "自定义布局", adcl.ALERTDIALOG_CUSTOM).setOnClickListener(adcl);
        createButton(alertDialog, "底部弹出", adcl.ALERTDIALOG_BOTTOM).setOnClickListener(adcl);

        //DialogFragment
        LinearLayout dialogFragment = menuLayout(layout);
        ImpDialogFragmentClickListener dfcl = new ImpDialogFragmentClickListener();
        createTitle(dialogFragment, "DialogFragment");
        createButton(dialogFragment, "正常普通", dfcl.DialogFragment_COMM).setOnClickListener(dfcl);
        createButton(dialogFragment, "无标题", dfcl.DialogFragment_NO_TITLE).setOnClickListener(dfcl);
        createButton(dialogFragment, "全屏", dfcl.DIALOGFRAGMENT_FULL_SCREEN).setOnClickListener(dfcl);
        createButton(dialogFragment, "自定义宽高", dfcl.DIALOGFRAGMENT_WIDTH_HEIGHT).setOnClickListener(dfcl);
        createButton(dialogFragment, "透明背景", dfcl.DialogFragment_TRANSPARENT).setOnClickListener(dfcl);
        createButton(dialogFragment, "底部弹出", dfcl.DialogFragment_BOTTOM).setOnClickListener(dfcl);

        //PopupWindow
        LinearLayout popupWindow = menuLayout(layout);
        ImpPopupWindowClickListener pwcl = new ImpPopupWindowClickListener();
        createTitle(popupWindow, "PopupWindow");
        createButton(popupWindow, "正常普通", pwcl.POPUPWINDOW_COMM).setOnClickListener(pwcl);
        createButton(popupWindow, "底部弹出", pwcl.POPUPWINDOW_BOTTOM_SHEET).setOnClickListener(pwcl);

        //MaterialDesign
        LinearLayout materialDesign = menuLayout(layout);
        ImpMaterialDesignClickListener mdcl = new ImpMaterialDesignClickListener();
        createTitle(materialDesign, "MaterialDesign");
        createButton(materialDesign, "BottomSheetDialog", mdcl.BOTTOM_SHEET_DIALOG).setOnClickListener(mdcl);
        createButton(materialDesign, "BottomSheetDialogDialogFragment", mdcl.BOTTOM_SHEET_DIALOG_FRAGMENT)
                .setOnClickListener(mdcl);

        //Activity弹出框
        LinearLayout activityPop = menuLayout(layout);
        createTitle(activityPop, "Activity弹出框");
        final Intent intent = new Intent(getActivity(), PopupActivity.class);
        createButton(activityPop, "点击", 0).setOnClickListener(v -> startActivity(intent));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private LinearLayout menuLayout(LinearLayout parent) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 35, 20, 35);
        layout.setBackgroundColor(tag ? COLOR_ONE : COLOR_TWO);
        parent.addView(layout, new LinearLayout.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT));
        tag = !tag;
        return layout;
    }

    /**
     * 设置按钮
     */
    private View createButton(LinearLayout viewGroup, String text, @IdRes int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.button_main, viewGroup, false);
        ((TextView) view.findViewById(R.id.button_name)).setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT);
        lp.setMargins(18, 12, 18, 0);
        viewGroup.addView(view, lp);
        view.setId(id);
        return view;
    }

    private void createTitle(LinearLayout viewGroup, String text) {
        TextView textView = new TextView(this);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setTextSize(20);
        textView.setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT);
        lp.setMargins(18, 0, 18, 5);
        viewGroup.addView(textView, lp);
    }

}
