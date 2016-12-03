package com.youngje.tgwing.accommodations.Activity;

/**
 * Created by SEGU on 2016-12-01.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngje.tgwing.accommodations.R;

import java.util.LinkedList;
import java.util.List;

public class CountryAdapter extends BaseAdapter {

    private List<Country> countryList;
    public CountryAdapter(){
        countryList = new LinkedList<Country>();
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Country getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View tempView = convertView;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tempView = inflater.inflate(R.layout.countryselectitem, parent, false);
            holder = new ViewHolder(tempView);
            tempView.setTag(holder);
        } else {
            holder = (ViewHolder) tempView.getTag();
        }

        holder.nameOfCountryTextView.setText(getItem(position).getNameOfCountry());
        holder.imageOfCountryImageView.setImageBitmap(getItem(position).getImageOfCountry());

        return tempView;
    }

    public void addItem(String nameOfCountry, Bitmap imageOfCountry, String codeOfCountry){
        countryList.add(new Country(nameOfCountry,imageOfCountry, codeOfCountry));
    }

    private class ViewHolder{
        private TextView nameOfCountryTextView;
        private ImageView imageOfCountryImageView;

        public ViewHolder(View view){
            nameOfCountryTextView = (TextView) view.findViewById(R.id.nameOfCountry);
            imageOfCountryImageView = (ImageView) view.findViewById(R.id.imageOfCountry);
        }
    }
}