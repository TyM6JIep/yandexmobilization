package com.arsanima.yandexmobilization.http;

import android.os.Looper;

import com.arsanima.yandexmobilization.helpers.NetworkHelper;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.utils.ConstantDictionary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by YuriF on 16.04.16.
 */
public class HttpClient {
    public static AsyncHttpClient syncHttpClient= new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    private static String url = "http://cache-default03d.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json";

    private static AsyncHttpClient getClient() {
        AsyncHttpClient client = asyncHttpClient;
        if (Looper.myLooper() == null) {
            client = syncHttpClient;
        }
        client.setTimeout(10*1000*10);
        return client;
    }

    public static <T> void get(final ResponseCallback<T> callback) {
        if (!NetworkHelper.getInstance().isNetworkConnect()) {
            callback.onConnectionError();
        }
        getClient().get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (statusCode == 200) {
                    Type type = new TypeToken<List<Artist>>() {}.getType();
                    T resp = (new Gson()).fromJson(response.toString(), type);
                    callback.onSuccess(resp);
                } else {
                    callback.onError(statusCode, ConstantDictionary.SPECIFIC_ERROR);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Type type = new TypeToken<List<Artist>>() {}.getType();
                    T resp = (new Gson()).fromJson(response.toString(), type);
                    callback.onSuccess(resp);
                } else {
                    callback.onError(statusCode, ConstantDictionary.SPECIFIC_ERROR);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.onError(statusCode, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
