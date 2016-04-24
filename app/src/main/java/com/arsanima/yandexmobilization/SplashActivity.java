package com.arsanima.yandexmobilization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.arsanima.yandexmobilization.base.BaseActivity;
import com.arsanima.yandexmobilization.helpers.StorageHelper;
import com.arsanima.yandexmobilization.http.ResponseCallback;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.utils.ArtistFavourite;
import com.arsanima.yandexmobilization.utils.ConstantDictionary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // проверяем наличие данных в хранилище
        if (!StorageHelper.getInstance().isLoadData()) {
            Artist.load(new ResponseCallback<List<Artist>>() {
                @Override
                public void onSuccess(List<Artist> response) {
                    StorageHelper.getInstance().setArtists(gson.toJson(response));
                    StorageHelper.getInstance().setLastUpdateTime();

                    startApp();
                }

                @Override
                public void onError(int statusCode, String error) {
                    showError(error);
                }

                @Override
                public void onError(int statusCode, JSONObject errorResponse) {
                    showError(errorResponse);
                }

                @Override
                public void onConnectionError() {
                    showConnectionError();
                }
            });
        } else {
            startApp();
        }
    }

    // запускаем главное активити...
    public void startApp() {
        Type type = new TypeToken<List<Artist>>() {}.getType();
        List<Artist> favouriteArtists = gson.fromJson(StorageHelper.getInstance().getArtistsFavouriteJson(), type);
        ArtistFavourite.init(favouriteArtists);

        // вытаскиваем и запоминаем уникальную коллекцию жанров из списка артистов
        List<Artist> artists = gson.fromJson(StorageHelper.getInstance().getArtistsJson(), type);
        List<String> genresList = new ArrayList<>();
        for (Artist artist : artists) {
            String[] genres = artist.getGenres();
            for(String gener : genres) {
                if (!genresList.contains(gener.trim())) {
                    genresList.add(gener.trim());
                }
            }
        }
        if (genresList.size() != 0) {
            StorageHelper.getInstance().setGenres(gson.toJson(genresList));
        }
        genresList.clear();
        artists.clear();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, ConstantDictionary.DELAY_RUNNABLE);
    }
}
