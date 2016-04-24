package com.arsanima.yandexmobilization;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arsanima.yandexmobilization.base.BaseActivity;
import com.arsanima.yandexmobilization.fragments.ArtistsFragment;
import com.arsanima.yandexmobilization.fragments.FavouritesFragment;
import com.arsanima.yandexmobilization.fragments.FinderFragment;
import com.arsanima.yandexmobilization.fragments.SettingsFragment;
import com.arsanima.yandexmobilization.helpers.StorageHelper;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Integer CURRENT_FRAGMENT = 0;
    private static String CURRENT_FRAGMENT_TAG = "";

    private ArtistsFragment artistsFragment;
    private FavouritesFragment favouritesFragment;
    private FinderFragment finderFragment;
    private SettingsFragment settingsFragment;
    private FragmentManager fragmentManager;
    private ImageView mUserImage;
    private TextView mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_artists);
        setActiveFragment(R.id.nav_artists);

        mUserImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userImage);
        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        StorageHelper.getInstance().updateProfile(mUserImage, mUserName); // подтягиваем данные для профиля
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setActiveFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    // для моделирования поведения приложения инстаграм - при переходе между фрагментами, текущий активный фрагмент скрывается, а потом отображается, таким образом
    // не скидывается положения листов, контролов
    public void setActiveFragment(int id) {
        if(CURRENT_FRAGMENT.equals(id)){
            return;
        }
        String tag = "";
        String title = "";
        Fragment fragment;
        switch (id) {
            case R.id.nav_artists: {
                if (artistsFragment == null) {
                    artistsFragment = new ArtistsFragment();
                }
                tag = ArtistsFragment.TAG;
                fragment = artistsFragment;
                title = "Исполнители";
                break;
            }
            case R.id.nav_search: {
                if (finderFragment == null) {
                    finderFragment = new FinderFragment();
                }
                tag = FinderFragment.TAG;
                fragment = finderFragment;
                title = "Поиск исполнителей";
                break;
            }
            case R.id.nav_favourite: {
                if (favouritesFragment == null) {
                    favouritesFragment = new FavouritesFragment();
                }
                tag = FavouritesFragment.TAG;
                fragment = favouritesFragment;
                title = "Избранное";
                break;
            }
            case R.id.nav_settings: {
                if (settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    settingsFragment.setListener(MainActivity.this);
                }
                tag = SettingsFragment.TAG;
                fragment = settingsFragment;
                title = "Настройки";
                break;
            }
            default:
                if (artistsFragment == null) {
                    artistsFragment = new ArtistsFragment();
                }
                tag = ArtistsFragment.TAG;
                fragment = artistsFragment;
                title = "Исполнители";
                break;
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFrg = fragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
        if(currentFrg != null) {
            fragmentTransaction.hide(currentFrg);
        }
        CURRENT_FRAGMENT = id;
        CURRENT_FRAGMENT_TAG = tag;

        if(fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.frameRoot, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();

        setTitle(title);
    }

    public void updateProfile() {
        StorageHelper.getInstance().updateProfile(mUserImage, mUserName);
        Toast.makeText(this, R.string.string_success, Toast.LENGTH_SHORT).show();
    }
}
