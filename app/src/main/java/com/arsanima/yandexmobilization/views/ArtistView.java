package com.arsanima.yandexmobilization.views;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arsanima.yandexmobilization.R;
import com.arsanima.yandexmobilization.models.Artist;
import com.arsanima.yandexmobilization.utils.ArtistFavourite;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by YuriF on 19.04.16.
 */
public class ArtistView extends RecyclerView.ViewHolder {

    private ArtistViewListener mListener;
    private Artist mArtist;
    private TextView artistName, artistGener, artistAlbums;
    private ImageView artistImage;
    private LinearLayout layoutGeners;
    private Button mArtistWebSite;
    private CheckBox mArtistFavourite;
    private HashMap<String, Button> mButtons = new HashMap<>();

    public interface ArtistViewListener {
        public void onArtistClick(Artist artist, int color, int colorDark); // выносим событие клика по элементу списка с артистами
        public void onArtistWebSiteClick(String url); // переход на сайт исполнителя
        public void onArtistFavourite(Artist artist, Boolean isAdded); // добавляем/удаляем артиста в/из списка любимых
    }

    public ArtistView(View itemView, ArtistViewListener listener) {
        super(itemView);

        mListener = listener;

        artistName = (TextView) itemView.findViewById(R.id.artistName);
        artistGener = (TextView) itemView.findViewById(R.id.artistGener);
        artistAlbums = (TextView) itemView.findViewById(R.id.artistAlbums);

        artistImage = (ImageView) itemView.findViewById(R.id.artistImage);

        layoutGeners = (LinearLayout) itemView.findViewById(R.id.layoutGeners);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mListener != null) {

                    Bitmap bitmap = ((BitmapDrawable)artistImage.getDrawable()).getBitmap();
                    Palette palette = Palette.from(bitmap).generate();

                    int vibrant = palette.getVibrantColor(Color.GRAY);
                    mListener.onArtistClick(mArtist, vibrant, generateDarkColor(vibrant));
                }
            }
        });

        mArtistWebSite = (Button) itemView.findViewById(R.id.artistWeb);
        mArtistWebSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onArtistWebSiteClick(mArtist.getLink());
                }
            }
        });

        mArtistFavourite = (CheckBox) itemView.findViewById(R.id.artistFavourite);
        mArtistFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    mListener.onArtistFavourite(mArtist, isChecked);
                }
            }
        });
    }

    public void setArtist(Artist artist) {
        mArtist = artist;

        artistName.setText(mArtist.getName());
        artistGener.setText(TextUtils.join(", ", mArtist.getGenres()));
        artistAlbums.setText(String.format(artistAlbums.getContext().getString(R.string.label_albums), String.valueOf(mArtist.getAlbums()), String.valueOf(mArtist.getTracks())));

        Picasso.with(artistImage.getContext()).load(artist.getCover().getUrlForSmallCover()).error(R.drawable.ic_user_not_found).placeholder(R.drawable.ic_user_not_found).into(artistImage);

        mArtistFavourite.setChecked(ArtistFavourite.isFavourite(mArtist));
    }

    private int generateDarkColor(int color) {
        // грязный хак на получение темного цвета для тулбара)))
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
       return Color.HSVToColor(hsv);
    }
}
