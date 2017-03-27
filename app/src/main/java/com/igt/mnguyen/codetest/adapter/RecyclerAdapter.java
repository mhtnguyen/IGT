package com.igt.mnguyen.codetest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igt.mnguyen.codetest.R;
import com.igt.mnguyen.codetest.model.Album;
import com.igt.mnguyen.codetest.model.Title;
import com.igt.mnguyen.codetest.util.BitmapLruCache;
import com.igt.mnguyen.codetest.util.Util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Title> titleData= Collections.emptyList();
    private List<Album> albumData= Collections.emptyList();
    private OnTitleClickListener titleListener=null;
    private OnAlbumClickListener albumListener=null;
    private Bitmap bitmap;
    private Bitmap image;
    private String page="";
    private final static String TAG = RecyclerAdapter.class.getSimpleName();

    public RecyclerAdapter(Context context, String page, List<Title> titleData,OnTitleClickListener titleListener){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.titleData=titleData;
        this.titleListener = titleListener;
        this.page=page;
    }

    public RecyclerAdapter(Context context,String page, List<Album> albumData,OnAlbumClickListener albumListener){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.albumData=albumData;
        this.albumListener = albumListener;
        this.page=page;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=null;
        RecyclerView.ViewHolder viewHolder =null;
        if(page == "title") {
            view =inflater.inflate(R.layout.title_row, parent,false);
            viewHolder  = new TitleViewHolder(view);

        }else if(page == "album"){
            view = inflater.inflate(R.layout.photo_row, parent, false);
            viewHolder  = new ImageViewHolder(view);

        }
        return viewHolder ;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {
            // Get current position of item in recyclerview to bind data and assign values from list
            if (page == "title") {
                TitleViewHolder viewHolder = (TitleViewHolder) holder;
                Title current = titleData.get(position);
                viewHolder.textAlbumName.setText(current.getTitle());
                //set onclick listener
                viewHolder.bind(current, titleListener);
            } else if (page == "album") {
                Album current = albumData.get(position);
                String imageUrl = current.getThumbnailUrl();
                Log.d(TAG, "Request URL ... " + imageUrl);
                ImageViewHolder viewHolder = (ImageViewHolder) holder;
                //set onclick listener
                viewHolder.bind(current, albumListener);
                //check cache first

                if (BitmapLruCache.getInstance().getLru().get(imageUrl) != null) {
                    Log.i(TAG, "cache hit " + imageUrl);
                    bitmap = BitmapLruCache.getInstance().getLru().get(imageUrl);
                    viewHolder.imageAlbumThumb.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                } else {
                    LoadImageTask task = new LoadImageTask(viewHolder);
                    task.execute(imageUrl);
                }
            }
        }catch(Exception ex){
            Log.e(TAG,ex.getMessage());
        }
    }

    // return total item from List
    @Override
    public int getItemCount() {
        int dataSize=0;
        if(page == "title") {
            dataSize=titleData.size();
        }else if(page == "album") {
            dataSize=albumData.size();
        }
        return dataSize;
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{

        TextView textAlbumName;

        // create constructor to get widget reference
        public TitleViewHolder(View itemView) {
            super(itemView);
            textAlbumName= (TextView) itemView.findViewById(R.id.textAlbumName);
        }

        public void bind(final Title title, final OnTitleClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    titleListener.onItemClick(title);
                }
            });
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageAlbumThumb;
        // create constructor to get widget reference
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageAlbumThumb = (ImageView) itemView.findViewById(R.id.album_thumb);
        }

        public void bind(final Album album, final OnAlbumClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    albumListener.onItemClick(album);
                }
            });
        }
    }

    class LoadImageTask extends AsyncTask<String,String,Bitmap> {
        private String thumbUrl;
        private HttpURLConnection connection=null;
        private ImageViewHolder viewHolder;

        public LoadImageTask(ImageViewHolder viewHolder) {
            this.viewHolder=viewHolder;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            thumbUrl=strings[0];

            try {
                URL uri = new URL(thumbUrl);
                connection = (HttpURLConnection) uri.openConnection();
                connection.setReadTimeout(5000);
                connection.addRequestProperty("User-Agent", "Mozilla");
                Log.i(TAG,"Request URL ... " + thumbUrl);

                int statusCode = connection.getResponseCode();
                boolean redirect = false;

                if (statusCode != HttpURLConnection.HTTP_OK) {

                    Log.w(TAG, "Status code not 200 for " + thumbUrl + " status code: "+statusCode);
                    //handle redirects
                    if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                            || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                            || statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
                        redirect = true;
                    }else {
                        //send to onPostExecute
                        return null;
                    }
                }

                if (redirect) {

                    // get redirect url from "location" header field
                    String newUrl = connection.getHeaderField("Location");

                    // get the cookie if need, for login
                    String cookies = connection.getHeaderField("Set-Cookie");

                    // open the new connnection again
                    connection = (HttpURLConnection) new URL(newUrl).openConnection();
                    connection.setRequestProperty("Cookie", cookies);
                    connection.addRequestProperty("User-Agent",Util.getUserAgent(context));
                    Log.i(TAG,"Redirect to URL : " + newUrl);
                }

                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    BitmapLruCache.getInstance().getLru().put(thumbUrl,bitmap);
                    return bitmap;
                }
            } catch (Exception e) {
                connection.disconnect();
                Log.e(TAG, "Error downloading image from " + thumbUrl +" "+e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            if(b == null) {
                Log.w(TAG,"Error in loading image");
                String title = context.getResources().getString(R.string.failed_title);
                String message = context.getResources().getString(R.string.try_again);
                Util.showDialog(context,title,message);
            }
            else {
                image=b;
                Log.i(TAG,"loading image: "+thumbUrl);
                viewHolder.imageAlbumThumb.setImageBitmap(image); //Sets the Bitmap to ImageView
            }
        }
    }
}
