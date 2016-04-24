package com.arsanima.yandexmobilization.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.utils.ArtistFavourite;
import com.arsanima.yandexmobilization.utils.ConstantDictionary;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by YuriF on 16.04.16.
 *
 * Класс для работы с внутренним хранилищем
 */
public class StorageHelper {

    private SharedPreferences mPrefs;
    private String mArtists = null;
    private String mGenres = null;
    private String mArtistFavourite = null;
    private String mUserImage = null;
    private String mUserName = null;
    private long mLastUpdateTime = 0;
    private Context mContext;

    private volatile static StorageHelper instance;

    public static StorageHelper getInstance() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized StorageHelper getSync() {
        if (instance == null) instance = new StorageHelper();
        return instance;
    }

    public void initialize(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(ConstantDictionary.APP_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setArtists(String json) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(ConstantDictionary.STORAGE_ARTIST, json);
        editor.apply();
        mArtists = json;
    }

    // возвращаем строку с артистами
    public String getArtistsJson() {
        if(mArtists == null) {
            mArtists = mPrefs.getString(ConstantDictionary.STORAGE_ARTIST, null);
        }
        return mArtists;
    }

    public void setLastUpdateTime() {
        mLastUpdateTime = System.currentTimeMillis();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(ConstantDictionary.STORAGE_LASTUPDATETIME, mLastUpdateTime);
        editor.apply();
    }

    // возвращаем время последнего обновления
    public Long getLastUpdateTime() {
        if(mLastUpdateTime == 0) {
            mLastUpdateTime = mPrefs.getLong(ConstantDictionary.STORAGE_LASTUPDATETIME, 0);
        }
        return mLastUpdateTime;
    }

    public void setArtistsFavourite(Artist artist, Boolean isAdded) {
        if (isAdded) {
            ArtistFavourite.add(artist);
        } else {
            ArtistFavourite.remove(artist);
        }
        String json = (new Gson()).toJson(ArtistFavourite.get());
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(ConstantDictionary.STORAGE_ARTISTFAVOURITE, json);
        editor.apply();
        mArtistFavourite = json;
    }

    // возвращаем список любимых артистов
    public String getArtistsFavouriteJson() {
        if(mArtistFavourite == null) {
            mArtistFavourite = mPrefs.getString(ConstantDictionary.STORAGE_ARTISTFAVOURITE, null);
        }
        return mArtistFavourite;
    }

    public void setUserImagePath(String path) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(ConstantDictionary.STORAGE_USERIMAGEPATH, path);
        editor.apply();
        mUserImage = path;
    }

    // возвращаем путь к файлу-аватарке
    public String getUserImagePath() {
        if(mUserImage == null) {
            mUserImage = mPrefs.getString(ConstantDictionary.STORAGE_USERIMAGEPATH, null);
        }
        return mUserImage;
    }

    public void setUserName(String name) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(ConstantDictionary.STORAGE_USERNAME, name);
        editor.apply();
        mUserName = name;
    }

    // возвращаем имя пользователя
    public String getUserName() {
        if(mUserName == null) {
            mUserName = mPrefs.getString(ConstantDictionary.STORAGE_USERNAME, null);
        }
        return mUserName;
    }

    public Boolean isLoadData() {
        return getArtistsJson() != null;
    }

    // обновляем профиль, вынес сюда, так как используется во многих местах
    public void updateProfile(ImageView imageView, TextView name) {
        if (getUserImagePath() != null && getUserImagePath().length() != 0) {
            File imgFile = new  File(getUserImagePath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
        }

        if (getUserName() != null && getUserName().length() != 0) {
            name.setText(getUserName());
        }
    }

    public void setGenres(String genres) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(ConstantDictionary.STORAGE_GENRES, genres);
        editor.apply();
        mGenres = genres;
    }

    // возвращаем уникальную коллекцию жанров
    public String getGenres() {
        if(mGenres == null) {
            mGenres = mPrefs.getString(ConstantDictionary.STORAGE_GENRES, null);
        }
        return mGenres;
    }
}
