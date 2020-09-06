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

import com.hjk.music_3.R;
import com.hjk.music_3.Service.MusicApplication;
import com.hjk.music_3.data.local.model.Music;

import com.hjk.music_3.databinding.FragmentMusicBinding;
import com.hjk.music_3.databinding.ItemMusicBinding;
import com.hjk.music_3.ui.activity.PlayerActivity;
import com.hjk.music_3.ui.adapter.MusicAdapter;

import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.utils.ItemOffDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MusicFragment extends Fragment implements MusicAdapter.OnItemClickListener{

    MusicAdapter musicAdapter;

    FragmentMusicBinding binding;

    MusicViewModel musicViewModel;

    ItemMusicBinding itemMusicBinding;


    public static MusicFragment newInstance(){return new MusicFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        setHasOptionsMenu(true);

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_music,container,false);
        itemMusicBinding=DataBindingUtil.inflate(inflater,R.layout.item_music,container,false);

        View root=binding.getRoot();



        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        getData();
    }




    public void getData(){
        musicViewModel= ViewModelProviders.of(this).get(MusicViewModel.class);

        musicViewModel.getAllMusic().observe(this,musicViewModel->{
           if(musicViewModel!=null){
               setMusic(musicViewModel);
               MusicApplication.getInstance().getServiceInterface().setViewModel( ViewModelProviders.of(this).get(MusicViewModel.class));

           }
        });
    }

    public void setMusic(List<Music> m){
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int pos) {
                return 1;
            }
        });

        RecyclerView recyclerView_music_list=getActivity().findViewById(R.id.music_list);
        musicAdapter=new MusicAdapter(m,this);

        recyclerView_music_list.setAdapter(musicAdapter);
        recyclerView_music_list.setLayoutManager(gridLayoutManager);
        ItemOffDecoration itemOffDecoration=new ItemOffDecoration(getActivity(),R.dimen.item_offset);
        recyclerView_music_list.addItemDecoration(itemOffDecoration);
    }

    @Override
    public void onItemClicked(int pos, ImageView imageView) throws Exception{
        final int pos_=pos;

        System.out.println("클릭"+pos_);

        musicViewModel.setPos(pos_);
        musicViewModel.set_current_music(pos_);
        MusicApplication.getInstance().getServiceInterface().init();
        Intent intent=new Intent(getActivity(), PlayerActivity.class);
        startActivity(intent);



        itemMusicBinding.title.setText("재생중");


    }
}
