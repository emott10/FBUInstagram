package com.mott.eric.fbuinstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mott.eric.fbuinstagram.LoginActivity;
import com.mott.eric.fbuinstagram.PostAdapter;
import com.mott.eric.fbuinstagram.R;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    private Button btnProfilePicSwap;
    private TextView tvUsername;
    private ImageView ivProfilePic;
    private TextView tvNoImages;
    public ImageView ivIcon;
    public ImageView ivTitle;
    ParseUser user;
    ParseFile profilePic;
    Post post;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        if(getArguments() != null) {
            post = (Post) getArguments().getSerializable("postId");
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        btnLogout = view.findViewById(R.id.btnLogout);
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvNoImages = view.findViewById(R.id.tvNoImages);
        btnProfilePicSwap = view.findViewById(R.id.btnSwapProfilePic);
        ivIcon = getActivity().findViewById(R.id.ivIcon);
        ivTitle = getActivity().findViewById(R.id.ivTitle);

        ivIcon.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.VISIBLE);

        if(post == null){
            user = ParseUser.getCurrentUser();
            try {
                profilePic = user.fetchIfNeeded().getParseFile("profilePic");
            }catch (ParseException error){
                error.printStackTrace();
            }
            tvUsername.setText(user.getUsername());
            ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(view.getContext())
                    .load(profilePic.getUrl())
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCornersTransformation(75, 0)))
                    .into(ivProfilePic);
        }
        else{
            btnLogout.setVisibility(View.GONE);
            btnProfilePicSwap.setVisibility(View.GONE);
            user = post.getUser();
            try {
                profilePic = user.fetchIfNeeded().getParseFile("profilePic");
            }catch (ParseException error){
                error.printStackTrace();
            }
            tvUsername.setText(user.getUsername());
            ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if(profilePic != null){
                ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getContext())
                        .load(profilePic.getUrl())
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCornersTransformation(75, 0)))
                        .into(ivProfilePic);
            }
            else {
                ivProfilePic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(getContext())
                        .load(R.drawable.defualt_profile_pic)
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCornersTransformation(75, 0)))
                        .into(ivProfilePic);
            }

        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnProfilePicSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                Fragment fragment = new ProfilePicFragment();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
            }
        });


        //find recyclerview for posts
        rvPosts = view.findViewById(R.id.rvTimelineProfile);

        //create data source
        posts = new ArrayList<>();

        //construct posts adapter
        postAdapter = new PostAdapter(posts);

        //setup recyclerview
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        //set the adapter
        rvPosts.setAdapter(postAdapter);

        loadTopPosts();
    }

    protected void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.profileQuery(user).withUser();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null){
                    for(int i = 0; i < objects.size(); i++){
                        Log.d("MainActivity", "Post[" + i + "] = " + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());

                        Post post = objects.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size() - 1);

                    }

                    if(posts.isEmpty()){
                        rvPosts.setVisibility(View.GONE);
                        tvNoImages.setText(user.getUsername() + " has no pictures yet.");
                        tvNoImages.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }
}


//Additional feature to add, popup dialog for settings

//Dialog dialog;
//dialog = new Dialog(getActivity());
//btnSettings.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        dialog.setContentView(R.layout.popup_settings);
//        //Button btnLogout = view.findViewById(R.id.b)
//
//        dialog.show();
//        }
//        });