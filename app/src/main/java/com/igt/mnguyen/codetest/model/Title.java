package com.igt.mnguyen.codetest.model;

public class Title {

    private String id;

    private String title;

    private String userId;

    public Title(String userId,String id,String title){
        this.userId=userId;
        this.id=id;
        this.title=title;
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

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "{\"userId\" = "+userId+", \"id\" = "+id+", \"title\" = "+title+"}";
    }
}
