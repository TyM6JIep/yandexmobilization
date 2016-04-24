package com.arsanima.yandexmobilization.models;

/**
 * Created by YuriF on 16.04.16.
 *
 * Модель для обложки
 */
public class Cover {
    private String small;
    private String big;

    public String getUrlForSmallCover() {
        return small;
    }

    public String getUrlForBigCover() {
        return big;
    }
}
