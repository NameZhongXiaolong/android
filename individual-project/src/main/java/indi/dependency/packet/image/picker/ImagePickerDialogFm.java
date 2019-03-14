package indi.dependency.packet.image.picker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import priv.xiaolong.app.R;

/**
 * 图片选择器
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/24 14:53.
 */
public class ImagePickerDialogFm extends DialogFragment {

    @IdRes private final int GRIDVIEW_ID = 0XADF001;


    private ImagePicker mImagePicker;

    private ArrayList<File> mData;
    private ImagePickerAdp mAdapter;

    public synchronized ImagePickerDialogFm setImagePicker(ImagePicker imagePicker) {
        mImagePicker = imagePicker;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#222222")));
        WindowManager.LayoutParams attr = dialog.getWindow().getAttributes();
        attr.windowAnimations = R.style.PopAnimation;
        dialog.getWindow().setAttributes(attr);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        LinearLayout playout = new LinearLayout(getContext());
        playout.setOrientation(LinearLayout.VERTICAL);
        playout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GridView gridView = new GridView(getContext());
        gridView.setId(GRIDVIEW_ID);
        gridView.setNumColumns(3);
        gridView.setVerticalSpacing(3);
        gridView.setHorizontalSpacing(3);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        playout.addView(gridView, lp);
        return playout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView) view.findViewById(GRIDVIEW_ID);
        mData = new ArrayList<>();
        mAdapter = new ImagePickerAdp(mData, getContext());
        gridView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("ImagePickerDialogFm", "图片");

        Log.d("ImagePickerDialogFm", mImagePicker.mChoiceMode==mImagePicker.CHOICE_MODE_SINGLE?"单选":"多选");
        Log.d("ImagePickerDialogFm", "是否拍照" + mImagePicker.mCanCamera);
        Log.d("ImagePickerDialogFm", "张数" + mImagePicker.mPhotoNumber);
        //检测SD卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            getAllFiles(Environment.getExternalStorageDirectory());
//                Environment.getExternalStorageDirectory();
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "没有SD卡", Toast.LENGTH_LONG).show();
        }


//        mData.addAll(ImageUrls.getOneFile());
        mAdapter.notifyDataSetChanged();

    }

    private void getAllFiles(File root) {
        File files[] = root.listFiles();
        boolean tag = true;
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFiles(f);
                } else {
                    Log.d("ImagePickerDialogFm", "f.length():" + f.length());
                    String path = f.getAbsolutePath();
                    if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
                        if (f.length() > 516800) mData.add(f);
                        if (mData.size()==20) break;
                    }
                }
            }
        }
    }

}
