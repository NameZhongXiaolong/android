package priv.xiaolong.app.basics.material.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import indi.dependency.packet.base.BaseFragment;
import priv.xiaolong.app.R;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/2 11:00.
 */
public class MaterialDesignFm extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_material_design, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.text);
        try {
            InputStream is = mContext.getAssets().open("MaterialDesign");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            textView.setText(sb.toString().trim());
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
