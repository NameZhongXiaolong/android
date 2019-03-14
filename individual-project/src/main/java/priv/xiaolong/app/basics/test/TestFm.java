package priv.xiaolong.app.basics.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import indi.dependency.packet.base.BaseFragment;
import indi.dependency.packet.util.ImageUrls;
import priv.xiaolong.app.R;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/6/1 11:29.
 */
public class TestFm extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ztest, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        String[] arr = list.toArray(new String[]{});

        view.findViewById(R.id.btn_test).setOnClickListener(v ->
                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show());

        final ImageView imageView = view.findViewById(R.id.image);
        Picasso.with(getContext()).load(ImageUrls.getOneFile().get(0)).into(imageView);

        ProgressBar dialogView = new ProgressBar(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(dialogView).create();
        dialogView.setProgress(10);
        dialog.show();
    }
}
