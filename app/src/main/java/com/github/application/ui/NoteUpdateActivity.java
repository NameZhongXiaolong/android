package com.github.application.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.base.choice.gallery.ChoiceGallery;
import com.github.application.data.Note;
import com.github.application.data.NoteDatabase;
import com.github.application.main.MainApplication;
import com.github.application.receiver.NoteDeleteReceiver;
import com.github.application.receiver.NoteUpdateReceiver;
import com.github.application.utils.FileUtils;
import com.github.application.utils.UnitUtils;
import com.github.application.view.ActionBarView;
import com.github.application.view.SquareItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/12/26 15:25.
 * <p>
 * 笔记编辑
 */
public class NoteUpdateActivity extends MultipleThemeActivity {

    private EditText mEditContent;
    private EditText mEditTitle;
    private NoteChoicePhotoAdapter mChoicePhotoAdapter;
    private Note mNote;

    public static void start(Context context, int noteId) {
        Intent starter = new Intent(context, NoteUpdateActivity.class);
        starter.putExtra("noteId", noteId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_note_update);
        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        mEditTitle = findViewById(R.id.edit_title);
        mEditContent = findViewById(R.id.edit_content);
        RecyclerView recyclerView = findViewById(R.id.list);
        Button btnDelete = findViewById(R.id.button);

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

        btnDelete.setOnClickListener(this::onDeleteViewClick);
        if (hasNavigationBar()) {
            ((LinearLayout.LayoutParams) btnDelete.getLayoutParams()).bottomMargin = UnitUtils.px(60);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        int noteId = getIntent().getIntExtra("noteId", 0);
        mNote = NoteDatabase.findNoteById(noteId);
        if (mNote == null) return;

        mEditTitle.setText(mNote.getTitle());
        mEditContent.setText(mNote.getContent());
        mEditTitle.setSelection(mEditTitle.length());
        mEditContent.setSelection(mEditContent.length());

        List<String> choicePhotoData = new ArrayList<>();
        for (Note.NotePhoto notePhoto : mNote.getNotePhotoList()) {
            choicePhotoData.add(notePhoto.getPath());
        }
        mChoicePhotoAdapter.addAll(choicePhotoData);
    }

    private void onItemClick(View item, int position) {
        boolean isChoice = item.findViewById(R.id.button).getVisibility() == View.VISIBLE;
        if (isChoice) {
            //已经选择
            List<String> data = mChoicePhotoAdapter.getChoiceData();
            PhotoPreviewActivity.start(this, data, position, newData -> mChoicePhotoAdapter.setNewData(newData));
        }else{
            //未选择
            clearFocusHideInputMethod();
            new ChoiceGallery(this).setCallback(photos -> mChoicePhotoAdapter.addAll(photos)).start();
        }
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
            mNote.setTitle(getEditText(mEditTitle));
            mNote.setContent(getEditText(mEditContent));
            mNote.setUpdateTime(mNote.getInsertTime());

            List<Note.NotePhoto> newPhotos = new ArrayList<>();
            for (String picturePath : mChoicePhotoAdapter.getChoiceData()) {
                if (TextUtils.isEmpty(picturePath)) {
                    continue;
                }

                boolean fileExists = false;
                for (Note.NotePhoto photo : mNote.getNotePhotoList()) {
                    if (photo.getPath().equals(picturePath)) {
                        mNote.getNotePhotoList().remove(photo);
                        fileExists = true;
                        break;
                    }
                }

                Note.NotePhoto notePhoto = new Note.NotePhoto();
                if (fileExists) {
                    notePhoto.setPath(picturePath);
                }else{
                    StringBuilder filePath = new StringBuilder(MainApplication.getFileRoot());
                    filePath.append("/res/");
                    filePath.append(System.currentTimeMillis());
                    filePath.append(".").append(FileUtils.getFileExtension(picturePath));

                    FileUtils.copyFile(picturePath, filePath.toString());
                    notePhoto.setPath(filePath.toString());
                }

                notePhoto.setInsertTime(mNote.getUpdateTime());
                notePhoto.setUpdateTime(mNote.getUpdateTime());
                newPhotos.add(notePhoto);

            }

            for (Note.NotePhoto notePhoto : mNote.getNotePhotoList()) {
                FileUtils.deleteFile(notePhoto.getPath());
            }

            mNote.setNotePhotoList(newPhotos);

            if (NoteDatabase.update(mNote)) {
                long saveDuration = System.currentTimeMillis() - startTime;
                try {
                    Thread.sleep(Math.max(2000 - saveDuration, 1));
                    runOnUiThread(() -> {
                        MainApplication.outToast("修改成功");
                        progressDialog.dismiss();
                        Intent data = new Intent();
                        data.putExtra("noteId", mNote.getId());
                        setResult(RESULT_OK, data);
                        NoteUpdateReceiver.post(this, mNote.getId());
                        new Handler().postDelayed(this::finish, 800);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void clearFocusHideInputMethod() {
        View currentFocus = getCurrentFocus();
        if (currentFocus instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            currentFocus.clearFocus();
        }
    }

    private void onDeleteViewClick(View view) {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("确定要删除这篇笔记?")
                .setPositiveButton("确定", (dialog, which) -> delete(view))
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();
    }

    private void delete(View view){
        ProgressDialog progressDialog = new ProgressDialog(this, Color.WHITE, 3);
        progressDialog.setTipsMsg("正在删除", true);
        progressDialog.show();
        long startTime = System.currentTimeMillis();
        if (NoteDatabase.deleteByNoteId(mNote.getId())) {
            new Thread(() -> {
                for (Note.NotePhoto notePhoto : mNote.getNotePhotoList()) {
                    FileUtils.deleteFile(notePhoto.getPath());
                }
                long endTime = System.currentTimeMillis();
                try {
                    Thread.sleep(Math.max(2000 - (endTime - startTime), 0));
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        MainApplication.outToast("已删除");
                        NoteDeleteReceiver.post(this, mNote.getId());
                        new Handler().postDelayed(this::finish, 800);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
