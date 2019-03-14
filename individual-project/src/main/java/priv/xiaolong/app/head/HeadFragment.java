package priv.xiaolong.app.head;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import indi.dependency.packet.base.BaseFragment;
import indi.dependency.packet.util.ViewUtils;
import priv.xiaolong.app.MainActivity;
import priv.xiaolong.app.R;

/**
 * 侧边栏
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 9:55.
 */
public class HeadFragment extends BaseFragment implements View.OnClickListener, OnCropListener {

    private static final String KEY_DEF_INDEX = "def_index";
    private static final String KEY_MENU = "menu";
    private final String SHAREDPREFS = this.getClass().getSimpleName();
    private final String SP_KEY = "key";
    private int mDefIndex;
    private ArrayList<String> mMenu;
    private OnSelectMenuListener mListener;
    private ImageView mIvHead;

    /**
     * @param menu     菜单(点击可以切换主页)
     * @param defIndex 默认的主页
     *
     * @return
     */
    public static HeadFragment newInstance(ArrayList<String> menu, int defIndex) {
        Bundle args = new Bundle();
        args.putStringArrayList(KEY_MENU,menu);
        args.putInt(KEY_DEF_INDEX, defIndex);
        HeadFragment fragment = new HeadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        mMenu = getArguments().getStringArrayList(KEY_MENU);
        mDefIndex = getArguments().getInt(KEY_DEF_INDEX);
        return inflater.inflate(R.layout.fm_header_main, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        for (int i = 0; i < mMenu.size(); i++) {
            addRadioButton(radioGroup, mMenu.get(i));
        }

        radioGroup.getChildAt(mDefIndex).performClick();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int index = group.indexOfChild(group.findViewById(checkedId));
                if (mListener != null) mListener.onSelect(mMenu.get(index));
            }
        });

        mIvHead = (ImageView) view.findViewById(R.id.iv_head);

        //获取之前保存的图片路径, 如果有就设置
        File file = new File(mContext.getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE).getString(SP_KEY, ""));
        if (file.exists()) {
            Picasso.with(mContext).load(file).into(mIvHead);
            if (mCompatActivity instanceof MainActivity) {
                Picasso.with(mContext).load(file).into(((MainActivity) mCompatActivity).getHeadImage());
            }
        }

        mIvHead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        final @IdRes int[] viewId = {0XADC001, 0XADC002, 0XADC003};
        final String[] btns = {"拍照", "相册选择", "取消"};
        dialog.setContentView(bottomSheetDialogView(viewId, btns));
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        for (int i = 0; i < viewId.length; i++) {
            final String desc = btns[i];
            dialog.findViewById(viewId[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (desc.equals("拍照")) selectCamera();
                    if (desc.equals("相册选择")) selectAlbum();
                }
            });
        }
        dialog.show();
    }

    /** 选择头像弹窗 */
    private View bottomSheetDialogView(@IdRes int[] viewId, String[] btns) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(25, 20, 25, 20);
        layout.setBackgroundColor(Color.TRANSPARENT);
        for (int i = 0; i < btns.length; i++) {
            TextView text = new TextView(mContext);
            text.setTextSize(18);
            text.setText(btns[i]);
            text.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            text.setBackgroundResource(R.color.white);
            text.setPadding(0, 20, 0, 20);
            text.setGravity(Gravity.CENTER);
            text.setId(viewId[i]);
            LinearLayout.LayoutParams lp;
            lp = new LinearLayout.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT);
            if (i == btns.length - 1) lp.setMargins(0, 10, 0, 0);
            else lp.setMargins(0, 1, 0, 0);
            layout.addView(text, lp);
        }
        return layout;
    }

    /*
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Senior"
                android:textColor="#666666"
                android:textSize="18sp"/>
     */
    private void addRadioButton(RadioGroup radioGroup, String text) {
        RadioButton rbtn = new RadioButton(mContext);
        rbtn.setText(text);
        rbtn.setTextColor(Color.parseColor("#666666"));
        rbtn.setTextSize(18);
        rbtn.setId(new Random().nextInt(10000));
        rbtn.setBackgroundResource(R.drawable.select_def_click);
        rbtn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        rbtn.setPadding(20, 25, 20, 25);
        radioGroup.addView(rbtn, new RadioGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT));
    }

    public void setOnSelectMenuListener(OnSelectMenuListener listener) {
        mListener = listener;
    }

    @Override
    public void onCrop(File file) {
        Picasso.with(mContext).load(file).into(mIvHead);
        if (mCompatActivity instanceof MainActivity) {
            ImageView headImage = ((MainActivity) mCompatActivity).getHeadImage();
            Picasso.with(mContext).load(file).into(headImage);
        }

        SharedPreferences sp = mContext.getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        //删除旧的图片
        File oldFile = new File(sp.getString(SP_KEY, ""));
        if (oldFile.exists()) oldFile.delete();

        //保存新的图片路径
        sp.edit().putString(SP_KEY, file.getAbsolutePath()).commit();
    }

    /**相机*/
    private void selectCamera(){
        CameraDialogFm.getInstance(createNewFile(), createNewFile()).setOnCropListener(this).show(this);
    }

    /** 相册选择 */
    private void selectAlbum() {
        AlbumDialogFm.getInstance(createNewFile()).setOnCropListener(this).show(this);
    }

    /** 创建新的图片路径 */
    private File createNewFile() {
        final String letter = getResources().getString(R.string.letter);
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(letter.charAt(random.nextInt(letter.length())));
        }
        sb.append(".png");
        File file = new File(mContext.getFilesDir(), sb.toString());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 菜单选择回调
     */
    public interface OnSelectMenuListener {
        void onSelect(String key);
    }

}
