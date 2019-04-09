package com.montek.tohafaa.Adapter;
import android.content.Context;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.R;
import com.montek.tohafaa.model.Filtersortlist;
import java.util.ArrayList;
public class ListAdapterFilterSortlist extends ArrayAdapter<Filtersortlist> {
    public ArrayList<Filtersortlist> MainList;
    public ArrayList<Filtersortlist> SortinglistFreelancerListTemp;
    public ListAdapterFilterSortlist.SortingListFreelancerDataFilter SortinglistFreelancerDataFilter ;
    public ListAdapterFilterSortlist(Context context, int id, ArrayList<Filtersortlist> SortinglistFreelancerArrayList) {
        super(context, id, SortinglistFreelancerArrayList);
        this.SortinglistFreelancerListTemp = new ArrayList<>();
        this.SortinglistFreelancerListTemp.addAll(SortinglistFreelancerArrayList);
        this.MainList = new ArrayList<>();
        this.MainList.addAll(SortinglistFreelancerArrayList);
    }
    @Override
    public Filter getFilter() {
        if (SortinglistFreelancerDataFilter == null){
            SortinglistFreelancerDataFilter  = new ListAdapterFilterSortlist.SortingListFreelancerDataFilter();
        }
        return SortinglistFreelancerDataFilter;
    }

    public class ViewHolder {
        TextView Name;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListAdapterFilterSortlist.ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_view_items, null);
            holder = new ListAdapterFilterSortlist.ViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ListAdapterFilterSortlist.ViewHolder) convertView.getTag();
        }
        //((holder) convertView.getTag()).checkbox.setTag(FilterListTemp.get(position));
        Filtersortlist test = SortinglistFreelancerListTemp.get(position);
        holder.Name.setText(test.getsorting_title());
       // holder.image.setChecked(categoires.getimage());
        return convertView;
    }
    private class SortingListFreelancerDataFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            charSequence = charSequence.toString().toLowerCase();
            FilterResults filterResults = new FilterResults();
            if(charSequence != null && charSequence.toString().length() > 0)
            {
                ArrayList<Filtersortlist> arrayList1 = new ArrayList<>();
                for(int i = 0, l = MainList.size(); i < l; i++)
                {
                    Filtersortlist subject = MainList.get(i);
                    if(subject.toString().toLowerCase().contains(charSequence))
                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();
                filterResults.values = arrayList1;
            }
            else
            {
                synchronized(this)
                {
                    filterResults.values = MainList;
                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            SortinglistFreelancerListTemp = (ArrayList<Filtersortlist>)filterResults.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = SortinglistFreelancerListTemp.size(); i < l; i++)
                add(SortinglistFreelancerListTemp.get(i));
            notifyDataSetInvalidated();
        }
    }
}