package indi.dependency.packet.image.picker;

import android.view.View;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/6/6 10:15.
 */
interface IImagePicker {

    /**
     * 设置可以拍照
     * @return
     */
    ImagePicker setCanCamera();

    /**
     * 设置选择模式(默认单选模式)
     * @param mode 单选ImagePicker.CHOICE_MODE_SINGLE; 多选ImagePicker.CHOICE_MODE_MULTIPLE
     * @param max 设置多选, 设置最大图片数量(默认3张)
     * @return
     */
    ImagePicker setChoiceMode(int mode,int... max);

    /**
     * 开始选择
     * @param onSelectBack
     */
    void start(OnSelectBack onSelectBack);

}
