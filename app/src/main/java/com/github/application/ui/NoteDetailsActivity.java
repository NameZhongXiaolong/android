package com.github.application.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.data.Note;
import com.github.application.data.NoteDatabase;
import com.github.application.receiver.NoteDeleteReceiver;
import com.github.application.receiver.NoteUpdateReceiver;
import com.github.application.utils.UnitUtils;
import com.github.application.view.ActionBarView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ZhongXiaolong on 2019/12/26 14:48.
 */
public class NoteDetailsActivity extends MultipleThemeActivity {

    private NoteUpdateReceiver mNoteUpdateReceiver;
    private ActionBarView mActionBarView;
    private LinearLayout mContainer;
    private TextView mTextContent;
    private int mNoteId;
    private NoteDeleteReceiver mNoteDeleteReceiver;

    public static void start(Context context, int noteId) {
        Intent starter = new Intent(context, NoteDetailsActivity.class);
        starter.putExtra("noteId",noteId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mNoteUpdateReceiver = new NoteUpdateReceiver(this, this::onUpdateReceive);
        mNoteDeleteReceiver = new NoteDeleteReceiver(this, this::onDeleteReceive);
        mNoteUpdateReceiver.register();
        mNoteDeleteReceiver.register();

        mContainer = findViewById(R.id.linear_layout);
        mActionBarView = findViewById(R.id.action_bar_view);
        mTextContent = findViewById(R.id.text1);
        mActionBarView.addMenuItem(1, R.drawable.ic_edit);

        mNoteId = getIntent().getIntExtra("noteId", 0);
        Note note = NoteDatabase.findNoteById(mNoteId);

        if (note != null) create(note);
    }

    private void create(Note note) {
        int noteId = note.getId();

        mActionBarView.setTitle(note.getTitle());
        mTextContent.setText(note.getContent());
        mActionBarView.setMenuItemClickListener(id -> NoteUpdateActivity.start(this, noteId));

        mContainer.removeAllViews();

        List<String> photoList = new ArrayList<>();
        for (Note.NotePhoto notePhoto : note.getNotePhotoList()) {
            photoList.add(notePhoto.getPath());
        }

        ColorDrawable placeholderDrawable = new ColorDrawable(Color.parseColor("#30333333"));
        int margin = UnitUtils.px(14);
        for (int i = 0; i < photoList.size(); i++) {
            ImageView image = new AppCompatImageView(this);
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.setMargins(margin, margin, margin,0);
            mContainer.addView(image, params);

            File file = new File(photoList.get(i));
            RequestCreator load = file.exists() ? Picasso.get().load(file) : Picasso.get().load(photoList.get(i));

            load.placeholder(placeholderDrawable)
                    .error(placeholderDrawable)
                    .into(image);

            final int index = i;
            image.setOnClickListener(v -> PhotoPreviewActivity.start(this,photoList,index));
        }
        mContainer.addView(new View(this), new LinearLayout.LayoutParams(1, UnitUtils.px(60)));
    }

    private void onUpdateReceive(int noteId) {
        Note note = NoteDatabase.findNoteById(noteId);

        if (note != null) create(note);
    }

    private void onDeleteReceive(int noteId) {
        if (noteId == mNoteId) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNoteUpdateReceiver.unregister();
        mNoteDeleteReceiver.unregister();
    }
}
