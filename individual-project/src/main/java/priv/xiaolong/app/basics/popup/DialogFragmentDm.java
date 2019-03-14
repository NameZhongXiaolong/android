package priv.xiaolong.app.basics.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import priv.xiaolong.app.R;

/**
 * DialogFragment弹出框,方法已经按生命周期排列
 * 获取父容器的方法:
 * 1.Activity: ((parentActivity)mContext).method();
 * 2.Fragment: ((parentFragment)getParentFragment()).method();
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2016/12/23 15:40.
 */
public class DialogFragmentDm extends DialogFragment implements View.OnClickListener {

    private static final String HINT = "hint";
    private Context mContext;
    private EditText mEditText;
    private boolean mSetSize;
    private boolean mBottomSheet;
    private boolean mTitle;
    private boolean mFullScreen;
    private @ColorInt int mBackgroundColor = Color.parseColor("#F9F9F9");

    /**
     * 静态工厂模式初始化
     */
    public static DialogFragmentDm getInstance() {
        DialogFragmentDm fm = new DialogFragmentDm();
        Bundle args = new Bundle();
        args.putString(HINT, "标题");
        fm.setArguments(args);
        return fm;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        if (mFullScreen) {
            setStyle(STYLE_NO_FRAME, R.style.TransparentAppTheme);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //设置外部是否可以点击(false不可点击\true可以点击)
        dialog.setCanceledOnTouchOutside(true);

        //设置弹出框颜色
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(mBackgroundColor));

        //设置无标题,将会使Dialog变形,6.0以上默认无标题
        if (mTitle) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        //设置底部弹出
        if (mBottomSheet) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.PopAnimation;
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }

        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pop_window, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置view颜色即是背景颜色
        view.setBackgroundColor(mBackgroundColor);

        //设置标题在6.0一下才有
        getDialog().setTitle("标题提示");

        //自动打开键盘
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        TextView tvHint = view.findViewById(R.id.tv_hint);
        mEditText = view.findViewById(R.id.et_input);

        tvHint.setText(getArguments().getString(HINT));

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置窗口大小
        if (mSetSize) {
            Dialog dialog = getDialog();
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //设置阴影透明度
        //Window window = getDialog().getWindow();
        //WindowManager.LayoutParams lp = window.getAttributes();
        //lp.dimAmount = 0.1f;
        //window.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                Toast.makeText(getActivity(), mEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 根据屏幕百分比设置尺寸
     *
     * @return
     */
    public DialogFragmentDm setWidthHeight() {
        mSetSize = true;
        return this;
    }

    /**
     * 设置底部弹出
     *
     * @return
     */
    public DialogFragmentDm setBottomSheet() {
        mBottomSheet = true;
        return this;
    }

    /**
     * 设置无标题
     *
     * @return
     */
    public DialogFragmentDm setNoTitle() {
        mTitle = true;
        return this;
    }

    /**
     * 设置全屏
     *
     * @return
     */
    public DialogFragmentDm setFullScreen() {
        mFullScreen = true;
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @param color
     *
     * @return
     */
    public DialogFragmentDm setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        return this;
    }
}