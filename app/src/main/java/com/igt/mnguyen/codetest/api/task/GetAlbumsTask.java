package com.igt.mnguyen.codetest.api.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.igt.mnguyen.codetest.R;
import com.igt.mnguyen.codetest.api.TitleApiDataListener;
import com.igt.mnguyen.codetest.model.Title;
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

public class GetAlbumsTask extends AsyncTask<Void, Void, List<Title>>{
    private TitleApiDataListener delegate;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/albums";
    private ProgressDialog mProgressDialog;
    private HttpURLConnection httpURLConnection;
    private URL url = null;
    private List<Title> titles = new ArrayList<Title>();
    private String response = "";
    private int responseCode=-1;
    private Context context;
    private final static String TAG = GetAlbumsTask.class.getSimpleName();

    public GetAlbumsTask(TitleApiDataListener delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
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

    public List<Title> doInBackground(Void... params) {
        try {

            if(isNetworkAvailable(context)) {
                url = new URL(BASE_URL);
                Log.i(TAG,"Request URL ... " + url);
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
                        String userId = String.valueOf(jsonObject.getInt("userId"));
                        String id = String.valueOf(jsonObject.getInt("id"));
                        String title = jsonObject.getString("title");
                        titles.add(new Title(userId, id, title));
                    }
                    Log.i(TAG, "Status code  200 for " + url);
                    return titles;
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

    public void onPostExecute(List<Title> result) {
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }

        if (result != null) {
            Log.i(TAG, "Processing titles received: "+result.size());
            delegate.onTitlesReceived(result);
        }else{
            Log.w(TAG, "Titles list was empty!");
            String title = context.getResources().getString(R.string.failed_title);
            String message = context.getResources().getString(R.string.try_again);
            Util.showDialog(context,title,message);
        }
    }
}
