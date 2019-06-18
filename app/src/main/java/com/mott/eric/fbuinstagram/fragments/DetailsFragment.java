package com.mott.eric.fbuinstagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class DetailsFragment extends Fragment {
    private String postId;
    public TextView tvHandle;
    public ImageView ivPic;
    public TextView tvDesc;
    public TextView tvDescHandle;
    public TextView tvDate;
    public ImageButton ibHeart;

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

        ibHeart.setTag(R.drawable.ufi_heart);
        ibHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)ibHeart.getTag() == R.drawable.ufi_heart) {
                    ibHeart.setImageResource(R.drawable.ufi_heart_active);
                    ibHeart.setTag(R.drawable.ufi_heart_active);
                }
                else{
                    ibHeart.setImageResource(R.drawable.ufi_heart);
                    ibHeart.setTag(R.drawable.ufi_heart);
                }
            }
        });

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);

        query.getInBackground(postId, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if(e == null){
                    ParseFile image = post.getImage();

                    //format date string
                    String date = post.getCreatedAt().toString();
                    StringBuilder dateSB = new StringBuilder(date);
                    StringBuilder dateAfterRemove = dateSB.delete(10, 23);

                    //tvHandle.setText(post.getUser().getUsername());
                    //tvDescHandle.setText(post.getUser().getUsername());
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
    }
}
