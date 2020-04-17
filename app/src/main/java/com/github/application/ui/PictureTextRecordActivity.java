package com.github.application.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.base.choice.gallery.ChoiceGallery;
import com.github.application.main.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by ZhongXiaolong on 2020/4/17 17:21.
 */
public class PictureTextRecordActivity extends MultipleThemeActivity {

    private LinearLayout mEditContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_text_record);

        mEditContainer = findViewById(R.id.linear_layout);
        mEditContainer.addView(getEditText());
        findViewById(R.id.button).setOnClickListener(this::onAddPhotoClick);
    }

    private void onAddPhotoClick(View view) {
        new ChoiceGallery(this).setMaxChoice(1).setCallback(photos -> {
            ImageView imageView = getImageView();
            mEditContainer.addView(imageView);
            Picasso.get().load(new File(photos.get(0))).into(imageView);
            EditText editText = getEditText();
            mEditContainer.addView(editText);
            editText.requestFocus();
        }).start();
    }

    private EditText getEditText() {
        AppCompatEditText editText = new AppCompatEditText(this);
        editText.setTextSize(16);
        editText.setTextColor(Color.parseColor("#333333"));
        editText.setLineSpacing(10, 1.2f);
//        editText.setMinHeight(UnitUtils.px(45));
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(new ViewGroup.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT));
        editText.setOnKeyListener(this::onKey);
        return editText;
    }

    private ImageView getImageView() {
        ImageView imageView = new AppCompatImageView(this);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT));
        return imageView;
    }

    private boolean onKey(View v, int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_ENTER == keyCode) {
            return false;
        } else if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_DEL == keyCode) {
            EditText editText = (EditText) v;
            return onDeleteDown(editText);
        } else {
            return false;
        }
    }

    private boolean onDeleteDown(EditText v) {
        if (v.getSelectionStart() == 0 && mEditContainer.getChildCount() > 1) {
            //当前输入的text
            Editable text = v.getText();
            //删除View的上一个View位置(避免下标越界)
            int index = Math.max(mEditContainer.indexOfChild(v), 1) - 1;


            //获取上个EditText,将删除的EditText的内容接到后面
            View previousView = mEditContainer.getChildAt(index);
            if (previousView instanceof ImageView) {
                //删除View
                mEditContainer.removeView(previousView);
                return true;
            }
            if (previousView instanceof EditText) {
                int length = ((EditText) previousView).length();
                ((EditText) previousView).append(text);
                ((EditText) previousView).setSelection(length);
                mEditContainer.removeView(v);
            }
            return false;
        }
        return false;
    }

}
