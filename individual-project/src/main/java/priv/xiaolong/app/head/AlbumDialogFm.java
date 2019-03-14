package priv.xiaolong.app.head;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;

import priv.xiaolong.app.R;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;
import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

/**
 * 相册选择
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/23 16:43.
 */
public class AlbumDialogFm extends DialogFragment implements DialogInterface.OnKeyListener {

    private static final int KEY_SELECT_ALBUM = 1872;
    private File mFile;//保存的位置,无论是否选择成功都会生成
    private OnCropListener mOnCrop;

    /**
     * @param file 保存位置
     *
     * @return
     */
    public static AlbumDialogFm getInstance(File file) {
        Bundle args = new Bundle();
        AlbumDialogFm fragment = new AlbumDialogFm();
        fragment.setArguments(args);
        fragment.setFile(file);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(this);
        return new ProgressBar(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent();
        // 开启Pictures画面Type设定为image
        intent.setType("image/*");
        // 使用Intent.ACTION_GET_CONTENT这个Action
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // 取得相片后返回本画面
        startActivityForResult(intent, KEY_SELECT_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选取图片
        if (requestCode == KEY_SELECT_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                //图片裁剪
                UCrop.of(data.getData(), Uri.fromFile(mFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(maxWidth, maxHeight)
                        .start(getContext(), this);
            } else {
                mFile.delete();
                close();
            }
        }

        //图片裁剪
        if (requestCode == REQUEST_CROP) {
            //成功裁剪
            if (resultCode == Activity.RESULT_OK) if (mOnCrop != null) mOnCrop.onCrop(mFile);

                //没有裁剪成功就删除之前创建好的图片
            else mFile.delete();
            close();
        }
    }

    /**
     * 保存的位置
     */
    private void setFile(File file) {
        mFile = file;
    }

    /**
     * 设置裁剪成功回调
     *
     * @param onCrop
     *
     * @return
     */
    public AlbumDialogFm setOnCropListener(OnCropListener onCrop) {
        mOnCrop = onCrop;
        return this;
    }

    /**
     * 显示
     *
     * @param fm
     */
    public void show(Fragment fm) {
        super.show(fm.getChildFragmentManager(), AlbumDialogFm.class.getSimpleName());
    }

    /**
     * 关闭
     */
    private void close() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1000);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getContext(), "正在操作,请稍等..", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
