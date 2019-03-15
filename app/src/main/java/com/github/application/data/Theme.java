package com.github.application.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZhongXiaolong on 2019/3/11 17:44.
 */
public class Theme implements Parcelable{

    private String theme;
    private int themeRes;

    public Theme(String theme, int themeRes) {
        this.theme = theme;
        this.themeRes = themeRes;
    }

    protected Theme(Parcel in) {
        theme = in.readString();
        themeRes = in.readInt();
    }

    public static final Creator<Theme> CREATOR = new Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel in) {
            return new Theme(in);
        }

        @Override
        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };

    public String getTheme() {
        return theme;
    }

    public int getThemeRes() {
        return themeRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(theme);
        dest.writeInt(themeRes);
    }
}
