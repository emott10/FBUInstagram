package com.mott.eric.fbuinstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mPosts;
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

        //format date to string
       


        viewHolder.tvHandle.setText(post.getUser().getUsername());
        viewHolder.tvDescHandle.setText(post.getUser().getUsername());
        viewHolder.tvDate.setText(post.getCreatedAt().toString());
        viewHolder.tvDesc.setText(post.getDescription());
        Glide.with(context)
                .load(image.getUrl())
                .into(viewHolder.ivPic);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //create viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvHandle;
        public ImageView ivPic;
        public TextView tvDesc;
        public TextView tvDescHandle;
        public TextView tvDate;

        public ViewHolder(View view){
            super(view);

            tvHandle = view.findViewById(R.id.tvDetailHandle);
            ivPic = view.findViewById(R.id.ivDetailPic);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvDescHandle = view.findViewById(R.id.tvDescHandle);
            tvDate = view.findViewById(R.id.tvCreated);


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
