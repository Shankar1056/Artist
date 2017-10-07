package bigappcompany.com.artist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import bigappcompany.com.artist.models.CitiesModel;

/**
 * Created by ravi on 3/18/17.
 */

public class CustomAdapter extends ArrayAdapter<CitiesModel> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<CitiesModel> items;
    private ArrayList<CitiesModel> itemsAll;
    private ArrayList<CitiesModel> suggestions;
    private int viewResourceId;

    public CustomAdapter(Context context, int viewResourceId, ArrayList<CitiesModel> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<CitiesModel>) items.clone();
        this.suggestions = new ArrayList<CitiesModel>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        CitiesModel customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v;
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(customer.getCity());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((CitiesModel)(resultValue)).getCity();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (CitiesModel customer : itemsAll) {
                    if(customer.getCity().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<CitiesModel> filteredList = (ArrayList<CitiesModel>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (CitiesModel c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };



}
