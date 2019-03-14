package indi.dependency.packet.image.picker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/6/6 10:15.
 */
public class ImagePicker implements IImagePicker {

    /**
     * 单选
     */
    public static final int CHOICE_MODE_SINGLE = 7928;

    /**
     * 多选
     */
    public static final int CHOICE_MODE_MULTIPLE = 0123;

    /**
     * 图片数量
     */
    int mPhotoNumber;

    /**
     * 回调
     */
    OnSelectBack mOnSelectBack;

    /**
     * 是否显示拍照
     */
    boolean mCanCamera;

    /**
     * 选择模式(单选/多选)
     */
    int mChoiceMode;

    private FragmentManager mFragmentManager;

    private ImagePicker() { }

    /**
     * 初始化
     */
    public synchronized static final ImagePicker with(FragmentActivity activity) {
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.init(imagePicker, activity.getSupportFragmentManager());
        return imagePicker;
    }

    /**
     * 初始化
     */
    public synchronized static final ImagePicker with(Fragment fm) {
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.init(imagePicker, fm.getChildFragmentManager());
        return imagePicker;
    }

    private void init(ImagePicker imagePicker, FragmentManager fm) {
        imagePicker.mFragmentManager = fm;
        imagePicker.mChoiceMode = ImagePicker.CHOICE_MODE_SINGLE;
        imagePicker.mPhotoNumber = 3;
    }

    @Override
    public ImagePicker setCanCamera() {
        mCanCamera = true;
        return this;
    }

    @Override
    public ImagePicker setChoiceMode(int mode, int... max) {
        if (mode == CHOICE_MODE_MULTIPLE) {
            //多选
            mChoiceMode = CHOICE_MODE_MULTIPLE;
            if (max.length > 0 && max[0] > 0) mPhotoNumber = max[0];
        }else{
            //单选
            mPhotoNumber = 1;
        }
        return this;
    }

    @Override
    public void start(OnSelectBack onSelectBack) {
        mOnSelectBack = onSelectBack;
        new ImagePickerDialogFm().setImagePicker(this).show(mFragmentManager, this.getClass().getSimpleName());
    }
}
