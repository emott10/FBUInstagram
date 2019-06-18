package com.mott.eric.fbuinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mott.eric.fbuinstagram.R;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DetailsFragment extends Fragment {
    private String postId;
    public TextView tvHandle;
    public ImageView ivPic;
    public TextView tvDesc;
    public TextView tvDescHandle;
    public TextView tvDate;
    public ImageButton ibHeart;
    public TextView tvLikes;
    public Post mPost;
    public String likes;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postId = getArguments().getString("postId");
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvHandle = view.findViewById(R.id.tvHandle);
        ivPic = view.findViewById(R.id.ivPic);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvDescHandle = view.findViewById(R.id.tvDescHandle);
        tvDate = view.findViewById(R.id.tvDetailCreated);
        ibHeart = view.findViewById(R.id.btnHeart);
        tvLikes = view.findViewById(R.id.tvLikes);



        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);

        query.getInBackground(postId, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if(e == null){
                    mPost = post;
                    ParseFile image = post.getImage();
                    ParseUser user = post.getUser();

                    //format date string
                    String date = post.getCreatedAt().toString();
                    StringBuilder dateSB = new StringBuilder(date);
                    StringBuilder dateAfterRemove = dateSB.delete(10, 23);

                    //grab username
                    String name = "";
                    try {
                        name = user.fetchIfNeeded().getString("username");
                    } catch (ParseException error) {
                        Log.d("DetailsFragment", error.toString());
                        error.printStackTrace();
                    }

                    tvLikes.setText(post.getLikes());
                    tvHandle.setText(name);
                    tvDescHandle.setText(name);
                    tvDate.setText(dateAfterRemove);
                    tvDesc.setText(post.getDescription());
                    Glide.with(getContext())
                            .load(image.getUrl())
                            .into(ivPic);
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        ibHeart.setTag(R.drawable.ufi_heart);
        ibHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)ibHeart.getTag() == R.drawable.ufi_heart) {
                    likes = mPost.getLikes();
                    int addLike = Integer.valueOf(likes);
                    addLike++;
                    likes = String.valueOf(addLike);
                    mPost.setLikes(likes);
                    tvLikes.setText(String.valueOf(addLike));
                    ibHeart.setImageResource(R.drawable.ufi_heart_active);
                    ibHeart.setTag(R.drawable.ufi_heart_active);
                }
                else{
                    likes = mPost.getLikes();
                    int addLike = Integer.valueOf(likes);
                    addLike--;
                    likes = String.valueOf(addLike);
                    mPost.setLikes(likes);
                    tvLikes.setText(String.valueOf(addLike));
                    ibHeart.setImageResource(R.drawable.ufi_heart);
                    ibHeart.setTag(R.drawable.ufi_heart);
                }
            }
        });
    }
}
