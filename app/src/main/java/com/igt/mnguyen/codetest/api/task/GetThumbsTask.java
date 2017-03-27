package com.igt.mnguyen.codetest.api.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.igt.mnguyen.codetest.R;
import com.igt.mnguyen.codetest.api.AlbumApiDataListener;
import com.igt.mnguyen.codetest.model.Album;
import com.igt.mnguyen.codetest.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.igt.mnguyen.codetest.util.Util.isNetworkAvailable;

/**
 *
 * AsyncTask is a fast way to make things work without blocking UI thread
 */

public class GetThumbsTask extends AsyncTask<Void, Void, List<Album>>{
    private AlbumApiDataListener delegate;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/photos?albumId=";
    private ProgressDialog mProgressDialog;
    private HttpURLConnection httpURLConnection;
    private URL url = null;
    private List<Album> albums = new ArrayList<Album>();
    private String response = "";
    private int responseCode=-1;
    private Context context;
    private String albumId;
    private final static String TAG = GetThumbsTask.class.getSimpleName();

    public GetThumbsTask(String albumId,AlbumApiDataListener delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
        this.albumId = albumId;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);

        //this method will be running on UI thread
        mProgressDialog.setMessage("\tLoading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

    }

    public List<Album> doInBackground(Void... params) {

        try {

            if(isNetworkAvailable(context)) {
                url = new URL(BASE_URL+albumId);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.addRequestProperty("User-Agent",Util.getUserAgent(context));
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpURLConnection.getInputStream();
                    // read response
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "\n";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    httpURLConnection.disconnect();
                    JSONArray jsonArray = new JSONArray(response);
                    int count = jsonArray.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String albumId = String.valueOf(jsonObject.getInt("albumId"));
                        String id = String.valueOf(jsonObject.getInt("id"));
                        String title = jsonObject.getString("title");
                        String url = jsonObject.getString("url");
                        String thumbnailUrl = jsonObject.getString("thumbnailUrl");
                        albums.add(new Album(albumId, id, title,url,thumbnailUrl));
                    }
                    Log.i(TAG, "Status code  200 for " + url);
                    return albums;
                }
            }else{
                Log.w(TAG, "Internet connection not available ");
                String title = context.getResources().getString(R.string.internet_not_available);
                String message = context.getResources().getString(R.string.check_connection);
                Util.showDialog(context,title,message);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        // if response failed or no connection
        Log.w(TAG, "Status code not 200 for " + url + " status code: "+responseCode);
        return null;
    }

    public void onPostExecute(List<Album> result) {
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }

        if (result != null) {
            Log.i(TAG, "Processing Albums received: "+result.size());
            delegate.onAlbumsReceived(result);
        }else{
            Log.w(TAG, "Albums list was empty!");
            String title = context.getResources().getString(R.string.failed_title);
            String message = context.getResources().getString(R.string.try_again);
            Util.showDialog(context,title,message);
        }
    }
}
