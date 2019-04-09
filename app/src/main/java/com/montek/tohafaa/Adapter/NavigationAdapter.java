package com.montek.tohafaa.Adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.R;
import com.montek.tohafaa.model.Item;
import java.util.ArrayList;
public class NavigationAdapter extends ArrayAdapter<Item> {
    ArrayList<Item> List = new ArrayList<>();
    public NavigationAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        List = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_view_items, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        if(List.get(position).getimage().equalsIgnoreCase("subcategory"))
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)textView.getLayoutParams();
            params.setMargins(110, 0, 0, 0);
            textView.setLayoutParams(params);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            imageView.setVisibility(View.GONE);
        }
        else
        {
            //textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }
        textView.setText(List.get(position).getName());
        //imageView.setImageResource(List.get(position).getimage());
        return v;
    }
}