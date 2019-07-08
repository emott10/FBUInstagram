package com.mott.eric.fbuinstagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mott.eric.fbuinstagram.R;
import com.mott.eric.fbuinstagram.model.Comment;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsFragment extends Fragment {
    public TextView tvHandle;
    public ImageView ivPic;
    public TextView tvDesc;
    public TextView tvDescHandle;
    public TextView tvDate;
    public ImageView ivProfilePic;
    public ImageButton ibHeart;
    public ImageButton ibComment;
    public TextView tvLikes;
    public Post post;
    public String likes;
    public EditText etComment;
    public Button btnSendComment;
    public ImageView ivIcon;
    public ImageView ivTitle;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        post = (Post) getArguments().getSerializable("postId");
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queryComments();

        tvHandle = view.findViewById(R.id.tvHandle);
        ivPic = view.findViewById(R.id.ivPic);
        tvDesc = view.findViewById(R.id.tvDesc);
        tvDescHandle = view.findViewById(R.id.tvDescHandle);
        tvDate = view.findViewById(R.id.tvDetailCreated);
        ibHeart = view.findViewById(R.id.btnHeart);
        tvLikes = view.findViewById(R.id.tvLikes);
        ivProfilePic = view.findViewById(R.id.ivProfilePicDetails);
        ibComment = view.findViewById(R.id.btnCommentDetails);
        etComment = view.findViewById(R.id.etComment);
        btnSendComment = view.findViewById(R.id.btnSendComment);
        ivIcon = getActivity().findViewById(R.id.ivIcon);
        ivTitle = getActivity().findViewById(R.id.ivTitle);

        ivIcon.setVisibility(View.GONE);
        ivTitle.setVisibility(View.GONE);

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
                    addLike(true);
                    ibHeart.setImageResource(R.drawable.ufi_heart_active);
                    ibHeart.setTag(R.drawable.ufi_heart_active);
                }
                else{
                    addLike(false);
                    ibHeart.setImageResource(R.drawable.ufi_heart);
                    ibHeart.setTag(R.drawable.ufi_heart);
                }
            }
        });

        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etComment.setVisibility(View.VISIBLE);
                btnSendComment.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                etComment.setFocusableInTouchMode(true);
            }
        });

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SendComment In the Works", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Send true to add a like, false to subtract a like
    public void addLike(boolean isAdd){
        likes = post.getLikes();
        int newLike = Integer.valueOf(likes);

        if(isAdd){
            newLike++;
        }
        else{
            newLike--;
        }

        likes = String.valueOf(newLike);
        post.setLikes(likes);
        post.saveInBackground();
        tvLikes.setText(String.valueOf(newLike));
    }

    public void queryComments(){
//        final Comment.Query commentQuery = new Comment.Query();
//        commentQuery.getComments().withPost().withUser();
//        commentQuery.findInBackground(new FindCallback<Comment>() {
//            @Override
//            public void done(List<Comment> objects, ParseException e) {
//                if(e == null){
//                    for(int i = 0; i < objects.size(); i++){
//                        int test = objects.size();
//                        Log.d("Comment", "Comment[" + i + "] = "
//                                + "\nusername = " + objects.get(i).getUser());
//                    }
//                }
//                else{
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
