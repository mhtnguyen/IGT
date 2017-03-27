package com.igt.mnguyen.codetest.api.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.igt.mnguyen.codetest.api.AlbumApiDataListener;
import com.igt.mnguyen.codetest.api.TitleApiDataListener;
import com.igt.mnguyen.codetest.model.Title;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mnguyen on 3/26/2017.
 */

public class GetAlbumsTaskTest {

    @Test
    public void testDoInBackground() throws Exception {

        TitleApiDataListener mockTitleApiDataListener = mock(TitleApiDataListener.class);
        Context mockContext = mock(Context.class);

        NetworkInfo mockNetworkInfo = mock(NetworkInfo.class);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        ConnectivityManager mockConnectivityManager = mock(ConnectivityManager.class);
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager);
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);

        GetAlbumsTask getAlbumsTask = new GetAlbumsTask(mockTitleApiDataListener, mockContext);

        List<Title> titles = getAlbumsTask.doInBackground(null);
        assertTrue("did not find any titles", titles.size() > 0);
        Title title = titles.get(0);
        assertTrue("did not contain a title", title.getTitle() != null);
        assertTrue("did not contain an id", title.getId() != null);
        assertTrue("did not contain a userId", title.getUserId() != null);

    }
}
