package com.github.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.MultipleThemeActivity;
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

/**
 * Created by ZhongXiaolong on 2019/4/10 22:05.
 * <p>
 * 笔记
 */
public class NoteInsertActivity extends MultipleThemeActivity {

    private EditText mEditContent;
    private EditText mEditTitle;
    private ChoicePhotoAdapter mChoicePhotoAdapter;

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

        mChoicePhotoAdapter = new ChoicePhotoAdapter(parentWidth / 3 - spacing);
        mChoicePhotoAdapter.add(null);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mChoicePhotoAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //查询我们需要的数据
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    mChoicePhotoAdapter.add(0, picturePath);
                }
                cursor.close();
            }
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
                String filePath = MainApplication.getFileRoot() + "/res/" + System.currentTimeMillis() + ".jpg";
                FileUtils.copyFile(picturePath, filePath);

                Note.NotePhoto notePhoto = new Note.NotePhoto();
                notePhoto.setPath(filePath);
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

    private class ChoicePhotoAdapter extends BaseAdapter<String> {

        private int mItemSize;

        private ChoicePhotoAdapter(int itemSize) {
            mItemSize = itemSize;
        }

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return BaseHolder.instance(viewGroup, R.layout.item_choice_image);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
            Context context = baseHolder.getItemView().getContext();
            ImageView imageView = baseHolder.findViewById(R.id.image);
            ImageButton button = baseHolder.findViewById(R.id.button);
            button.setOnClickListener(v -> remove(i));
            String url = get(i);
            if (TextUtils.isEmpty(url)) {
                baseHolder.setOnClickListener((item, position) -> {
                    clearFocusHideInputMethod();
                    Intent intent = new Intent();
                    // 开启Pictures画面Type设定为image
                    intent.setType("image/*");
                    // 使用Intent.ACTION_GET_CONTENT这个Action
                    intent.setAction(Intent.ACTION_PICK);
                    // 取得相片后返回本画面
                    startActivityForResult(intent, 100);
                });
                imageView.setImageResource(R.mipmap.ic_add_to);
                button.setVisibility(View.INVISIBLE);
            }else{
                button.setVisibility(View.VISIBLE);
                File file = new File(url);
                Picasso.get().load(file).resize(mItemSize,mItemSize).centerCrop().into(imageView);
                baseHolder.setOnClickListener((item, position) -> {
                    AppCompatImageView dialogImage = new AppCompatImageView(context);
                    dialogImage.setAdjustViewBounds(true);
                    dialogImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    dialogImage.setMinimumWidth(UnitUtils.px(300));
                    dialogImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(dialogImage);
                    dialog.show();
                });
            }
        }
    }

}
