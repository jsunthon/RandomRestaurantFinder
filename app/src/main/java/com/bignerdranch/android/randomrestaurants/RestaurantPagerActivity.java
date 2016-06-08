package com.bignerdranch.android.randomrestaurants;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bignerdranch.android.models.Restaurant;
import com.bignerdranch.android.models.RestaurantLab;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.UUID;

/**
 * Created by jsunthon on 6/7/2016.
 */
public class RestaurantPagerActivity extends AppCompatActivity {

    private final String LOG_TAG = RestaurantPagerActivity.class.getSimpleName();
    public static final String EXTRA_RESTAURANT_ID = "com.bignerdranch.android.randomrestaurants.resId";
    private ViewPager mViewPager;
    private List<Restaurant> mRestaurants;

    public static Intent newIntent(Context packageContext, UUID restraurantId) {
        Intent intent = new Intent(packageContext, RestaurantPagerActivity.class);
        intent.putExtra(EXTRA_RESTAURANT_ID, restraurantId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_pager);

        UUID restaurantId = (UUID) getIntent().getSerializableExtra(EXTRA_RESTAURANT_ID);
        mRestaurants = RestaurantLab.get(this).getRestaurants();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager = (ViewPager) findViewById(R.id.activity_restaurant_pager_view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Restaurant restaurant = mRestaurants.get(position);
                return RestaurantFragment.newInstance(restaurant.getId());
            }

            @Override
            public int getCount() {
                return mRestaurants.size();
            }
        });

        for (int i = 0; i < mRestaurants.size(); i++) {
            Restaurant restaurant = mRestaurants.get(i);
            if (restaurant.getId().equals(restaurantId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
