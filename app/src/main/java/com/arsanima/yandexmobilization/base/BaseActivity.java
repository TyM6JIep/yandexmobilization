package com.arsanima.yandexmobilization.base;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.dialogs.Dialogs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YuriF on 16.04.16.
 *
 * Базовая активити для показа ошибок и для обработки отсутствия подключения интернета
 */
public class BaseActivity extends AppCompatActivity {

    protected AlertDialog mErrorDialog, mLoaderDialog;

    public BaseActivity() {}


    public void showError(JSONObject errorResponse) {
        String msg = getString(R.string.string_ufo_error);
        try {
            msg = errorResponse.get("error").toString(); //ToDo
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showErrorDialog(msg);
    }

    public void showError(String error) {
        String msg = getString(R.string.string_ufo_error);
        if (error.length() != 0) {
            msg = error;
        }
        showErrorDialog(msg);
    }

    private void showErrorDialog(String errorMessage) {
        checkDialogs();
        mErrorDialog = Dialogs.createErrorDialog(this, errorMessage);
        mErrorDialog.show();
    }

    public void showConnectionError() {
        showError(getString(R.string.string_connection_error));
    }

    protected void checkDialogs() {
        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
        if (mLoaderDialog != null) {
            mLoaderDialog.dismiss();
            mLoaderDialog = null;
        }
    }
}
