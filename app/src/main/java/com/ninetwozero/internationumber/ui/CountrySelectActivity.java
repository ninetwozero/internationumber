package com.ninetwozero.internationumber.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.decorator.DividerItemDecoration;
import android.telephony.TelephonyManager;
import android.util.Property;
import android.view.View;
import android.widget.LinearLayout;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.database.dao.Country;
import com.ninetwozero.internationumber.ui.firstrun.CountryListAdapter;
import com.ninetwozero.internationumber.utils.DensityUtils;
import com.ninetwozero.internationumber.utils.Keys;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.Query;

public class CountrySelectActivity extends BaseActivity {
    public static final String SELECTED_LOCALE = "selectedLocale";

    private CursorList<Country> countries;
    private View topContainer;
    private View instructionsTextView;

    private CountryListAdapter adapter;
    private RecyclerView countryListView;
    private View nextButton;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_run);
        initialize();
        Query.all(Country.class).getAsync(getSupportLoaderManager(), new ManyQuery.ResultHandler<Country>() {

            @Override
            public boolean handleResult(final CursorList<Country> c) {
                if (c == null) {
                    return false;
                }
                countries = c;
                adapter.setItems(countries);

                selectAndScrollToUserCountry(getPossibleUserCountryLocale());
                animateLayout();

                return false;
            }
        });
    }

    /* FIXME: Make this work consistently - why is RecyclerView stalling and not scrolling? */
    private void selectAndScrollToUserCountry(final String locale) {
        final int userCountryPosition = adapter.getPosition(locale);
        adapter.setItemChecked(userCountryPosition, true);
        countryListView.scrollToPosition(userCountryPosition);
}

    private void initialize() {
        topContainer = findViewById(R.id.wrap_top);
        instructionsTextView = findViewById(R.id.instructions_select);
        adapter = new CountryListAdapter(countries);

        countryListView = (RecyclerView) findViewById(R.id.country_list);
        countryListView.setLayoutManager(new LinearLayoutManager(this));
        countryListView.setHasFixedSize(true);
        countryListView.setItemAnimator(new DefaultItemAnimator());
        countryListView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        countryListView.setAdapter(adapter);

        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onCountrySelected();
            }

            private void onCountrySelected() {
                final String locale = adapter.getItem(adapter.getCheckedItemPosition()).getLocale();
                preferences.edit().putString(Keys.USER_LOCALE, locale).apply();

                if (getCallingActivity() == null) {
                    startActivity(new Intent(CountrySelectActivity.this, MainActivity.class));
                } else {
                    final Intent data = new Intent();
                    data.putExtra(CountrySelectActivity.SELECTED_LOCALE, locale);
                    setResult(Activity.RESULT_OK, data);
                }
                finish();
            }
        });
    }

    private void animateLayout() {
        ObjectAnimator slideUpWrapper = ObjectAnimator.ofInt(topContainer, new LayoutHeightProperty(Integer.class, "viewLayoutHeight"), topContainer.getHeight(), DensityUtils.toPixels(150)).setDuration(800);
        ObjectAnimator fadeInInstructions = ObjectAnimator.ofFloat(instructionsTextView, "alpha", 0f, 0.8f).setDuration(400);

        ObjectAnimator slideUpButton = ObjectAnimator.ofFloat(nextButton, "translationY", DensityUtils.toPixels(100f), 0).setDuration(800);
        ObjectAnimator fadeInButton = ObjectAnimator.ofFloat(nextButton, "alpha", 0f, 1f).setDuration(0);

        // TODO: nextButton should be View.GONE until the kicks off.
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(slideUpWrapper);
        animatorSet.play(fadeInInstructions).after(slideUpWrapper);
        animatorSet.play(slideUpButton).with(fadeInButton).after(fadeInInstructions);
        animatorSet.setStartDelay(800);
        animatorSet.start();
    }

    private String getPossibleUserCountryLocale() {
        return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso();
    }

    @Override
    protected void onDestroy() {
        countries.close();
        super.onDestroy();
    }

    private class LayoutHeightProperty extends Property<View, Integer> {
        public LayoutHeightProperty(final Class<Integer> type, final String name) {
            super(type, name);
        }

        @Override
        public void set(final View object, final Integer value) {
            object.getLayoutParams().height = value;
            object.requestLayout();
        }

        @Override
        public Integer get(final View object) {
            return object.getLayoutParams().height;
        }
    }
}
