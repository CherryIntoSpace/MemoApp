package com.memoapp.app;

public class MainData {
    private String tv_date;
    private String tv_content;

    public MainData(String tv_name, String tv_content) {
        this.tv_date = tv_name;
        this.tv_content = tv_content;
    }

    public String getTv_date() {
        return tv_date;
    }

    public void setTv_name(String tv_name) {
        this.tv_date = tv_name;
    }

    public String getTv_content() {
        return tv_content;
    }

    public void setTv_content(String tv_content) {
        this.tv_content = tv_content;
    }
}
