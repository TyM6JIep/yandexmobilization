package com.arsanima.yandexmobilization.models;

import com.arsanima.yandexmobilization.http.HttpClient;
import com.arsanima.yandexmobilization.http.ResponseCallback;

import java.util.List;

/**
 * Created by YuriF on 16.04.16.
 *
 * Модель для артиста
 */
public class Artist {

    private int id;
    private String name;
    private String[] genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private Cover cover;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }

    @Override
    public boolean equals(Object o) {
       return this.id == ((Artist) o).getId();
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + tracks;
        result = 31 * result + albums;
        result = 31 * result + description.hashCode();
        return result;
    }

    public static void load(final ResponseCallback<List<Artist>> responseCallback) {
        HttpClient.get(responseCallback);
    }
}
