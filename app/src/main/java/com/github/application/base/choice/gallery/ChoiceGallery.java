package com.github.application.base.choice.gallery;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/12/30 17:16.
 *
 * 图库选择,使用时只要把这个模块复制过去就能用了
 * 链式结构
 * new ChoiceGallery(this)
 *         .setMaxChoice(9)//最大选择数
 *         .setChoiceList(new ArrayList<String>())//已经选择图片
 *         .setCallback(OnChoiceGalleryCallback)//回调
 *         .start()//启动
 *
 * 选则图片页面{@link ChoiceGalleryActivity}
 * 图片预览界面{@link GalleryPreviewActivity}
 */
public class ChoiceGallery {

    private Context mContext;
    private int maxChoice;
    private List<String> choiceList;
    private OnChoiceGalleryCallback onChoiceGalleryCallback;

    public ChoiceGallery(Context context) {
        mContext = context;
    }

    public ChoiceGallery(Fragment fragment) {
        mContext = fragment.requireContext();
    }

    public ChoiceGallery(View view) {
        mContext = view.getContext();
    }

    public ChoiceGallery setMaxChoice(int maxChoice) {
        this.maxChoice = maxChoice;
        return this;
    }

    public ChoiceGallery setChoiceList(List<String> choiceList) {
        this.choiceList = choiceList;
        return this;
    }

    /**
     * 为方便使用,回调方式结合广播接收者,不用在onActivityResult()中回调,广播{@link ChoiceGalleryReceiver}
     */
    public ChoiceGallery setCallback(OnChoiceGalleryCallback onChoiceGalleryCallback) {
        this.onChoiceGalleryCallback = onChoiceGalleryCallback;
        return this;
    }

    Context getContext() {
        return mContext;
    }

    int getMaxChoice() {
        return maxChoice;
    }

    List<String> getChoiceList() {
        return choiceList;
    }

    OnChoiceGalleryCallback getCallback() {
        return onChoiceGalleryCallback;
    }

    public void start() {
        if (choiceList == null) {
            choiceList = new ArrayList<>();
        }
        if (maxChoice == 0) {
            maxChoice = 9;
        }
        ChoiceGalleryActivity.start(this);
    }
}
