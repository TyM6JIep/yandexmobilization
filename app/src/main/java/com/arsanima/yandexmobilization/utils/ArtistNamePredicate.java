package com.arsanima.yandexmobilization.utils;

import com.arsanima.yandexmobilization.models.Artist;

import org.apache.commons.collections4.Predicate;

/**
 * Created by YuriF on 25.04.16.
 *
 * Предикат на поиск по жанру
 *
 */
public class ArtistNamePredicate implements Predicate {
    private String name;

    public ArtistNamePredicate(String name) {
        super();
        this.name = name;
    }

    @Override
    public boolean evaluate(Object o) {
        return ((Artist) o).getName().toLowerCase().contains(name.toLowerCase());
    }
}
