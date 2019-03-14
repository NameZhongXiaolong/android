package priv.xiaolong.app.basics.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import priv.xiaolong.app.R;

/**
 * BottomSheetDialogFragment,方法已经按生命周期排列
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2017/1/11 13:13.
 */
public class BottomSheetDialogFragmentBm extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TITLE = "title";
    private Context mContext;
    private EditText mEditText;

    /**
     * 静态工厂模式初始化
     */
    public static BottomSheetDialogFragmentBm newInstance(String title) {
        BottomSheetDialogFragmentBm bm = new BottomSheetDialogFragmentBm();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        bm.setArguments(args);
        return bm;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //版本判断,5.0以上去除顶部状态黑边
        boolean version5 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        if (version5) dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //设置外部是否可以点击(false不可点击\true可以点击)
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle instanceState) {
        return inflater.inflate(R.layout.pop_window, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("BottomSheetDialogFragment");

        //设置背景边框颜色(但不能设置透明颜色)
        view.setBackgroundColor(Color.parseColor("#FFFF00"));

        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        TextView tvHint = view.findViewById(R.id.tv_hint);
        mEditText = view.findViewById(R.id.et_input);

        tvHint.setText(getArguments().getString(TITLE));
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

}
