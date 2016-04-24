package com.arsanima.yandexmobilization.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arsanima.yandexmobilization.ArtistActivity;
import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.adapters.ArtistsAdapter;
import com.arsanima.yandexmobilization.helpers.StorageHelper;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.views.ArtistView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        ArtistView.ArtistViewListener{

    public static final String TAG = "favouritesfragment";

    private RecyclerView mRecyclerView;
    private ArtistsAdapter mAdapter;
    private SwipeRefreshLayout swipeLayout;
    private Gson gson = new Gson();
    private TextView textViewEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeLayout.setOnRefreshListener(this);

        textViewEmpty = (TextView) view.findViewById(R.id.textViewEmpty);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ArtistsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        onRefresh(); // при создании активити обновляем список
    }

    @Override
    public void onArtistClick(Artist artist, int color, int colorDark) {
        if (artist != null) {
            Intent intent = new Intent(getActivity(), ArtistActivity.class);
            intent.putExtra(ArtistActivity.CURRENT_ARTIST, gson.toJson(artist));
            intent.putExtra(ArtistActivity.CURRENT_COLOR, color);
            intent.putExtra(ArtistActivity.CURRENT_COLOR_DARK, colorDark);

            startActivity(intent);
        }
    }

    @Override
    public void onArtistWebSiteClick(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onArtistFavourite(Artist artist, Boolean isAdded) {
        StorageHelper.getInstance().setArtistsFavourite(artist, isAdded);
    }

    @Override
    public void onRefresh() {
        // избранные артисты хранятся исключительно во внутреннем хранилище
        String json = StorageHelper.getInstance().getArtistsFavouriteJson();
        Type type = new TypeToken<List<Artist>>() {}.getType();
        List<Artist> artists = gson.fromJson(json, type);
        if (artists != null && artists.size() != 0) { // проверяем на наличие
            mAdapter.setListItems(artists);
        } else {
            mAdapter.clearListItem(); // очищаем адаптер
        }
        mAdapter.notifyDataSetChanged();

        // управляем выводом сообщения о наличии избранных артистов
        if (mAdapter.getItemCount() != 0) {
            textViewEmpty.setVisibility(View.GONE);
        } else {
            textViewEmpty.setText(getString(R.string.label_empty_favourite));
            textViewEmpty.setVisibility(View.VISIBLE);
        }
        swipeLayout.setRefreshing(false);
    }
}
