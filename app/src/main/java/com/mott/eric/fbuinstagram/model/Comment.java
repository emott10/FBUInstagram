package com.mott.eric.fbuinstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    private static final String KEY_BODY = "body";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_USER = "user";
    private static final String KEY_POST = "Post";

    public String getBody(){
        return getString(KEY_BODY);
    }

    public void setBody(String desc){
        put(KEY_BODY, desc);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public ParseObject getPost(){ return getParseObject(KEY_POST); }

    public static class Query extends ParseQuery<Comment> {
        public Query(){
            super(Comment.class);
        }

        public Query getComments(){
            setLimit(50);
            addDescendingOrder(KEY_CREATED_AT);
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }

        public Query withPost(){
            include("Post");
            return this;
        }
    }
}
