package hu.bme.aut.amorg.examples.firebaseHGL57H.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.amorg.examples.firebaseHGL57H.R;
import hu.bme.aut.amorg.examples.firebaseHGL57H.data.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvTitle;
        public TextView tvBody;
        public ImageView imgPost;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
        }
    }

    private Context context;
    private List<Post> postList;
    private int lastPosition = -1;

    public PostsAdapter(Context context) {
        this.context = context;
        this.postList = new ArrayList<Post>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_post, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Post tmpPost = postList.get(position);
        viewHolder.tvAuthor.setText(tmpPost.getAuthor());
        viewHolder.tvTitle.setText(tmpPost.getTitle());
        viewHolder.tvBody.setText(tmpPost.getBody());

        if (!TextUtils.isEmpty(tmpPost.getImageUrl())) {
            Glide.with(context).load(tmpPost.getImageUrl()).into(viewHolder.imgPost);
            viewHolder.imgPost.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgPost.setVisibility(View.GONE);
        }

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addPost(Post post, String key) {
        postList.add(post);
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}