package com.example.android.booklisting;

import android.content.Context;

import java.util.List;

/**
 * Created by Paul on 10/2/2017.
 */

public class BookLoader extends android.content.AsyncTaskLoader<List<Book>> {

    private String mURL;

    public BookLoader(Context context, String url) {

        super((context));

        mURL = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<Book> result = QueryUtilis.fetchBookData(mURL);

        return result;
    }


}
