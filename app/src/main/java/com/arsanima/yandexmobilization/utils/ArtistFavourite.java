package com.arsanima.yandexmobilization.utils;

import com.arsanima.yandexmobilization.models.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuriF on 20.04.16.
 *
 * Статичная коллекция любимых артистов
 */
public class ArtistFavourite {
    private static List<Artist> favourites = new ArrayList<>();

    public static void init(List<Artist> favourites) {
        if (favourites != null) {
            ArtistFavourite.favourites = favourites;
        }
    }

    public static void add(Artist artist) {
        if (!ArtistFavourite.isFavourite(artist)) {
            favourites.add(artist);
        }
    }

    public static void remove(Artist artist) {
        if (ArtistFavourite.isFavourite(artist)) {
            favourites.remove(artist);
        }
    }

    public static List<Artist> get() {
        return favourites;
    }

    public static Boolean isFavourite(Artist artist) {
        return favourites.contains(artist);
    }
}
