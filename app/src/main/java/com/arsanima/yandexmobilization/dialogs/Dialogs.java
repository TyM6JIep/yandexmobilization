package com.arsanima.yandexmobilization.dialogs;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.arsanima.yandexmobilization.R;

/**
 * Created by YuriF on 16.04.16.
 *
 * Некая фабрика для получения диалогов с текстом ошибок
 */
public class Dialogs {

    public static AlertDialog createErrorDialog(AppCompatActivity activity, String errorMessage) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_error, null);

        TextView textView = (TextView) view.findViewById(R.id.errorMessage);
        textView.setText(errorMessage);

        builder.setView(view);

        return builder.create();
    }
}
