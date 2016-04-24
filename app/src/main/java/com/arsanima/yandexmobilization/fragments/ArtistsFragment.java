package com.arsanima.yandexmobilization.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.arsanima.yandexmobilization.MainActivity;
import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.adapters.ArtistsAdapter;
import com.arsanima.yandexmobilization.helpers.StorageHelper;
import com.arsanima.yandexmobilization.http.ResponseCallback;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.utils.ConstantDictionary;
import com.arsanima.yandexmobilization.views.ArtistView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;


public class ArtistsFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        ArtistView.ArtistViewListener {

    public static final String TAG = "artistsfragment";

    private RecyclerView mRecyclerView;
    private ArtistsAdapter mAdapter;
    private SwipeRefreshLayout swipeLayout;
    private ResponseCallback<List<Artist>> callback;
    private TextView mLastUpdateTime;
    private SimpleDateFormat simpleDateFormat;
    private Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat  = new SimpleDateFormat("d MMMM yyyy, HH:mm", getResources().getConfiguration().locale);
        callback = new ResponseCallback<List<Artist>>() {
            @Override
            public void onSuccess(List<Artist> artists) {

                // получение списка артистов и добавление его в текущий адаптер
                mAdapter.addItems(artists);
                mAdapter.notifyDataSetChanged();

                // запоминаем данные, пришедшие с сервера + запоминаем последнюю дату обновления
                StorageHelper.getInstance().setArtists(gson.toJson(artists));
                StorageHelper.getInstance().setLastUpdateTime();

                // вывод даты последнего обновления
                checkLastUpdate();

                // прячем лоадер
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int statusCode, String error) {
                swipeLayout.setRefreshing(false);
                ((MainActivity)getActivity()).showError(error); // отображение ошибок
            }

            @Override
            public void onError(int statusCode, JSONObject errorResponse) {
                swipeLayout.setRefreshing(false);
                ((MainActivity)getActivity()).showError(errorResponse); // отображение ошибок
            }

            @Override
            public void onConnectionError() {
                swipeLayout.setRefreshing(false);
                ((MainActivity)getActivity()).showConnectionError(); // отображение ошибки отсутствия интернета
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ArtistsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (!StorageHelper.getInstance().isLoadData()) { // если данных во внутреннем хранилище нет - запрашиваем с сервера
            onRefresh();
        } else {
            String json = StorageHelper.getInstance().getArtistsJson(); // достаем данные из хранилища и парсим в список артистов
            Type type = new TypeToken<List<Artist>>() {}.getType();
            List<Artist> artists = gson.fromJson(json, type);
            mAdapter.addItems(artists);
            mAdapter.notifyDataSetChanged();
            checkLastUpdate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeLayout.setOnRefreshListener(this);

        mLastUpdateTime = (TextView) view.findViewById(R.id.lastUpdateTime);

        return view;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Artist.load(callback);
            }
        }, ConstantDictionary.DELAY_RUNNABLE);
    }

    private void checkLastUpdate() {
        long date = StorageHelper.getInstance().getLastUpdateTime();
        if (date != 0) {
            String title = String.format(getResources().getString(R.string.label_lastUpdate), simpleDateFormat.format(date));
            mLastUpdateTime.setText(title);
            mLastUpdateTime.setVisibility(View.VISIBLE);
        } else {
            mLastUpdateTime.setVisibility(View.GONE);
        }
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
}
