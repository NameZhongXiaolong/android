package com.svnchina.application.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svnchina.application.R;
import com.svnchina.application.base.BaseSuperFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2017/12/13 16:04.
 * <p>
 * 测试
 */
public class TestFm extends BaseSuperFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_text, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = view.findViewById(R.id.text);
        text.setText("分享该群");
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinQQGroup("pEl9e_h3JyX3LhxDgn9yP-LKH9_6NwhC");
            }
        });
        final TextView textView = view.findViewById(R.id.text);
        final SharedPreferences sharedPrefs = getBaseSuperActivity().getSharedPrefs();

        List<Phone> list = new ArrayList<>();
        list.add(new Phone(11, "aa"));
        list.add(new Phone(22, "bb"));
        list.add(new Phone(33, "cc"));
        list.add(new Phone(44, "dd"));
        Data data = new Data(list);

        Class<? extends Data> clazz = data.getClass();
        for (int i = 0; i < clazz.getFields().length; i++) {
            try {
                String name = clazz.getFields()[i].getName();
                Field field = clazz.getDeclaredField(name);

                if (field.getType().getSimpleName().equals("List")){
                    List l = (List) field.get(data);
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (Object o : l) {
                        jsonArray.put(o.toString());
                    }
                    jsonObject.put(name, jsonArray);
                    Log.d("TestFm", jsonObject.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /****************
     *
     * 发起添加群流程。群号：311新胖子领导(581104782) 的 key 为： pEl9e_h3JyX3LhxDgn9yP-LKH9_6NwhC
     * 调用 joinQQGroup(pEl9e_h3JyX3LhxDgn9yP-LKH9_6NwhC) 即可发起手Q客户端申请加群 311新胖子领导(581104782)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    private class Test{

        public List<String> photo;
    }
    private class Data {

        public Data(List<Phone> photo) {
            this.photo = photo;
        }

        public List<Phone> photo;
    }

    private class Phone{
        public Phone(int num, String name) {
            this.num = num;
            this.name = name;
        }

        int num;
        String name;

        @Override
        public String toString() {
            return "{" +
                    "num" +
                    ":" + num +
                    ", name:" + name + '\'' +
                    '}';
        }
    }
}
