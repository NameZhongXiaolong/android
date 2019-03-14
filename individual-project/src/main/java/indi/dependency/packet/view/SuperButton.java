package indi.dependency.packet.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import priv.xiaolong.app.R;

/**
 * 可以设置圆角/点击背景&文字颜色/选中背景&文字颜色的Button
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/7 23:00.
 */
@SuppressLint("AppCompatCustomView")
public class SuperButton extends TextView {

    private int mLeftTopRadius;
    private int mRightTopRadius;
    private int mLeftBottomRadius;
    private int mRightBottomRadius;
    @ColorInt private int mClickColorBg = Color.parseColor("#00123456");
    @ColorInt private int mTextClickColor = Color.parseColor("#00123456");
    @ColorInt private int mUnenabledColorBg = Color.parseColor("#00123456");
    @ColorInt private int mTextUnenabledColor = Color.parseColor("#00123456");
    @ColorInt private int mBackground = Color.parseColor("#00123456");

    public SuperButton(Context context) {
        super(context);
    }

    public SuperButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SuperButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Log.d("SuperButton", "superButton");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadiusCoverView);

        mLeftTopRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_buttonRadius, mLeftTopRadius);
        mRightTopRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_buttonRadius, mRightTopRadius);
        mLeftBottomRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_buttonRadius, mLeftBottomRadius);
        mRightBottomRadius = typedArray.getDimensionPixelSize(R.styleable.SuperButton_buttonRadius, mRightBottomRadius);

        mLeftTopRadius = typedArray.getDimensionPixelSize(
                R.styleable.SuperButton_buttonLeftTopRadius, mLeftTopRadius);
        mRightTopRadius = typedArray.getDimensionPixelSize(
                R.styleable.SuperButton_buttonRightTopRadius, mRightTopRadius);
        mLeftBottomRadius = typedArray.getDimensionPixelSize(
                R.styleable.SuperButton_buttonLeftBottomRadius, mLeftBottomRadius);
        mRightBottomRadius = typedArray.getDimensionPixelSize(
                R.styleable.SuperButton_buttonRightBottomRadius, mRightBottomRadius);


        Drawable background = getBackground();


        mClickColorBg = typedArray.getColor(R.styleable.SuperButton_buttonClickBackgroundColor, mClickColorBg);
        mUnenabledColorBg = typedArray.getColor(R.styleable.SuperButton_buttonUnenabledBackgroundColor,
                mUnenabledColorBg);
        mBackground = typedArray.getColor(R.styleable.SuperButton_buttonBackgroundColor, mBackground);
//        setSelectorBackground(mBackground, mClickColorBg, mUnenabledColorBg);
setBackgroundDrawable(newSelector(context,mBackground,mClickColorBg,mUnenabledColorBg,mUnenabledColorBg));
        ColorStateList textColors = getTextColors();


        mTextClickColor = typedArray.getColor(R.styleable.SuperButton_buttonTextClickColor, mTextClickColor);
        mTextUnenabledColor = typedArray.getColor(
                R.styleable.SuperButton_buttonTextUnenabledColor, mTextUnenabledColor);
        setBackgroundDrawable(getSelector(new ColorDrawable(Color.parseColor("#00FF00")),new ColorDrawable(Color.parseColor("#FFFFFF"))));
    }

    private StateListDrawable getSelector(Drawable normalDraw, Drawable pressedDraw) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, pressedDraw);
        stateListDrawable.addState(new int[]{}, normalDraw);
        return stateListDrawable;
    }

    public void setSelectorBackground(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unenabledBg) {

        StateListDrawable bg = new StateListDrawable();
//        Drawable normal = this.getResources().getDrawable(mImageIds[0]);
//        Drawable selected = this.getResources().getDrawable(mImageIds[1]);
//        Drawable pressed = this.getResources().getDrawable(mImageIds[2]);
        bg.addState(View.PRESSED_ENABLED_STATE_SET, new ColorDrawable(pressed));
        bg.addState(View.ENABLED_FOCUSED_STATE_SET, new ColorDrawable(unenabledBg));
        bg.addState(View.ENABLED_STATE_SET, new ColorDrawable(normal));
        bg.addState(View.FOCUSED_STATE_SET, new ColorDrawable(unenabledBg));
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF00")));

    }

        /**
         * 代码生成选择器
         * @param context 当前上下文
         * @param idNormal 默认图片id
         * @param idPressed 触摸时图片id
         * @param idFocused 获得焦点时图片id
         * @param idUnable 没有选中时图片id
         * @return
         */
        public  StateListDrawable newSelector(Context context, @ColorInt int idNormal, @ColorInt int idPressed, @ColorInt int idFocused,@ColorInt int idUnable) {
            StateListDrawable bg = new StateListDrawable();
            int def = Color.parseColor("#00123456");
            @ColorInt int normal = idNormal == -1 ? def : idNormal;
            @ColorInt int pressed = idPressed == -1 ? def : idPressed;
            @ColorInt int focused = idFocused == -1 ? def : idFocused;
            @ColorInt int unable = idUnable == -1 ? def : idUnable;
            // View.PRESSED_ENABLED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, new ColorDrawable(pressed));
            // View.ENABLED_FOCUSED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, new ColorDrawable(focused));
            // View.ENABLED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_enabled }, new ColorDrawable(normal));
            // View.FOCUSED_STATE_SET
            bg.addState(new int[] { android.R.attr.state_focused }, new ColorDrawable(focused));
            // View.WINDOW_FOCUSED_STATE_SET
            bg.addState(new int[]{android.R.attr.state_window_focused}, new ColorDrawable(unable));
            // View.EMPTY_STATE_SET
            bg.addState(new int[] {}, new ColorDrawable(normal));
            return bg;
        }


        /**
         * 控件选择器
         *
         * @param context 当前上下文
         * @param idNormal 默认图片id
         * @param idPressed 按压时图片id
         * @return
         */
        public static StateListDrawable setSelector(Context context, @DrawableRes int idNormal, @DrawableRes int idPressed){
            StateListDrawable bg=new StateListDrawable();
            Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
            Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
            bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
            bg.addState(new int[] { android.R.attr.state_enabled }, normal);
            bg.addState(new int[] {}, normal);
            return bg;
        }
}























