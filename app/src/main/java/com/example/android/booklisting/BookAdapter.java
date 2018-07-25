package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Paul on 10/2/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public static Context mContext;

    public BookAdapter(Context context, List<Book> books) {

        super(context, 0, books);
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_liist_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.Cover);


        Picasso.with(getContext()).load(currentBook.getCover()).into(thumbnail);

        //Text for title
        TextView titleBook = (TextView) listItemView.findViewById(R.id.titleInfo);

        titleBook.setText(currentBook.getTitle());

        //author

        TextView authorBook = (TextView) listItemView.findViewById(R.id.authorInfo);

        authorBook.setText(currentBook.getmAuthor());

        return listItemView;
    }


}
