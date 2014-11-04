package com.ninetwozero.internationumber.ui.contacts;

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

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private List<Contact> items;
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

    public ContactListAdapter(List<Contact> items) {
        this.items = items;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        final ViewHolder viewholder = new ViewHolder(inflatedView);
        inflatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewholder.getPosition();
                Toast.makeText(v.getContext(), "Clicked on " + items.get(position).getName() + "!", Toast.LENGTH_SHORT).show();
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.contactImage.setImageResource(R.drawable.default_contact_image);
        holder.contactName.setText(items.get(position).getName());
        if (country == null) {
            holder.contactNumber.setText(items.get(position).getNumber());
        } else {
            holder.contactNumber.setText(converter.convert(items.get(position).getNumber()));
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setCountry(final Country country) {
        this.country = country;
        this.converter = new NumberConverter(country);
        this.notifyDataSetChanged();
    }

}
