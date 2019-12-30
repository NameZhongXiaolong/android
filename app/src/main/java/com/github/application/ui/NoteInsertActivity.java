package com.github.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.base.choice.gallery.ChoiceGallery;
import com.github.application.data.Note;
import com.github.application.data.NoteDatabase;
import com.github.application.main.MainApplication;
import com.github.application.utils.FileUtils;
import com.github.application.utils.UnitUtils;
import com.github.application.view.ActionBarView;
import com.github.application.view.SquareItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/4/10 22:05.
 * <p>
 * 笔记
 */
public class NoteInsertActivity extends MultipleThemeActivity {

    private EditText mEditContent;
    private EditText mEditTitle;
    private NoteChoicePhotoAdapter mChoicePhotoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_note_insert);
        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        mEditTitle = findViewById(R.id.edit_title);
        mEditContent = findViewById(R.id.edit_content);
        RecyclerView recyclerView = findViewById(R.id.list);

        actionBarView.addMenuItem(1, R.drawable.ic_save, id -> save());
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(UnitUtils.px(3));
        gradientDrawable.setStroke(1, Color.parseColor("#CCCCCC"));
        mEditContent.setBackground(gradientDrawable);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        ViewGroup.MarginLayoutParams listParent = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        int parentWidth = UnitUtils.displayWidth() - listParent.leftMargin - listParent.rightMargin;
        int spacing = UnitUtils.px(3);
        listParent.leftMargin -= spacing;
        listParent.rightMargin -= spacing;
        recyclerView.addItemDecoration(new SquareItemDecoration(parentWidth, 3, spacing));

        mChoicePhotoAdapter = new NoteChoicePhotoAdapter(parentWidth / 3 - spacing);
        mChoicePhotoAdapter.setOnItemClickListener(this::onItemClick);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mChoicePhotoAdapter);
    }

    private void onItemClick(View item, int position) {
        boolean isChoiced = item.findViewById(R.id.button).getVisibility() == View.VISIBLE;
        if (isChoiced) {
            //已经选择
            File file = new File(mChoicePhotoAdapter.get(position));
            AppCompatImageView dialogImage = new AppCompatImageView(this);
            dialogImage.setAdjustViewBounds(true);
            dialogImage.setScaleType(ImageView.ScaleType.FIT_XY);
            dialogImage.setMinimumWidth(UnitUtils.px(300));
            Picasso.get().load(file).into(dialogImage);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(dialogImage);
            dialog.show();
        }else{
            //未选择
            clearFocusHideInputMethod();
            new ChoiceGallery(this).setChoiceList(mChoicePhotoAdapter.getChoiceData()).setCallback(this::onChoiceCallback).start();
        }
    }

    private void onChoiceCallback(List<String> photos) {
        mChoicePhotoAdapter.clear();
        mChoicePhotoAdapter.addAll(photos);
    }

    private void save() {
        if (mEditTitle.length() == 0) {
            MainApplication.errToast("请输入标题");
            return;
        }
        if (mEditContent.length() == 0 && mChoicePhotoAdapter.getItemCount() <= 1) {
            MainApplication.errToast("请输入内容");
            return;
        }
        if (getCurrentFocus() instanceof EditText) {
            getCurrentFocus().clearFocus();

        }
        clearFocusHideInputMethod();
        final long startTime = System.currentTimeMillis();
        ProgressDialog progressDialog = new ProgressDialog(this, Color.WHITE, 3);
        progressDialog.setTipsMsg("正在保存", true);
        progressDialog.show();
        new Thread(() -> {
            Note note = new Note();
            note.setTitle(getEditText(mEditTitle));
            note.setContent(getEditText(mEditContent));
            note.setInsertTime(System.currentTimeMillis());
            note.setUpdateTime(note.getInsertTime());
            note.setNotePhotoList(new ArrayList<>());

            for (int i = 0; i < mChoicePhotoAdapter.getItemCount(); i++) {
                String picturePath = mChoicePhotoAdapter.get(i);
                if (TextUtils.isEmpty(picturePath)){
                    continue;
                }

                StringBuilder filePath = new StringBuilder(MainApplication.getFileRoot());
                filePath.append("/res/");
                filePath.append(System.currentTimeMillis());
                filePath.append(".").append(FileUtils.getFileExtension(picturePath));

                FileUtils.copyFile(picturePath, filePath.toString());

                Note.NotePhoto notePhoto = new Note.NotePhoto();
                notePhoto.setPath(filePath.toString());
                notePhoto.setInsertTime(note.getInsertTime());
                notePhoto.setUpdateTime(note.getInsertTime());
                note.getNotePhotoList().add(notePhoto);
            }

            if (NoteDatabase.insert(note) > 0) {
                long saveDuration = System.currentTimeMillis() - startTime;
                try {
                    Thread.sleep(Math.max(2000 - saveDuration,1));
                    runOnUiThread(() -> {
                        MainApplication.outToast("保存成功");
                        progressDialog.dismiss();
                        Intent data = new Intent();
                        data.putExtra("noteId", note.getId());
                        setResult(RESULT_OK,data);
                        new Handler().postDelayed(this::finish, 800);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    protected void clearFocusHideInputMethod(){
        View currentFocus = getCurrentFocus();
        if (currentFocus instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null){
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            currentFocus.clearFocus();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
