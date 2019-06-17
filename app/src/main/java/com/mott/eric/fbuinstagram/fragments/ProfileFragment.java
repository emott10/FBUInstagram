package com.mott.eric.fbuinstagram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mott.eric.fbuinstagram.LoginActivity;
import com.mott.eric.fbuinstagram.MainActivity;
import com.mott.eric.fbuinstagram.PostAdapter;
import com.mott.eric.fbuinstagram.R;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private Button btnLogOut;
    private TextView tvUsername;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnLogOut = view.findViewById(R.id.btnLogout);
        tvUsername = view.findViewById(R.id.tvUsername);

        ParseUser user = ParseUser.getCurrentUser();

        tvUsername.setText(user.getUsername());

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
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
        postQuery.profileQuery().withUser();
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
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }
}
