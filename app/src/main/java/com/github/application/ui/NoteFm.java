package com.github.application.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.BaseSuperFragment;
import com.github.application.data.Note;
import com.github.application.data.NoteDatabase;
import com.github.application.receiver.NoteDeleteReceiver;
import com.github.application.receiver.NoteUpdateReceiver;
import com.github.application.utils.UnitUtils;
import com.github.application.view.NineGridlayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ZhongXiaolong on 2019/4/11 18:13.
 */
public class NoteFm extends BaseSuperFragment {

    private NoteAdapter mAdapter;
    private NoteUpdateReceiver mNoteUpdateReceiver;
    private NoteDeleteReceiver mNoteDeleteReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setId(R.id.list);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteUpdateReceiver = new NoteUpdateReceiver(requireContext(), this::onUpdateReceive);
        mNoteDeleteReceiver = new NoteDeleteReceiver(requireContext(), this::onDeleteReceive);
        mNoteUpdateReceiver.register();
        mNoteDeleteReceiver.register();

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NoteAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.addAll(NoteDatabase.findNoteList());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (requireActivity() instanceof ContainerActivity) {
            ((ContainerActivity) requireActivity())
                    .getActionBarView()
                    .addMenuItem(0, R.drawable.ic_insert,
                            id -> startActivityForResult(
                                    new Intent(requireContext(), NoteInsertActivity.class), START_REQUEST_CODE));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int noteId = data.getIntExtra("noteId", 0);
            Note note = NoteDatabase.findNoteById(noteId);
            if (note != null) {
                mAdapter.add(0, note);
            }
        }
    }

    private void onUpdateReceive(int noteId) {
        for (int i = 0; i < mAdapter.getItemCount() - 1; i++) {
            if (mAdapter.get(i).getId() == noteId) {
                mAdapter.set(i, NoteDatabase.findNoteById(noteId));
            }
        }
    }

    private void onDeleteReceive(int noteId) {
        for (int i = 0; i < mAdapter.getItemCount() - 1; i++) {
            if (mAdapter.get(i).getId() == noteId) {
                mAdapter.remove(i);
            }
        }
    }

    /**
     * 显示底部弹出按钮弹窗
     */
    private void showButtonDialog(View view){
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(R.layout.dialog_bottom_note);
        //设置大背景颜色
        View container = dialog.findViewById(R.id.design_bottom_sheet);
        if (container != null) container.setBackgroundColor(Color.TRANSPARENT);
        dialog.show();

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button button_1 = dialog.findViewById(R.id.button_1);
        if (btnCancel != null && button_1 != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            button_1.setOnClickListener(v -> {
                dialog.dismiss();
                screenshotLoad(view);
                Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * view截图保存本地
     * @param view 要保存的view
     */
    private void screenshotLoad(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.TRANSPARENT);
        view.draw(c);

        String child = "Screenshots/" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), child);

        try {
            boolean mkdirs = true;
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                mkdirs = file.getParentFile().mkdirs();
            }

            if (mkdirs && file.createNewFile()) {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                //通知系统刷新
                requireActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse("file://" + file.getAbsolutePath())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNoteUpdateReceiver.unregister();
        mNoteDeleteReceiver.unregister();
    }

    private class NoteAdapter extends BaseAdapter<Note> {

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 10) {
                View itemView = new View(parent.getContext());
                itemView.setMinimumHeight(UnitUtils.px(60));
                return BaseHolder.instance(itemView);
            }
            return BaseHolder.instance(parent, R.layout.item_test);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            if (getItemViewType(position) == 10) {
                holder.setOnClickListener((item, i) -> Log.d("NoteAdapter", "click"));
                return;
            }
            NineGridlayout nineGridlayout = holder.findViewById(R.id.nine_gridlayout);
            Note note = get(position);
            List<String> urls = new ArrayList<>();
            for (Note.NotePhoto notePhoto : note.getNotePhotoList()) {
                urls.add(notePhoto.getPath());
            }
            nineGridlayout.setAdapter(new NineGridlayout.SimpleImageAdapter(urls));
            holder.text(R.id.text1, note.getTitle());
            holder.text(R.id.text2, note.getContent());
            holder.setOnClickListener((item, i) -> NoteDetailsActivity.start(requireContext(), note.getId()));
            nineGridlayout.setOnItemClickListener((i, v) -> NoteDetailsActivity.start(requireContext(), note.getId()));

            holder.setOnLongClickListener((item, i) -> {
                showButtonDialog(item);
                return true;
            });

            nineGridlayout.setOnItemLongClickListener((i, view) -> holder.itemView.performLongClick());
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() > 0 ? super.getItemCount() + 1 : 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return 10;
            }
            return super.getItemViewType(position);
        }
    }
}
