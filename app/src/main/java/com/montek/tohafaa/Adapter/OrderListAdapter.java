package com.montek.tohafaa.Adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.Activity_OrdersDetails;
import com.montek.tohafaa.R;
import com.montek.tohafaa.model.categoryitems;
import java.text.*;
import java.util.*;
import static com.montek.tohafaa.Activity_OrdersList.flagforpdfororderlist;
public class OrderListAdapter extends ArrayAdapter<categoryitems> {
        public ArrayList<categoryitems> MainList,item;
        public ArrayList<categoryitems> ListTemp;
        public OrderListAdapter.DataFilter Filter ;
        public static String orderdelivery_date,flagcancelrequest="",orderid,orderdate,ordergross_total,orderstatus,totalofcart,shipping_cost,name,paymentname,shippingname,invoice_no,shipping_address_same_as_billing_address, shipping_contact,shipping_email,shipping_address_1,shipping_address_2,shipping_city,shipping_state,shipping_pincode,shipping_country;
        public OrderListAdapter(Context context, int id, ArrayList<categoryitems> ArrayList) {
            super(context, id, ArrayList);
            this.ListTemp = new ArrayList<categoryitems>();
            this.ListTemp.addAll(ArrayList);
            this.item = new ArrayList<categoryitems>();
            this.item.addAll(ArrayList);
            this.MainList = new ArrayList<categoryitems>();
            this.MainList.addAll(item);
        }
        @Override
        public Filter getFilter() {
            if (Filter == null){
                Filter  = new OrderListAdapter.DataFilter();
            }
            return Filter;
        }
        public class ViewHolder {
            TextView txv_transactionid,txv_total,txv_rate,txv_productname,txv_qty,txv_orderid,txv_orderdate,txv_ordertotal,txv_cancelreq;
            LinearLayout txv_btn;
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
           OrderListAdapter.ViewHolder holder = null;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.activity_trackorderlist, null);
                holder = new OrderListAdapter.ViewHolder();
                holder.txv_orderid = (TextView)view.findViewById(R.id.txv_orderid);
                holder.txv_orderdate = (TextView)view.findViewById(R.id.txv_orderdate);
                holder.txv_ordertotal = (TextView) view.findViewById(R.id.txv_ordertotal);
                holder.txv_transactionid = (TextView) view.findViewById(R.id.txv_transactionid);
                holder.txv_btn = (LinearLayout) view.findViewById(R.id.txv_btn);
                              view.setTag(holder);
            } else {
                holder = (OrderListAdapter.ViewHolder) view.getTag();
            }
          //  final categoryitems list = ListTemp.get(position);
            holder.txv_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    orderid = ListTemp.get(position).getorderid().trim();
                    try {
                        orderdate = getDate(ListTemp.get(position).getorderdate().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    orderdelivery_date=ListTemp.get(position).getproduct_size().trim();
                    ordergross_total=ListTemp.get(position).getordertotal().trim();
                    orderstatus=ListTemp.get(position).getorderstatus().trim();
                    totalofcart = ListTemp.get(position).gettotalofcart().trim();
                    shipping_cost = ListTemp.get(position).getshipping_cost().trim();
                    name=ListTemp.get(position).getname().trim();
                    paymentname=ListTemp.get(position).getpaymentname().trim();
                    shippingname=ListTemp.get(position).getshippingname().trim();
                    invoice_no=ListTemp.get(position).getinvoice_no().trim();
                    shipping_contact = ListTemp.get(position).getproduct_cod().trim();
                    shipping_email=ListTemp.get(position).getimage_url().trim();
                    shipping_address_1=ListTemp.get(position).getproduct_sub2category_id().trim();
                    shipping_address_2 = ListTemp.get(position).getproduct_sub3category_id().trim();
                    shipping_city = ListTemp.get(position).getproduct_spc_id().trim();
                    shipping_state=ListTemp.get(position).getproduct_description().trim();
                    shipping_pincode=ListTemp.get(position).getpincode().trim();
                    shipping_country=ListTemp.get(position).getproduct_category_id().trim();
                    shipping_address_same_as_billing_address=ListTemp.get(position).getid().trim();
                    Intent i1=new Intent(view.getContext(),Activity_OrdersDetails.class);
                    view.getContext().startActivity(i1);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.finish();
                }
         });
            if(flagforpdfororderlist.equalsIgnoreCase("OrderList")) {
                holder.txv_transactionid.setText(ListTemp.get(position).gettxnid());
                try {
                    holder.txv_orderdate.setText(getDate(ListTemp.get(position).getorderdate()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.txv_ordertotal.setText(ListTemp.get(position).getordertotal());
                if(ListTemp.get(position).gettxnid()!=null){
                holder.txv_orderid.setText("Order_ID : "+ListTemp.get(position).getorderid());}
                else {
                    holder.txv_orderid.setText("Order_ID : 0");
                }
            }
            return view;
  }
        private class DataFilter extends Filter
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charSequence = charSequence.toString().toLowerCase();
                FilterResults filterResults = new FilterResults();
                if(charSequence != null && charSequence.toString().length() > 0)
                {
                    ArrayList<categoryitems> arrayList = new ArrayList<categoryitems>();
                    for(int i = 0, l = MainList.size(); i < l; i++)
                    {
                        categoryitems subject = MainList.get(i);
                        if(subject.toString().toLowerCase().contains(charSequence))
                            arrayList.add(subject);
                    }
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
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
                ListTemp = (ArrayList<categoryitems>)filterResults.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = ListTemp.size(); i < l; i++)
                    add(ListTemp.get(i));
                notifyDataSetInvalidated();
            }
        }

    public static String getDate(String date) throws ParseException {
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate=spf.parse(date);
        spf= new SimpleDateFormat("dd-MM-yyyy");
        String str = spf.format(newDate);
        System.out.println(date);
        return str;
    }

}