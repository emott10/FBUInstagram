package com.mott.eric.fbuinstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Post")
public class Post extends ParseObject implements Serializable {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_LIKES = "likes";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String desc){
        put(KEY_DESCRIPTION, desc);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public String getLikes() { return getString(KEY_LIKES); }

    public void setLikes(String likes){ put(KEY_LIKES, likes);}


    public static class Query extends ParseQuery<Post> {
        public Query(){
            super(Post.class);
        }

        public Query getTop(){
            setLimit(20);
            addDescendingOrder(KEY_CREATED_AT);
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }

        public Query profileQuery(ParseUser user){
            setLimit(20);
            addDescendingOrder(KEY_CREATED_AT);
            whereEqualTo(KEY_USER, user);
            return this;
        }
    }
}
