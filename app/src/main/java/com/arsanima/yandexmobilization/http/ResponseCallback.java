package com.arsanima.yandexmobilization.http;

import org.json.JSONObject;

/**
 * Created by YuriF on 16.04.16.
 */
public interface ResponseCallback<T> {
    void onSuccess(T response); // событие, если запрос обработан успешно и вернулся ответ
    void onError(int statusCode, String error); // событие, если произошла ошибка
    void onError(int statusCode, JSONObject errorResponse); // событие, если произошла ошибка
    void onConnectionError(); // возвращаем отдельным событием отсуствие интернета
}
