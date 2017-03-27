package com.igt.mnguyen.codetest.api;

import com.igt.mnguyen.codetest.model.Album;

import java.util.List;

public interface AlbumApiDataListener {

    public void onAlbumsReceived(List<Album> albums);

}
