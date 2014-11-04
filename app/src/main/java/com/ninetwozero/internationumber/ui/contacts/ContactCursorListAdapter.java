package com.ninetwozero.internationumber.ui.contacts;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.database.dao.Country;
import com.ninetwozero.internationumber.utils.NumberConverter;

import java.util.List;

public class ContactCursorListAdapter extends RecyclerView.Adapter<ContactCursorListAdapter.ViewHolder> {
    private Cursor cursor;
    private Country country;
    private NumberConverter converter;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView contactName;
        public final TextView contactNumber;
        public ImageView contactImage;

        public ViewHolder(View v) {
            super(v);
            contactImage = (ImageView) v.findViewById(R.id.contact_image);
            contactName = (TextView) v.findViewById(R.id.contact_name);
            contactNumber = (TextView) v.findViewById(R.id.contact_number);
        }
    }

    public ContactCursorListAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ContactCursorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        final ViewHolder viewholder = new ViewHolder(inflatedView);
        inflatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewholder.getPosition();
                Toast.makeText(v.getContext(), "Clicked on unknown!", Toast.LENGTH_SHORT).show();
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        final Contact contact = createContactObjectFromCursor();
        holder.contactName.setText(contact.getName());
        if (country == null) {
            holder.contactNumber.setText(contact.getNumber());
        } else {
            holder.contactNumber.setText(converter.convert(contact.getNumber()));
        }

        if (contact.getPathToImage() == null) {
            holder.contactImage.setImageResource(R.drawable.default_contact_image);
        } else {
            holder.contactImage.setImageURI(contact.getPathToImage());
        }
    }

    private Contact createContactObjectFromCursor() {
        final String thumbnailUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI));
        final Uri uri = thumbnailUri == null ? null : Uri.parse(thumbnailUri);
        return new Contact(
            cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID)),
            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
            uri
        );
    }

    @Override
    public int getItemCount() {
        return cursor == null || cursor.isClosed() ? 0 : cursor.getCount();
    }

    public void setCountry(final Country country) {
        this.country = country;
        this.converter = new NumberConverter(country);
        this.notifyDataSetChanged();
    }

    public void setCursor(final Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
