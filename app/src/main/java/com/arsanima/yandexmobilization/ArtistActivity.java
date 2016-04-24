package com.arsanima.yandexmobilization;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.arsanima.yandexmobilization.models.Artist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

// активити для отображения дополнительной информации

public class ArtistActivity extends AppCompatActivity {

    public static final String CURRENT_ARTIST = "current_artist";
    public static final String CURRENT_COLOR = "current_color";
    public static final String CURRENT_COLOR_DARK = "current_color_dark";

    private ImageView artistCover;
    private TextView artistGenres, artistStat, artistInfo;
    private int mColor = 0, mColorDark = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        artistCover = (ImageView) findViewById(R.id.artistCover);

        artistGenres = (TextView) findViewById(R.id.artistGenres);
        artistStat = (TextView) findViewById(R.id.artistStat);
        artistInfo = (TextView) findViewById(R.id.artistInfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        if (b != null && b.getString(CURRENT_ARTIST) != null) {
            Type type = new TypeToken<Artist>() {}.getType();
            Artist artist = (new Gson()).fromJson(b.getString(CURRENT_ARTIST), type);
            setTitle(artist.getName());
            artistGenres.setText(TextUtils.join(", ", artist.getGenres()));
            artistStat.setText(String.format(getString(R.string.label_albums), String.valueOf(artist.getAlbums()), String.valueOf(artist.getTracks())));
            artistInfo.setText(artist.getDescription());

            Picasso.with(this).load(artist.getCover().getUrlForBigCover()).error(R.drawable.ic_user_not_found).placeholder(R.drawable.ic_user_not_found).into(artistCover);

            mColor = b.getInt(CURRENT_COLOR, 0);
            mColorDark = b.getInt(CURRENT_COLOR_DARK, 0);

            ActionBar ab = getSupportActionBar();
            if (ab != null){
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setBackgroundDrawable(new ColorDrawable(mColor));
            }

            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.setStatusBarColor(mColorDark);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return false;
    }
}
