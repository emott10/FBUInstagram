package com.mott.eric.fbuinstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mott.eric.fbuinstagram.fragments.DetailsFragment;
import com.mott.eric.fbuinstagram.fragments.ProfileFragment;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.ParseFile;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<Post> mPosts;
    Context context;

    //pass in the posts array in the constructor
    public PostAdapter(List<Post> posts){

        mPosts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder viewHolder, int i) {
        Post post = mPosts.get(i);
        ParseFile image = post.getImage();
        ParseFile profilePic = post.getUser().getParseFile("profilePic");
        if(profilePic != null) {
            Log.d("Test2", "Pic: " + profilePic.getUrl());
        }

        //format date string
        String date = post.getCreatedAt().toString();
        StringBuilder dateSB = new StringBuilder(date);
        StringBuilder dateAfterRemove = dateSB.delete(10, 23);

        viewHolder.tvHandle.setText(post.getUser().getUsername());
        viewHolder.tvDescHandle.setText(post.getUser().getUsername());
        viewHolder.tvDate.setText(dateAfterRemove);
        viewHolder.tvDesc.setText(post.getDescription());
        Glide.with(context)
                .load(image.getUrl())
                .into(viewHolder.ivPic);

        if(profilePic != null){
            viewHolder.ibProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context)
                    .load(profilePic.getUrl())
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCornersTransformation(75, 0)))
                    .into(viewHolder.ibProfilePic);
        }
        else {
            viewHolder.ibProfilePic.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context)
                    .load(R.drawable.defualt_profile_pic)
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCornersTransformation(75, 0)))
                    .into(viewHolder.ibProfilePic);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //create viewholder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvHandle;
        public ImageView ivPic;
        public TextView tvDesc;
        public TextView tvDescHandle;
        public TextView tvDate;
        public ImageButton ibHeart;
        public ImageButton ibProfilePic;

        public ViewHolder(View view){
            super(view);

            tvHandle = view.findViewById(R.id.tvHandle);
            ivPic = view.findViewById(R.id.ivPic);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvDescHandle = view.findViewById(R.id.tvDescHandle);
            tvDate = view.findViewById(R.id.tvCreated);
            ibHeart = view.findViewById(R.id.btnHeart);
            ibProfilePic = view.findViewById(R.id.ibtnProfilePic);

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

            ibProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Post post = mPosts.get(position);
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("postId", post);
                        Fragment fragment = new ProfileFragment();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();

                    }
                }
            });

            tvHandle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Post post = mPosts.get(position);
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("postId", post);
                        Fragment fragment = new ProfileFragment();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();

                    }
                }
            });

            tvDescHandle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Post post = mPosts.get(position);
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("postId", post);
                        Fragment fragment = new ProfileFragment();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();

                    }
                }
            });

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION){
                Post post = mPosts.get(position);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("postId", post);
                Fragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
            }
        }
    }

    //clean all elements of the recycler view
    public void clear(){
        mPosts.clear();
        notifyDataSetChanged();
    }

    //Add a list of items
    public void addAll(List<Post> list){
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
