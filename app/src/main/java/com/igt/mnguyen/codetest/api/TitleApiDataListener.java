package com.igt.mnguyen.codetest.api;

import com.igt.mnguyen.codetest.model.Album;
import com.igt.mnguyen.codetest.model.Title;

import java.util.List;

public interface TitleApiDataListener {

    public void onTitlesReceived(List<Title> titles);

}
