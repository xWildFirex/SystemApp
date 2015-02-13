package com.ikrok.systemapp;


import android.graphics.drawable.Drawable;

public class AppInformation {
    private Drawable icon;
    private String appName;
    private String packageName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return appName +
                " (" +
                packageName +
                ") " +
                "\n";
    }
}
