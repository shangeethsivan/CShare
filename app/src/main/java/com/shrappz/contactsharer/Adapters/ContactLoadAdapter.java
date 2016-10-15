package com.shrappz.contactsharer.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shrappz.contactsharer.Models.Item;
import com.shrappz.contactsharer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 08-10-2016.
 */

public class ContactLoadAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    Context mcontext;
    ArrayList<Item> mainobject;
    List<Item> filteredobjects;

    public ContactLoadAdapter(Context context, int resource, ArrayList<Item> objects) {
        this.filteredobjects = objects;
        inflater = LayoutInflater.from(context);
        mainobject = new ArrayList<>();
        mainobject.addAll(objects);
    }

    class Holder {
        TextView name, number;
        ImageView profpic;
        ImageView send;
    }

    @Override
    public int getCount() {
        return filteredobjects.size();
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return filteredobjects.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_contacts, null);
        }
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.number = (TextView) view.findViewById(R.id.number);
        holder.name.setText(filteredobjects.get(position).getName());
        holder.number.setText(filteredobjects.get(position).getNumber());
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredobjects.clear();
        Log.d(mainobject.size() + "", "filter: ");
        if (charText.length() == 0) {
            filteredobjects.addAll(mainobject);
        } else {
            for (Item wp : mainobject) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredobjects.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
