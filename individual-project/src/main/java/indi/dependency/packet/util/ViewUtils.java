package indi.dependency.packet.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * View Utils
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2016/12/28 9:34.
 */
public class ViewUtils {

    /**
     * 占满屏幕
     */
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    /**
     * 包裹
     */
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    @IdRes public static final int BASE_VIEW_ROOT_ID=0x100001;

//    public static View baseView(Context context){
//        LinearLayout root = new LinearLayout(context);
//        root.setOrientation(LinearLayout.VERTICAL);
//        root.setId(BASE_VIEW_ROOT_ID);
//        View view = LayoutInflater.from(context).inflate(R.layout.action_bar, root, false);
//        root.addView(view);
//        root.setBackgroundColor(Color.rgb(225, 238, 210));
//        return root;
//    }

    /**
     * 对TextView/Button设置不同状态时其文字颜色。
     */
    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 添加新的TextView
     *
     * @param context
     * @param viewParent
     * @return
     */
    public static TextView createTextView(Context context, LinearLayout viewParent) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lp;
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 18, 0, 10);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(lp);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setBackgroundColor(Color.parseColor("#00666666"));
        textView.setTextSize(18);
        if (viewParent != null) viewParent.addView(textView);
        return textView;
    }

    /**
     * 初始化RecyclerView
     *
     * @param context
     * @param viewGroup 父容器
     * @return
     */
    public static RecyclerView createRecyclerView(Context context, ViewGroup viewGroup) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setBackgroundColor(Color.parseColor("#EDEDED"));
        recyclerView.setPadding(0, 3, 0, 0);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.MATCH_PARENT, ViewUtils.MATCH_PARENT));
        if (viewGroup != null) viewGroup.addView(recyclerView);
        return recyclerView;
    }

}



















