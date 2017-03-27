package com.igt.mnguyen.codetest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.igt.mnguyen.codetest.R;
import com.igt.mnguyen.codetest.adapter.OnAlbumClickListener;
import com.igt.mnguyen.codetest.adapter.RecyclerAdapter;
import com.igt.mnguyen.codetest.api.AlbumApiDataListener;
import com.igt.mnguyen.codetest.api.task.GetThumbsTask;
import com.igt.mnguyen.codetest.model.Album;

import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private Context context;
    private String albumId;
    private final static String TAG = PhotoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        context=this;
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_photo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            albumId = extras.getString("albumId");
            Log.i(TAG, "albumId: "+albumId);
        }

        //call back when finished
        AlbumApiDataListener listener = new AlbumApiDataListener() {
            @Override
            public void onAlbumsReceived(List<Album> albums) {
                recyclerAdapter=new RecyclerAdapter(context,"album",albums,new OnAlbumClickListener() {
                    @Override public void onItemClick(Album item) {
                        Log.i(TAG, "Album Clicked: "+item.getThumbnailUrl());
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(PhotoActivity.this));
                recyclerView.setAdapter(recyclerAdapter);
            }
        };

        if(albumId!=null) {
            Log.i(TAG, "Get thumbnails.");
            //Make call to get thumbnails
            new GetThumbsTask(albumId, listener, this).execute();
        }
    }
}
