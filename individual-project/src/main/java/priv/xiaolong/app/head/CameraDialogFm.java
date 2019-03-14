package priv.xiaolong.app.head;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import priv.xiaolong.app.R;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;
import static android.content.ContentValues.TAG;
import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

/**
 * 拍照选择
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/23 17:23.
 */
public class CameraDialogFm extends DialogFragment implements DialogInterface.OnKeyListener {

    private static final int KEY_SELECT_CAMERA = 8120;
    private File mFile;//保存的位置
    private File mTmpFile;//缓存的位置
    private OnCropListener mOnCrop;

    /**
     * @param file 保存位置
     *
     * @return
     */
    public static CameraDialogFm getInstance(File file, File tmpFile) {
        Bundle args = new Bundle();
        CameraDialogFm fragment = new CameraDialogFm();
        fragment.setArguments(args);
        fragment.setFile(file);
        fragment.setTmpFile(tmpFile);
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
        //判断摄像头权限
        if (cameraIsCanUse()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, KEY_SELECT_CAMERA);
        } else {
            checkCameraPermission();
            mFile.delete();
            close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_SELECT_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                //获取返回的图片, 并且保存到临时目录
                Bitmap bitmap = data.getParcelableExtra("data");
                saveBitmap(bitmap, mTmpFile);

                //图片裁剪
                UCrop.of(Uri.fromFile(mTmpFile), Uri.fromFile(mFile))
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
            if (resultCode == Activity.RESULT_OK) {
                mOnCrop.onCrop(mFile);
            }

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

    private void setTmpFile(File tmpFile) {
        mTmpFile = tmpFile;
        mTmpFile = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
        if (!mTmpFile.exists()) {
            mTmpFile.mkdirs();
        }
    }

    /**
     * 设置裁剪成功回调
     *
     * @return
     */
    public CameraDialogFm setOnCropListener(OnCropListener onCrop) {
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
        mTmpFile.delete();
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

    /**
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    public boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    //检查权限
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), android
                .Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1000);
        }
    }

    //保存Bitmap
    public void saveBitmap(Bitmap bm, File file) {
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

















