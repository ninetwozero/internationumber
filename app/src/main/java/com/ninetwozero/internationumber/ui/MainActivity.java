package com.ninetwozero.internationumber.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.database.dao.Country;
import com.ninetwozero.internationumber.utils.BusProvider;
import com.ninetwozero.internationumber.utils.Keys;

import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;


public class MainActivity extends BaseActivity {
    private static final int REQUEST_COUNTRY = 0;
    private String selectedLocale;
    private Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromSharedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_country:
                startActivityForResult(new Intent(this, CountrySelectActivity.class), REQUEST_COUNTRY);
                return true;
            case R.id.action_backups:
            case R.id.action_settings:
            default:
                Toast.makeText(this, "Clicked on " + getResources().getResourceName(item.getItemId()), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_COUNTRY && resultCode == Activity.RESULT_OK) {
            final String locale = data.getStringExtra(CountrySelectActivity.SELECTED_LOCALE);
            showToast("Selected locale: " + locale);
        }
    }

    private void initialize() {
        loadDataFromSharedPreferences();
        if (hasUserSelectedACountry()) {
            loadCountryFromDatabase();
        } else {
            sendBackToCountrySelection();
        }
    }

    private void loadCountryFromDatabase() {
        Query.one(
            Country.class,
            "SELECT * " + "FROM " + Country.TABLE_NAME + " " + "WHERE locale = ?",
            new String[]{selectedLocale}
        ).getAsync(
            getSupportLoaderManager(),
            new OneQuery.ResultHandler<Country>() {
                @Override
                public boolean handleResult(final Country country) {
                    selectedCountry = country;
                    BusProvider.getInstance().post(country);
                    return false;
                }
            }
        );
    }

    private void loadDataFromSharedPreferences() {
        selectedLocale = preferences.getString(Keys.USER_LOCALE, null);
    }

    private boolean hasUserSelectedACountry() {
        if (selectedLocale == null) {
            return false;
        }
        return true;
    }

    private void sendBackToCountrySelection() {
        startActivity(new Intent(this, CountrySelectActivity.class));
        finish();
    }
}
