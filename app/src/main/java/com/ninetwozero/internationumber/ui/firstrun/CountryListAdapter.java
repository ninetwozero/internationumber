package com.ninetwozero.internationumber.ui.firstrun;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.database.dao.Country;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import se.emilsjolander.sprinkles.CursorList;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {
    private static final int NONE = -1;
    private int selectableItemBackground = NONE;

    private CursorList<Country> items;
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
    private int checkedItemPosition = NONE;

    public CountryListAdapter(final CursorList<Country> items) {
        this.items = items;

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setGroupingSeparator(' ');

        this.formatter.setDecimalFormatSymbols(formatSymbols);
        this.formatter.setPositivePrefix("+");
    }

    public Country getItem(final int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final TextView codeTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.country_name);
            codeTextView = (TextView) itemView.findViewById(R.id.country_code);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int position) {
        final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_country, parent, false);
        final ViewHolder viewholder = new ViewHolder(inflatedView);
        inflatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = viewholder.getPosition();
                setItemChecked(position, !isItemChecked(position));
            }
        });
        inflatedView.setClickable(true);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.nameTextView.setText(items.get(position).getName());
        viewHolder.codeTextView.setText(formatter.format(items.get(position).getCode()));
        formatRow(viewHolder, isItemChecked(position));
    }

    private void formatRow(final ViewHolder viewHolder, final boolean checked) {
        final View container = (View) viewHolder.nameTextView.getParent();
        final Resources resources = container.getContext().getResources();
        if (checked) {
            viewHolder.nameTextView.setTextColor(resources.getColor(android.R.color.white));
            viewHolder.codeTextView.setTextColor(resources.getColor(android.R.color.white));
            container.setBackgroundColor(resources.getColor(R.color.colorAccent));
        } else {
            if (selectableItemBackground == NONE) {
                selectableItemBackground = getBackgroundResource(container.getContext());
            }
            viewHolder.nameTextView.setTextColor(resources.getColor(R.color.primary_dark_material_dark));
            viewHolder.codeTextView.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            container.setBackgroundResource(selectableItemBackground);
        }
    }

    private int getBackgroundResource(final Context context) {
        TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground,
                typedValue,
                true
            );
        return typedValue.resourceId;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public boolean isItemChecked(final int position) {
        return checkedItemPosition == position;
    }

    public int getCheckedItemPosition() {
        return checkedItemPosition;
    }

    public void setItemChecked(final int position, final boolean check) {
        final int oldPosition = checkedItemPosition;
        checkedItemPosition = check ? position : NONE;

        if (oldPosition != NONE) {
            notifyItemChanged(oldPosition);
        }

        notifyItemChanged(checkedItemPosition);
    }

    public void setItems(final CursorList<Country> items) {
        this.items = items;
        notifyDataSetChanged();
   }

    public int getPosition(final String locale) {
        if (items == null) {
            return 0;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getLocale().equalsIgnoreCase(locale)) {
                return i;
            }
        }
        return 0;
    }
}
