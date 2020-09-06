package com.hjk.music_3.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hjk.music_3.R;
import com.hjk.music_3.Service.MusicApplication;
import com.hjk.music_3.data.local.model.YouTube;
import com.hjk.music_3.databinding.FragmentYoutubeBinding;
import com.hjk.music_3.ui.activity.PlayerActivity;
import com.hjk.music_3.ui.activity.YouTubePlayerActivity;
import com.hjk.music_3.ui.adapter.YouTubeAdapter;
import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;
import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;
import com.hjk.music_3.utils.ItemOffDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YouTubeFragment extends Fragment implements YouTubeAdapter.OnItemClickListener {

    YouTubeAdapter youTubeAdapter;

    SwipeRefreshLayout refreshLayout;

    FragmentYoutubeBinding binding;

    YouTubeViewModel youTubeModel;

    public static YouTubeFragment newInstance(){return new YouTubeFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        setHasOptionsMenu(true);
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_youtube,container,false);

        View root=binding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle save){
        super.onActivityCreated(save);
         getData();
    }

    public void getData(){
        youTubeModel= ViewModelProviders.of(this).get(YouTubeViewModel.class);

        youTubeModel.getAllYouTube().observe(this, y->{
            if(y!=null){
                setYouTube(y);
            }
        });
    }

    public void setYouTube(List<YouTube> y){
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int pos) {
                return 1;
            }
        });

        RecyclerView recyclerView_youtube_list=getActivity().findViewById(R.id.youtube_list);
        youTubeAdapter=new YouTubeAdapter(y,this);

        recyclerView_youtube_list.setAdapter(youTubeAdapter);
        recyclerView_youtube_list.setLayoutManager(gridLayoutManager);
        ItemOffDecoration itemOffDecoration=new ItemOffDecoration(getActivity(),R.dimen.item_offset);
        recyclerView_youtube_list.addItemDecoration(itemOffDecoration);
    }

    @Override
    public void onItemClicked(int pos, ImageView imageView) throws Exception {
        final int pos_=pos;
        Intent intent=new Intent(getActivity(), YouTubePlayerActivity.class);
        youTubeModel.getAllYouTube().observe(this,y->{
            youTubeModel.set_current_youtube(y.get(pos_));
             intent.putExtra("youtube_url",youTubeModel.current_youtube().getMp4());
        });


        startActivity(intent);
    }
}
