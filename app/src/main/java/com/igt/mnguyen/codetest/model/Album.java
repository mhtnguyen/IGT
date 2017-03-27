package com.igt.mnguyen.codetest.model;

public class Album {

    private String id;

    private String title;

    private String albumId;

    private String thumbnailUrl;

    private String url;

    public Album(String albumId, String id, String title, String url, String thumbnailUrl){
        this.albumId=albumId;
        this.id=id;
        this.title=title;
        this.url=url;
        this.thumbnailUrl=thumbnailUrl;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getAlbumId ()
    {
        return albumId;
    }

    public void setAlbumId (String albumId)
    {
        this.albumId = albumId;
    }

    public String getThumbnailUrl ()
    {
        return thumbnailUrl;
    }

    public void setThumbnailUrl (String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "{\"albumId\" = "+albumId+", \"id\" = "+id+", \"title\" = "+title+", \"url\" = "+url+", \"thumbnailUrl\" = "+thumbnailUrl+"}";
    }
}
