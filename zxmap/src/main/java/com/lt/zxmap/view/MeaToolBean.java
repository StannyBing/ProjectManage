package com.lt.zxmap.view;

import androidx.annotation.DrawableRes;

public class MeaToolBean {

    private String name;
    private @DrawableRes int icon;

    public MeaToolBean(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
