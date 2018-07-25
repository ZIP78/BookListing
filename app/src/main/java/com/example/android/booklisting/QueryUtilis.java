package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklisting.BookMainActivity.LOG_TAG;


/**
 * Created by Paul on 10/2/2017.
 */

public final class QueryUtilis {


    private QueryUtilis() {
    }

    public static List<Book> extractFeatureFromJSON(String UnderpantsJSON) {

        if (TextUtils.isEmpty(UnderpantsJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(UnderpantsJSON);

            if (jsonResponse.has("items")) {
                JSONArray booksArray = jsonResponse.getJSONArray("items");

                for (int i = 0; i < booksArray.length(); i++) {

                    JSONObject currentBook = booksArray.getJSONObject(i);

                    JSONObject volume = currentBook.getJSONObject("volumeInfo");

                    String title = "N/A";
                    if (volume.has("title")) {
                        title = volume.getString("title");
                    }

                    String url = "N/A";
                    if (volume.has("previewLink")) {
                        url = volume.getString("previewLink");
                    }


                    String authorName = "N?A";
                    if (volume.has("authors")) {
                        JSONArray auth = volume.getJSONArray("authors");
                        authorName = auth.getString(0);
                    }

                    if (volume.has("imageLinks")) {
                        JSONObject image = volume.getJSONObject("imageLinks");

                        String thumbnails = "N/A";
                        if (volume.has("imageLinks")) {
                            thumbnails = image.getString("thumbnail");
                        }

                        Book book = new Book(title, authorName, url, thumbnails);

                        books.add(book); //books = array name
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }

    //the networking process
    public static List fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List books = extractFeatureFromJSON(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
