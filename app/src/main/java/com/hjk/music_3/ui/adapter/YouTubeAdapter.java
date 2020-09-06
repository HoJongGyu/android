package com.hjk.music_3.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hjk.music_3.R;
import com.hjk.music_3.data.local.model.YouTube;
import com.hjk.music_3.databinding.ItemYoutubeBinding;

import java.util.List;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.ItemViewHolder> {

    private List<YouTube> youtube;
    YouTubeAdapter.OnItemClickListener onItemClickListener;

    public YouTubeAdapter(List<YouTube> youtube, OnItemClickListener itemClickListener){
        this.youtube=youtube;
        this.onItemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public YouTubeAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemYoutubeBinding itemYoutubeBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_youtube,parent,false);
        return new YouTubeAdapter.ItemViewHolder(itemYoutubeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeAdapter.ItemViewHolder holder,int pos) {
        holder.bind(pos);
    }

    @Override
    public int getItemCount(){
        if(youtube==null)
            return 0;
        return youtube.size();
    }

    public interface OnItemClickListener{
        void onItemClicked(int pos, ImageView imageView) throws Exception;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ItemYoutubeBinding binding;
        ImageView youtube_image;

        ItemViewHolder(@NonNull ItemYoutubeBinding itemView){
            super(itemView.getRoot());
            binding=itemView;
            itemView.getRoot().setOnClickListener(this);
            youtube_image=itemView.getRoot().findViewById(R.id.image);
        }

        void bind(int pos){
            YouTube youtube_item=youtube.get(pos);
            binding.setYou(youtube_item);
        }

        @Override
        public void onClick(View v){
            try{
                onItemClickListener.onItemClicked(getAdapterPosition(),youtube_image);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
