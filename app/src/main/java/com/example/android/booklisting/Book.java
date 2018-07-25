package com.example.android.booklisting;

/**
 * Created by Paul on 10/2/2017.
 */
//constructor class

public class Book {


    private String mTitle;

    private String mAuthor;

    private String mURL;

    private String mCover;


    public Book(String title, String authorName, String url, String cover) {

        mTitle = title;
        mAuthor = authorName;
        mURL = url;
        mCover = cover;

    }

    public String getTitle() {

        return mTitle;
    }


    public String getmAuthor() {
        return mAuthor;
    }


    public String getUrl() {
        return mURL;
    }



    public String getCover() {

        return mCover;
    }
}
