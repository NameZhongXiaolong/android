package com.github.application.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.interfaces.SimpleTextWatcher;
import com.github.application.main.Constants;
import com.github.application.view.ActionBarView;

/**
 * Created by ZhongXiaolong on 2019/4/10 22:05.
 * <p>
 * 笔记
 */
public class NoteActivity extends MultipleThemeActivity implements View.OnClickListener, BaseHolder.OnClickListener {

    private BaseAdapter<String> mAdapter;
    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_note);
//        RecyclerView recyclerView = findViewById(R.id.list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new BaseAdapter<String>() {
//            @NonNull
//            @Override
//            public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return BaseHolder.instance(parent, R.layout.item_note_text);
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
//                holder.text(R.id.text, get(position));
//                holder.setOnClickListener(NoteActivity.this);
//            }
//        };
//        recyclerView.setAdapter(mAdapter);
//
//        mAdapter.add("a");
//
//        TextView button = findViewById(R.id.button);
//        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final TextView textView = (TextView) v;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
        final PopupWindow pop = new PopupWindow(this);

        pop.setWidth(Constants.MATCH_PARENT);
        pop.setHeight(Constants.WRAP_CONTENT);
        FrameLayout frameLayout = new FrameLayout(this);
        final EditText editText = (EditText) LayoutInflater.from(this).inflate(R.layout.edit_text, frameLayout, false);
        editText.setMinimumWidth(v.getWidth());
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT);
        frameLayout.addView(editText, params);
        //设置返回键可以点击
        pop.setFocusable(true);
        //设置外部可以点击
        pop.setOutsideTouchable(true);

        pop.setContentView(frameLayout);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        pop.showAsDropDown(v);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.d("NoteActivity", "KeyEvent.KEYCODE_BACK");
                    pop.dismiss();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s);
            }
        });

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                String data = editText.getText().toString();
                textView.setText("");
                if (!TextUtils.isEmpty(data)) mAdapter.add(data);
            }
        });
    }

    @Override
    public void onClick(View item,final int position) {
        final TextView textView = (TextView) item;
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) textView.getLayoutParams();
        PopupWindow pop = new PopupWindow(this);

        pop.setWidth(Constants.MATCH_PARENT);
        pop.setHeight(Constants.WRAP_CONTENT);
        FrameLayout frameLayout = new FrameLayout(this);
        final EditText editText = (EditText) LayoutInflater.from(this).inflate(R.layout.edit_text, frameLayout, false);
        editText.setMinimumWidth(item.getWidth());
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editText.setText(textView.getText().toString());
        editText.setSelection(editText.length());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Constants.MATCH_PARENT, Constants.WRAP_CONTENT);
        frameLayout.addView(editText, params);
        //设置返回键可以点击
        pop.setFocusable(true);
        //设置外部可以点击
        pop.setOutsideTouchable(true);

        pop.setContentView(frameLayout);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        int[] ints = new int[2];
        item.getLocationOnScreen(ints);
//        pop.showAsDropDown(item,ints[0], ints[1]);
        pop.showAtLocation(item,Gravity.TOP,ints[0]+dp2px(16), ints[1]);
//        pop.showAsDropDown(item);

        editText.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s);
            }
        });
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                String data = editText.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    mAdapter.remove(position);
                }else{
                    mAdapter.set(position, data);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEditText = findViewById(R.id.edit);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("NoteActivity", "hasFocus:" + hasFocus);
                mEditText.setCursorVisible(hasFocus);
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NoteActivity", "click");
                if (mEditText.isFocusable()) {
                    return;
                }
                mEditText.setFocusable(true);
                mEditText.setFocusableInTouchMode(true);
                mEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });
        ((ActionBarView) findViewById(R.id.action_bar_view)).setNavigationClickListener(new ActionBarView
                .NavigationClickListener() {
            @Override
            public void onNavigationClick(ImageButton button) {
                Log.d("NoteActivity", "清空焦点");
                mEditText.setFocusable(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("NoteActivity", "onBackPressed");
    }
}
