package com.github.application.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ZhongXiaolong on 2019/12/26 17:07.
 */
public class NoteUpdateReceiver extends BroadcastReceiver {

    private static final String ACTION = "com.github.application.note.update";
    private Context mContext;
    private OnReceiveCall mOnReceiveCall;

    public NoteUpdateReceiver(Context context, OnReceiveCall onReceiveCall) {
        mContext = context;
        mOnReceiveCall = onReceiveCall;
    }

    public void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        mContext.registerReceiver(this, filter);
    }

    public static void post(Context context,int noteId){
        //设置Action
        Intent intent = new Intent(ACTION);

        //传递数据
        intent.putExtra("noteId", noteId);

        //发送广播
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent data) {
        int noteId = data.getIntExtra("noteId", 0);
        mOnReceiveCall.onReceive(noteId);
    }

    public void unregister(){
        mContext.unregisterReceiver(this);
    }

    public interface OnReceiveCall{
        void onReceive(int noteId);
    }
}
