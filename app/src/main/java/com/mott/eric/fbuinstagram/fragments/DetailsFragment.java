package com.mott.eric.fbuinstagram.fragments;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mott.eric.fbuinstagram.R;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsFragment extends Fragment {
    public TextView tvHandle;
    public ImageView ivPic;
    public TextView tvDesc;
    public TextView tvDescHandle;
    public TextView tvDate;
    public ImageView ivProfilePic;
    public ImageButton ibHeart;
    public TextView tvLikes;
    public Post post;
    public String likes;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        post = (Post) getArguments().getSerializable("postId");
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
        ivProfilePic = view.findViewById(R.id.ivProfilePicDetails);

        ParseFile image = post.getImage();
        ParseUser user = post.getUser();
        ParseFile profilePic = null;

        try {
            profilePic = user.fetchIfNeeded().getParseFile("profilePic");
        }catch (ParseException error){
            error.printStackTrace();
        }

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

        if(profilePic != null){
            ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext())
                    .load(profilePic.getUrl())
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCornersTransformation(75, 0)))
                    .into(ivProfilePic);
        }

        ibHeart.setTag(R.drawable.ufi_heart);
        ibHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)ibHeart.getTag() == R.drawable.ufi_heart) {
                    likes = post.getLikes();
                    int addLike = Integer.valueOf(likes);
                    addLike++;
                    likes = String.valueOf(addLike);
                    post.setLikes(likes);
                    tvLikes.setText(String.valueOf(addLike));
                    ibHeart.setImageResource(R.drawable.ufi_heart_active);
                    ibHeart.setTag(R.drawable.ufi_heart_active);
                }
                else{
                    likes = post.getLikes();
                    int addLike = Integer.valueOf(likes);
                    addLike--;
                    likes = String.valueOf(addLike);
                    post.setLikes(likes);
                    tvLikes.setText(String.valueOf(addLike));
                    ibHeart.setImageResource(R.drawable.ufi_heart);
                    ibHeart.setTag(R.drawable.ufi_heart);
                }
            }
        });
    }
}
