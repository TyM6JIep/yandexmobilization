package com.arsanima.yandexmobilization.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.views.ArtistView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuriF on 19.04.16.
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistView> {

    private List<Artist> listData = new ArrayList<>();
    private ArtistView.ArtistViewListener mListener;

    public ArtistsAdapter(ArtistView.ArtistViewListener listener) {
        mListener = listener;
    }

    public void addItems(List<Artist> items) {
        listData.addAll(items);
    }

    public void setListItems(List<Artist> items) {
        listData = items;
    }

    @Override
    public ArtistView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item_artist, parent, false);
        return new ArtistView(v, mListener);
    }

    @Override
    public void onBindViewHolder(ArtistView holder, int position) {
        holder.setArtist(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void clearListItem() {
        listData.clear();
    }

}
