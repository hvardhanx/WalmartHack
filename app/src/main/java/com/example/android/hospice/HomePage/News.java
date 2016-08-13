package com.example.android.hospice.HomePage;

import io.realm.RealmObject;

/**
 * Created by me on 7/8/16.
 */
public class News extends RealmObject {
    private String mTitle;
    private String mDescription;
    private String mDate;
    private String mUrl;
    public String getNewsTitle() {
        return mTitle;
    }

    public String getNewsDescription() {
        return mDescription;
    }

    public String getNewsDate() {
        return mDate;
    }

    public String getNewsUrl() {
        return mUrl;
    }

    public void setNewsTitle(String title) {
        mTitle = title;
    }

    public void setNewsDescription(String description) {
        mDescription = description;
    }

    public void setNewsDate(String date) {
        mDate = date;
    }

    public void setNewsUrl(String url) {
        mUrl = url;
    }
}
