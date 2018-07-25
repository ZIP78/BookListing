package com.example.android.booklisting;

        import android.app.LoaderManager;
        import android.content.Context;
        import android.content.Intent;
        import android.content.Loader;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

//main Activity

public class BookMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    static final String LOG_TAG = BookMainActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    public String text = "";
    private BookAdapter mAdapter;
    private TextView mEmptyStateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_main);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());


        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateView = (TextView) findViewById(R.id.empty);
        bookListView.setEmptyView(mEmptyStateView);


        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);


        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book currentBook = mAdapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);
                }
            }
        });

        ImageView search = (ImageView) findViewById(R.id.image_View1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchText = (EditText) findViewById(R.id.edit_Text1);
                text = searchText.getText().toString().toLowerCase();


                //check the state of the connectivity
                final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                // get details of the network
                final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();
                    if (loaderManager.getLoader(BOOK_LOADER_ID) != null) {
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, BookMainActivity.this);
                    } else
                        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                        // because this activity implements the LoaderCallbacks interface).
                        loaderManager.initLoader(BOOK_LOADER_ID, null, BookMainActivity.this);


                } else {
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    //display error
                    mEmptyStateView.setText(R.string.no_net);
                }
            }

        });
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        return new BookLoader(this, BOOK_REQUEST_URL + text);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
        mEmptyStateView.setText(R.string.empty_page);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }


}

