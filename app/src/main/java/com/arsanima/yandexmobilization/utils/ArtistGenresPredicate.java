package com.arsanima.yandexmobilization.utils;

import com.arsanima.yandexmobilization.models.Artist;

import org.apache.commons.collections4.Predicate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by YuriF on 25.04.16.
 *
 * Предикат на поиск по имени
 */
public class ArtistGenresPredicate implements Predicate {
    private String genre;

    public ArtistGenresPredicate(String genre) {
        super();
        this.genre = genre;
    }

    @Override
    public boolean evaluate(Object o) {
        String[] genres = ((Artist) o).getGenres();
        List<String> genreList = Arrays.asList(genres);

        return genreList.contains(genre);
    }
}
