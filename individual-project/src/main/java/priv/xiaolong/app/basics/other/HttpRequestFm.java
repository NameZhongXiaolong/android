package priv.xiaolong.app.basics.other;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import priv.xiaolong.app.R;

/**
 * 网络请求测试(okhttp)
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/2/27 16:57.
 */
public class HttpRequestFm extends Fragment
        implements View.OnClickListener, TextWatcher, View.OnLongClickListener, okhttp3.Callback {

    private EditText mEtUrl;
    private Button mBtnRequest;
    private TextView mTvReturnRes;
    private TextView mTvRequestRes;
    private LinearLayout mLlParams;
    private EditText mEtMainUrl;
    private ScrollView mSvParent;
    private String mUrl = "null";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fm_http_request, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtMainUrl = (EditText) view.findViewById(R.id.et_main_url);
        mEtUrl = (EditText) view.findViewById(R.id.et_url);
        mBtnRequest = (Button) view.findViewById(R.id.btn_request);
        mLlParams = (LinearLayout) view.findViewById(R.id.ll_params);
        mTvReturnRes = (TextView) view.findViewById(R.id.tv_out_return_res);
        mTvRequestRes = (TextView) view.findViewById(R.id.tv_request_res);
        mSvParent = (ScrollView) view.findViewById(R.id.sv_http_parent);
        view.findViewById(R.id.btn_request_clean).setOnClickListener(this);
        view.findViewById(R.id.btn_add_params).setOnClickListener(this);
        mTvReturnRes.setOnLongClickListener(this);
        mBtnRequest.setOnClickListener(this);
        mEtMainUrl.addTextChangedListener(this);
        mEtUrl.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                //请求
                httpRequest();
                break;
            case R.id.btn_request_clean:
                //清空
                if (mLlParams.getChildCount() > 0) mLlParams.removeAllViews();
                else mEtUrl.setText("");
                break;
            case R.id.btn_add_params:
                //添加参数
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_http_param, mLlParams, false);
                mLlParams.addView(view);
                break;
        }
    }

    /**
     * 请求
     */
    private void httpRequest() {
        mBtnRequest.setEnabled(false);
        mTvRequestRes.setVisibility(View.GONE);
        mTvReturnRes.setVisibility(View.GONE);

        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();

        String url = mEtMainUrl.getText().toString().trim() + mEtUrl.getText().toString().trim();
        for (int i = 0; i < mLlParams.getChildCount(); i++) {
            View childView = mLlParams.getChildAt(i);
            String key = ((EditText) childView.findViewById(R.id.et_param_key)).getText().toString().trim();
            String value = ((EditText) childView.findViewById(R.id.et_param_value)).getText().toString().trim();
            if (!TextUtils.isEmpty(key)) builder.add(key, value);
        }

        try {
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            okhttp3.Call call = client.newCall(request);
            call.enqueue(this);

        } catch (IllegalArgumentException e) {
            Snackbar.make(mSvParent, "请输入正确网址!", Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * 请求失败
     *
     * @param call
     * @param e
     */
    @Override
    public void onFailure(okhttp3.Call call, IOException e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvRequestRes.setVisibility(View.VISIBLE);
                mTvRequestRes.setText("请求超时(没网络或者地址错误)");
                mBtnRequest.setEnabled(true);
            }
        });
    }

    /**
     * 请求成功
     *
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(okhttp3.Call call, Response response) throws IOException {
        mUrl = call.request().body().toString();
        if (response.isSuccessful()) {
            final String res = response.body().string();
            getActivity().runOnUiThread(() -> {
                mTvReturnRes.setVisibility(View.VISIBLE);
                mTvReturnRes.setText(res.toString());
                mBtnRequest.setEnabled(true);
            });
        } else {
            getActivity().runOnUiThread(() -> {
                mTvRequestRes.setVisibility(View.VISIBLE);
                mTvRequestRes.setText("请求失败");
                mBtnRequest.setEnabled(true);
            });
        }
    }

    @Override
    public boolean onLongClick(View v) {
        String res = mTvReturnRes.getText().toString().trim();
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText(null,res));
        Snackbar.make(mSvParent, "已复制", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, 0, 0, "复制");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setPrimaryClip(ClipData.newPlainText(null,mUrl));
            Snackbar.make(mSvParent, "请求已复制", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void afterTextChanged(Editable s) {
        mBtnRequest.setEnabled(TextUtils.isEmpty(s) ? false : true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

}
