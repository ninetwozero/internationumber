package com.ninetwozero.internationumber.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.decorator.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.database.dao.Country;
import com.ninetwozero.internationumber.utils.BusProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {
    private static final int LOADER_ID = 0;

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ContactCursorListAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle state) {
        final View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onCountryLoadedFromDatabase(final Country country) {
        adapter.setCountry(country);
    }

    private void initialize(final View view) {
        adapter = getAdapter(null);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        getLoaderManager().restartLoader(
            LOADER_ID,
            new Bundle(),
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    // http://developer.android.com/reference/android/provider/ContactsContract.Contacts.html
                    // http://developer.android.com/reference/android/provider/ContactsContract.Contacts.Photo.html
                    return new CursorLoader(
                        getActivity(),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {
                            ContactsContract.RawContacts._ID,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI
                            // ^-- cursor.getBlob(cursor.getIndexForNameShit(^^^^^^^^^))
                        },
                        "",
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                    );
                }

                @Override
                public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {
                    Log.d("YOLO", "adapter.size() => " + adapter.getItemCount());
                    adapter.setCursor(o);
                    Log.d("YOLO", "o.getCount() => " + o.getCount());
                }

                @Override
                public void onLoaderReset(Loader<Cursor> objectLoader) {

                }
            });
    }

    private ContactCursorListAdapter getAdapter(final Cursor cursor) {
        if (adapter == null) {
            return new ContactCursorListAdapter(cursor);
        } else {
            return adapter;
        }
    }

    /*private List<Contact> generateDummyContactsForList() {
        final List<Contact> contacts = new ArrayList<Contact>();
        contacts.add(new Contact("Alfred Pennyworth", "0701234567"));
        contacts.add(new Contact("Batman", "911"));
        contacts.add(new Contact("Bruce Wayne", "0701234568"));
        contacts.add(new Contact("Donatello", "09010001"));
        contacts.add(new Contact("Leonardo", "09010002"));
        contacts.add(new Contact("Master Splinter", "09010000"));
        contacts.add(new Contact("Michelangelo", "09010003"));
        contacts.add(new Contact("Rafael", "09010004"));
        contacts.add(new Contact("The Batcave", "090123456"));
        return contacts;
    }*/
}
