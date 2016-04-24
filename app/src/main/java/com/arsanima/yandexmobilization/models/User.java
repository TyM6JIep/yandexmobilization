package com.arsanima.yandexmobilization.models;

/**
 * Created by YuriF on 16.04.16.
 *
 * Модель для пользователя - имя + аватарка
 */
public class User {
    private String name;
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
