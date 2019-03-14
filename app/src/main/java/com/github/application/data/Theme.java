package com.github.application.data;

public class Theme {

    public Theme(String theme, int themeRes) {
        this.theme = theme;
        this.themeRes = themeRes;
    }

    private String theme;
    private int themeRes;

    public String getTheme() {
        return theme;
    }

    public int getThemeRes() {
        return themeRes;
    }
}
