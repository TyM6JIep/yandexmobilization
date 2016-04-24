package com.arsanima.yandexmobilization.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.arsanima.yandexmobilization.ArtistActivity;
import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.adapters.ArtistsAdapter;
import com.arsanima.yandexmobilization.helpers.StorageHelper;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.utils.ArtistGenresPredicate;
import com.arsanima.yandexmobilization.utils.ArtistNamePredicate;
import com.arsanima.yandexmobilization.views.ArtistView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinderFragment extends Fragment implements
        ArtistView.ArtistViewListener {

    private final int SEARCH_TYPE_NAME = 0;
    private final int SEARCH_TYPE_GENRE = 1;

    private RadioGroup searchType;
    private EditText artistName;
    private Spinner artistGenres;
    private Gson gson = new Gson();
    private int searchCurrentType = 0;
    private Button bntClear, btnSearch;
    private RecyclerView mRecyclerView;
    private ArtistsAdapter mAdapter;
    private List<Artist> artists;
    private ArrayAdapter adapter;
    private TextView emptySearch;

    public static final String TAG = "finderfragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finder, container, false);

        searchType = (RadioGroup) view.findViewById(R.id.searchType);
        searchType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonName: {
                        artistName.setVisibility(View.VISIBLE);
                        artistGenres.setVisibility(View.GONE);
                        searchCurrentType = SEARCH_TYPE_NAME;
                    } break;
                    case R.id.radioButtonGenres: {
                        artistName.setVisibility(View.GONE);
                        artistGenres.setVisibility(View.VISIBLE);
                        searchCurrentType = SEARCH_TYPE_GENRE;
                    } break;
                }
            }
        });

        artistName = (EditText) view.findViewById(R.id.searchName);
        artistGenres = (Spinner) view.findViewById(R.id.searchGenres);


        // достаем уникальную коллекцию жанров
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> genres = gson.fromJson(StorageHelper.getInstance().getGenres(), type);

        // создаем адаптер с жанрами для выпадающего списка
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        artistGenres.setAdapter(adapter);

        bntClear = (Button) view.findViewById(R.id.btnClear);
        bntClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchCurrentType == SEARCH_TYPE_NAME) {
                    artistName.setText("");
                } else {
                    artistGenres.setSelected(false);
                }

                mAdapter.setListItems(artists); // возращаем дефолтную коллекцию артистов
                mAdapter.notifyDataSetChanged();

                emptySearch.setVisibility(View.GONE); // прячем сообщение о пустом результате поиска
            }
        });

        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchCurrentType == SEARCH_TYPE_NAME) {
                    String search = artistName.getText().toString();
                    if (search.length() != 0) {
                        Collection<Artist> smallList = CollectionUtils.select(artists, new ArtistNamePredicate(search)); // ищем совпадения по имени и выводим в список
                        mAdapter.setListItems((List<Artist>) smallList);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (artistGenres.getSelectedItem() != null) {
                        String search = artistGenres.getSelectedItem().toString();
                        Collection<Artist> smallList = CollectionUtils.select(artists, new ArtistGenresPredicate(search));// ищем совпадения по жанрам и выводим в список
                        mAdapter.setListItems((List<Artist>) smallList);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                // управляем показом сообщения о пустом результате поиска
                if (mAdapter.getItemCount() == 0) {
                    emptySearch.setVisibility(View.VISIBLE);
                } else {
                    emptySearch.setVisibility(View.GONE);
                }
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        emptySearch = (TextView) view.findViewById(R.id.emptySearch);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ArtistsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // создаем дефолтную коллекцию артистов
        String json = StorageHelper.getInstance().getArtistsJson();
        Type type = new TypeToken<List<Artist>>() {}.getType();
        artists = gson.fromJson(json, type);
        mAdapter.addItems(artists);
        mAdapter.notifyDataSetChanged();
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
