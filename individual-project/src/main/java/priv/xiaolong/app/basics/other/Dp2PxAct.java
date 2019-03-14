package priv.xiaolong.app.basics.other;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

import indi.dependency.packet.base.ListActivity;
import priv.xiaolong.app.R;

/**
 * dp与px互转
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 17:07.
 */
public class Dp2PxAct extends ListActivity implements TextWatcher {

    private BottomSheetDialog mDialog;
    private TextView mTvInputUnit;
    private TextView mTvOutRes;
    private TextView mTvOutUnit;
    private int mTag;
    private EditText mEtInputNum;
    String[] menu = {"获取屏幕分辨率", "dp转px", "px转dp", "sp转px", "px转sp","获取手机信息"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new BottomSheetDialog(this);
        mDialog.setContentView(R.layout.dia_dp_px);
        boolean version5 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        if (version5) mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mEtInputNum = mDialog.findViewById(R.id.et_input_num);
        mTvInputUnit = mDialog.findViewById(R.id.tv_input_unit);
        mTvOutRes = mDialog.findViewById(R.id.tv_out_res);
        mTvOutUnit = mDialog.findViewById(R.id.tv_out_unit);
        mEtInputNum.addTextChangedListener(this);

        setListAdapter(new ArrayAdapter<>(this, R.layout.button_main, R.id.button_name, Arrays.asList(menu)));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (menu[position]) {
            case "获取屏幕分辨率":
                DisplayMetrics metrics = new DisplayMetrics();
                int width;
                int height;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                } else {
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                }
                width = metrics.widthPixels;
                height = metrics.heightPixels;
                getSupportActionBar().setTitle(width + " × " + height);
                return;
            case "dp转px":
                mTag = 1;
                mEtInputNum.setHint("输入dp");
                mEtInputNum.setText("");
                mTvInputUnit.setText("dp");
                mTvOutUnit.setText("px");
                break;

            case "px转dp":
                mTag = 2;
                mEtInputNum.setHint("输入px");
                mEtInputNum.setText("");
                mTvInputUnit.setText("px");
                mTvOutUnit.setText("dp");
                break;
            case "px转sp":
                mTag = 3;
                mEtInputNum.setHint("输入px");
                mEtInputNum.setText("");
                mTvInputUnit.setText("px");
                mTvOutUnit.setText("sp");
                break;
            case "sp转px":
                mTag = 4;
                mEtInputNum.setHint("输入sp");
                mEtInputNum.setText("");
                mTvInputUnit.setText("sp");
                mTvOutUnit.setText("px");
                break;
            case "获取手机信息":
                StringBuilder msg = new StringBuilder();
                msg.append("版本:  ").append(Build.MODEL).append("\n");
                msg.append("手机品牌: ").append(android.os.Build.MANUFACTURER).append("\n");
                msg.append("内核版本: ").append("API.").append(Build.VERSION.SDK_INT).append("\n");
                msg.append("主板名称: ").append(Build.BOARD).append("\n");
                msg.append("系统引导程序版本号: ").append(Build.BOOTLOADER).append("\n");
                msg.append("系统定制商: ").append(Build.BRAND).append("\n");
                msg.append("CPU和ABI的本地代码指令集: ").append(Build.CPU_ABI).append("\n");
                msg.append("设备参数:  ").append(Build.DEVICE).append("\n");
                msg.append("硬件名:  ").append(Build.FINGERPRINT).append("\n");
                msg.append("显示屏参数:  ").append(Build.DISPLAY).append("\n");
                msg.append("内核命令行中的硬件名:  ").append(Build.HARDWARE).append("\n");
                msg.append("硬件厂商:  ").append(Build.MANUFACTURER).append("\n");
                msg.append("手机厂商: ").append(Build.PRODUCT).append("\n");
                msg.append("INCREMENTAL: ").append(Build.VERSION.INCREMENTAL).append("\n");

                new AlertDialog.Builder(getContext()).setTitle("手机信息").setMessage(msg.toString()).show();
                return;
        }
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mDialog.show();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (TextUtils.isEmpty(editable.toString())) {
            mTvOutRes.setText("" + 0);
            return;
        }
        float input = Float.valueOf(editable.toString().trim());
        if (mTag == 1) mTvOutRes.setText("" + dp2px(input));
        if (mTag == 2) mTvOutRes.setText("" + px2dp(input));
        if (mTag == 3) mTvOutRes.setText("" + px2sp(input));
        if (mTag == 4) mTvOutRes.setText("" + sp2px(input));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(float pxValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    private int sp2px(float spValue) {
        float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    private int px2sp(float pxValue) {
        float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {/*..*/}

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { /*..*/ }

}
