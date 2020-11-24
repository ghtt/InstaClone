package com.akrasnoyarov.instaclone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akrasnoyarov.instaclone.R;
import com.akrasnoyarov.instaclone.database.entity.MediaEntity;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostFeedAdapter extends RecyclerView.Adapter<PostFeedAdapter.PostFeedHolder> {
    private LayoutInflater mInflater;
    private List<MediaEntity> mMedia;
    private Context mContext;

    public PostFeedAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public PostFeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = mInflater.inflate(R.layout.post_feed_item, parent, false);
        return new PostFeedHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostFeedHolder holder, int position) {
        holder.mTextViewUsername.setText(mMedia.get(position).getUsername());
        holder.mTextViewCaption.setText(mMedia.get(position).getCaption());
        if (mMedia.get(position).getMediaType().equals("VIDEO")) {
            holder.mImageViewPost.setVisibility(View.GONE);
            holder.mVideoView.setVideoPath(mMedia.get(position).getMediaUrl());
        } else {
            holder.mVideoView.setVisibility(View.GONE);
            Glide.with(mContext).load(mMedia.get(position).getMediaUrl())
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .into(holder.mImageViewPost);
        }
    }

    @Override
    public int getItemCount() {
        return (mMedia != null) ? mMedia.size() : 0;
    }

    public void setMedia(List<MediaEntity> media) {
        mMedia = media;
        notifyDataSetChanged();
    }

    class PostFeedHolder extends RecyclerView.ViewHolder {
        ImageView mImageViewUserAvatar, mImageViewPost;
        VideoView mVideoView;
        TextView mTextViewUsername, mTextViewFooterUsername, mTextViewCaption;
        ImageButton mImageButtonLike, mImageButtonComment, mImageButtonReply;

        public PostFeedHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            mImageViewPost = itemView.findViewById(R.id.iv_post_img);

            mVideoView = itemView.findViewById(R.id.iv_post_video);
            MediaController mediaController = new MediaController(mContext);
            mVideoView.setMediaController(mediaController);
            //mediaController.setAnchorView(mVideoView);


            mTextViewUsername = itemView.findViewById(R.id.tv_user_name);
            mTextViewFooterUsername = itemView.findViewById(R.id.tv_footer_user_name);
            mTextViewCaption = itemView.findViewById(R.id.tv_caption);

            mImageButtonLike = itemView.findViewById(R.id.ib_like);
            mImageButtonReply = itemView.findViewById(R.id.ib_reply);
            mImageButtonComment = itemView.findViewById(R.id.ib_comment);

        }
    }
}
