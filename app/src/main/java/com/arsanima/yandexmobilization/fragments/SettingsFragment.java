package com.arsanima.yandexmobilization.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.arsanima.yandexmobilization.MainActivity;
import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.base.BaseActivity;
import com.arsanima.yandexmobilization.helpers.StorageHelper;
import com.arsanima.yandexmobilization.utils.ConstantDictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "settingsfragment";
    private ImageView mUserImage;
    private EditText mUserName;

    private Button btnSave;
    private String mImagePath;
    private MainActivity mListener;

    public void setListener(MainActivity listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mUserImage = (ImageView) view.findViewById(R.id.userImage);
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });

        mUserName = (EditText) view.findViewById(R.id.userName);

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageHelper.getInstance().setUserImagePath(mImagePath);
                StorageHelper.getInstance().setUserName(mUserName.getText().toString());

                if (mListener != null) {
                    mListener.updateProfile(); // обновляем профиль в "сендвич"-панели
                }
            }
        });

        StorageHelper.getInstance().updateProfile(mUserImage, mUserName);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bitmap photoBitmap = null;
            switch(requestCode) {
                case ConstantDictionary.ARC_ITEM_IMAGE_CAMERA: { // забираем картинку либо с камеры...
                    photoBitmap = (Bitmap) data.getExtras().get("data");
                } break;
                case ConstantDictionary.ARC_ITEM_IMAGE_GALLERY: {// ... либо с галереи
                    Uri uri = data.getData();
                    try {
                        photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        ((BaseActivity) getActivity()).showError(e.getLocalizedMessage());
                        return;
                    }
                } break;
            }
            if (photoBitmap != null) {
                mImagePath = getTmpFilePath(photoBitmap);
                mUserImage.setImageBitmap(photoBitmap);
            } else {
                ((BaseActivity) getActivity()).showError(getString(R.string.error_empty_bitmap)); // если вдруг что-то пошло не так
            }
        }
    }

    // получаем путь до временного файла
    private String getTmpFilePath(Bitmap data) {
        Context ctx = getActivity();
        File outputDir = null;
        if (ctx != null) {
            outputDir = ctx.getExternalCacheDir();
            if (outputDir == null || !outputDir.canWrite())
                outputDir = ctx.getCacheDir();
        }
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("tmpImg", String.format(".%s", "png"), outputDir);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            data.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return tmpFile.getPath();
        } catch (IOException ignored) {}
        return "";
    }

    // показываем меню для выбора источника пользовательского изображения
    public void loadImage() {
        final CharSequence[] items = {
                getString(R.string.label_input_activity_item_photo_camera),
                getString(R.string.label_input_activity_item_photo_galery),
                getString(R.string.label_input_activity_item_photo_cancel)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_input_activity_item_photo_choose));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, ConstantDictionary.ARC_ITEM_IMAGE_CAMERA);
                    } break;
                    case 1: {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_input_activity_item_photo_choose)), ConstantDictionary.ARC_ITEM_IMAGE_GALLERY);
                    } break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
