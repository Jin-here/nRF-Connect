package com.vgaw.nrfconnect.page;

/**
 * Created by dell on 2018/3/4.
 */

public class MainTabBean {
    public String title;
    public String subTitle;

    public MainTabBean() {}

    public MainTabBean(String title) {
        this.title = title;
    }

    public MainTabBean(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }
}
