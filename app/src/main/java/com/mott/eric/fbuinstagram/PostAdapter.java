package com.mott.eric.fbuinstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mott.eric.fbuinstagram.fragments.DetailsFragment;
import com.mott.eric.fbuinstagram.model.Post;
import com.parse.ParseFile;

import java.util.List;

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

        public ViewHolder(View view){
            super(view);

            tvHandle = view.findViewById(R.id.tvHandle);
            ivPic = view.findViewById(R.id.ivPic);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvDescHandle = view.findViewById(R.id.tvDescHandle);
            tvDate = view.findViewById(R.id.tvCreated);
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

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION){
                Post post = mPosts.get(position);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("postId", post.getObjectId());
                Fragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();

                /*Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(i);*/
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
