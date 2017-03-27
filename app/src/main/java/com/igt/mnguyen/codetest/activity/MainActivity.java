package com.igt.mnguyen.codetest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.igt.mnguyen.codetest.R;
import com.igt.mnguyen.codetest.adapter.OnTitleClickListener;
import com.igt.mnguyen.codetest.adapter.RecyclerAdapter;
import com.igt.mnguyen.codetest.api.TitleApiDataListener;
import com.igt.mnguyen.codetest.api.task.GetAlbumsTask;
import com.igt.mnguyen.codetest.model.Title;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private Context context;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_main);
        //call back when finished
        TitleApiDataListener listener = new TitleApiDataListener() {
            @Override
            public void onTitlesReceived(List<Title> titles) {
                recyclerAdapter=new RecyclerAdapter(context,"title",titles,new OnTitleClickListener() {
                    @Override public void onItemClick(Title item) {
                        Log.i(TAG, "Title Clicked: "+item.getId());
                        String albumId = item.getId();
                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("albumId",albumId);
                        startActivity(intent);

                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(recyclerAdapter);

            }
        };
        Log.i(TAG, "Get Albums.");
        //Make call to get albums
        new GetAlbumsTask(listener,this).execute();
    }


}
