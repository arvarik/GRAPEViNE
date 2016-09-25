package com.oose2015.grapevine;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * https://github.com/Phantast/smartnavi/tree/master/smartnavi/src/free/java/com/ilm/sandwich/tools
 */

public class PlaceAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> resultList;
    private Context mContext;
    private int mResource;
    private AutocompletePlacesAPI mPlaceAPI = new AutocompletePlacesAPI();

    /**
     * Constructor for class
     * @param context context of the Class
     * @param resource the resource
     */
    public PlaceAutocompleteAdapter(Context context, int resource) {
        super(context, resource);
 
        mContext = context;
        mResource = resource;
    }
 
    @Override
    /**
     * Gets the count of the results array
     */
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }
 
    @Override
    /**
     * Gets an item from the list
     * @param position the position of the list
     */
    public String getItem(int position) {
        return resultList.get(position);
    }
 
    @Override
    /**
     * Filters the list to return the results
     */
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());
 
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
 
                return filterResults;
            }
 
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
 
        return filter;
    }
}