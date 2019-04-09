package com.montek.tohafaa.Adapter;
import android.app.Dialog;
import android.content.Context;
import android.os.*;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
//import com.bumptech.glide.Glide;
import com.montek.tohafaa.Activity_OrdersList;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.R;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.interfaces.ItemClickListener;
import com.montek.tohafaa.model.Item;
import com.montek.tohafaa.model.categoryitems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Adapter.OrderListAdapter.*;
import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.DashBordActivity.dialog;
import static com.montek.tohafaa.LoginActivity.p;
public class TrackOrderProductAdapter extends RecyclerView.Adapter<TrackOrderProductAdapter.ViewHolder> {
    private ArrayList<categoryitems> item;
    private Context context;
    private ItemClickListener clickListener;
    EditText edtx_ifsccode,edtx_bankname,edtx_ConfirmAccountNumber,edtx_branchname,edtx_AccountHolder,edtx_AccountNumber,edtx_PhoneNumber;
    ListView list;
    String req_type="",req_reason="",product_id,ifsccode,bankname,ConfirmAccountNumber,branchname,AccountHolder,PhoneNumber,AccountNumber;
    TextInputLayout PhoneNumberinputlayout,ConfirmAccountinputlayout,IFSCCodeinputlayout,BankNameinputlayout,BranchNameinputlayout,AccountHolderinputlayout,AccountNumberinputlayout;
    public TrackOrderProductAdapter(Context context, ArrayList<categoryitems> item) {
        this.item = item;
        this.context = context;
        //  this.fragmentManager = fragmentManager;
    }
    @Override
    public TrackOrderProductAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_orderproductlistelement, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final TrackOrderProductAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.txv_status.setText(item.get(i).getstatus_by_admin());
        if ( item.get(i).getproduct_offer().equalsIgnoreCase("1") &&item.get(i).getreq_from().equalsIgnoreCase("customer")) {
            viewHolder.txv_adminActionStatus.setVisibility(View.GONE);
            viewHolder.txv_ActionStatus.setVisibility(View.GONE);
            viewHolder.li_Action.setVisibility(View.GONE);
        }
        else if( item.get(i).getstatus_by_admin().equalsIgnoreCase("Cancelled")&& item.get(i).getreq_from().equalsIgnoreCase("admin") &&  item.get(i).getreq_bank_detail_status().equalsIgnoreCase("0") )
        {
            viewHolder.txv_adminActionStatus.setText("This order is cancelled by admin due to some technical reason.");
            viewHolder.txv_ActionStatus.setText("Status of Product");
            viewHolder.txv_adminActionStatus.setVisibility(View.VISIBLE);
            viewHolder.txv_ActionStatus.setVisibility(View.VISIBLE);
        }
        else if(item.get(i).getstatus_by_admin().equalsIgnoreCase("Cancelled") && item.get(i).getreq_from().equalsIgnoreCase("admin") &&  item.get(i).getreq_bank_detail_status().equalsIgnoreCase("1") )
        {
            viewHolder.txv_adminActionStatus.setText("Payment details for order cancellation received.Payment transfer will be done within 10 days.");
            viewHolder.txv_ActionStatus.setText("Status of Product");
            viewHolder.txv_adminActionStatus.setVisibility(View.VISIBLE);
            viewHolder.txv_ActionStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.txv_adminActionStatus.setVisibility(View.GONE);
            viewHolder.txv_ActionStatus.setVisibility(View.GONE);
        }
        Log.d("CartList.get",item.get(i).getstatus_by_admin()+item.get(i).getorderproductstatus()+" "+item.get(i).getreq_from());
        if (item.get(i).getstatus_by_admin().equalsIgnoreCase("Pending")&& item.get(i).getproduct_offer().equalsIgnoreCase("0")&& (item.get(i).getreq_from().equalsIgnoreCase("customer")|| item.get(i).getreq_from().equalsIgnoreCase(null)) && item.get(i).getreq_bank_detail_status().equalsIgnoreCase("1") ) {
            viewHolder.txv_Action.setText("Cancel");
            viewHolder.li_Action.setVisibility(View.GONE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_bank.setVisibility(View.GONE);
            viewHolder.txv_adminActionStatus.setText("Your request for cancel is received.");
            viewHolder.txv_ActionStatus.setText("Status of Cancel Request");
            viewHolder.txv_adminActionStatus.setVisibility(View.VISIBLE);
            viewHolder.txv_ActionStatus.setVisibility(View.VISIBLE);
        } if (item.get(i).getstatus_by_admin().equalsIgnoreCase("Pending")&& item.get(i).getproduct_offer().equalsIgnoreCase("2")&& (item.get(i).getreq_from().equalsIgnoreCase("customer")|| item.get(i).getreq_from().equalsIgnoreCase(null)) && item.get(i).getreq_bank_detail_status().equalsIgnoreCase("0") ) {
            viewHolder.txv_Action.setText("Cancel");
            viewHolder.li_Action.setVisibility(View.GONE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_bank.setVisibility(View.VISIBLE);
            //((View)viewHolder.itemView.findViewById(R.id.view)).setVisibility(View.GONE);
        }
        else if (item.get(i).getstatus_by_admin().equalsIgnoreCase("Approved")  && (item.get(i).getreq_from().equalsIgnoreCase("customer")|| item.get(i).getreq_from().equalsIgnoreCase(null))) {
            viewHolder.txv_Action.setText("Cancel");
            viewHolder.li_Action.setVisibility(View.GONE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_bank.setVisibility(View.GONE);
            viewHolder.txv_adminActionStatus.setText("Your request for cancel is received.");
            viewHolder.txv_ActionStatus.setText("Status of Cancel Request");
            viewHolder.txv_adminActionStatus.setVisibility(View.VISIBLE);
            viewHolder.txv_ActionStatus.setVisibility(View.VISIBLE);
        }
        else if (item.get(i).getstatus_by_admin().equalsIgnoreCase("Delivered") &&   item.get(i).getreq_from().equalsIgnoreCase("customer")) {
            viewHolder.txv_Action.setText("Return");
            viewHolder.li_Action.setVisibility(View.GONE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_bank.setVisibility(View.GONE);
        }else if (item.get(i).getstatus_by_admin().equalsIgnoreCase("Delivered")) {
            viewHolder.txv_Action.setText("Return");
            viewHolder.li_Action.setVisibility(View.VISIBLE);
            viewHolder.li_exchange.setVisibility(View.VISIBLE);
            viewHolder.li_bank.setVisibility(View.GONE);
        }
        else  if(item.get(i).getstatus_by_admin().equalsIgnoreCase("Cancelled") && item.get(i).getreq_from().equalsIgnoreCase("customer") )
        {
            viewHolder.li_bank.setVisibility(View.GONE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_Action.setVisibility(View.GONE);
           // viewHolder.view1.setVisibility(View.GONE);
            //viewHolder.view11.setVisibility(View.GONE);
            if(item.get(i).getreq_reason()!=null) {
                viewHolder.txv_adminActionStatus.setText(item.get(i).getreq_reason());
                viewHolder.txv_ActionStatus.setText("Status of Product");
                viewHolder.txv_adminActionStatus.setVisibility(View.VISIBLE);
                viewHolder.txv_ActionStatus.setVisibility(View.VISIBLE);
            }
        }else if(item.get(i).getstatus_by_admin().equalsIgnoreCase("Cancelled") && item.get(i).getreq_from().equalsIgnoreCase("admin") &&  item.get(i).getreq_bank_detail_status().equalsIgnoreCase("0"))
        {
            viewHolder.li_bank.setVisibility(View.VISIBLE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_Action.setVisibility(View.GONE);
            //viewHolder.view1.setVisibility(View.GONE);
            //viewHolder.view11.setVisibility(View.GONE);
        }
        else if(item.get(i).getstatus_by_admin().equalsIgnoreCase("Cancelled") && item.get(i).getreq_from().equalsIgnoreCase("admin") &&  item.get(i).getreq_bank_detail_status().equalsIgnoreCase("1"))
        {
            viewHolder.li_bank.setVisibility(View.GONE);
            viewHolder.li_exchange.setVisibility(View.GONE);
            viewHolder.li_Action.setVisibility(View.GONE);
            ///((View)view.findViewById(R.id.view)).setVisibility(View.GONE);
           // ((View)viewHolder.itemView.findViewById(R.id.view11)).setVisibility(View.GONE);
        }
//        ///settext for peri
//        if (item.get(i).getproduct_cod().equalsIgnoreCase("0")) {
//            viewHolder.txv_Perishablecheck.setText("Non Perishable");
//            viewHolder.txv_Action.setEnabled(true);
//        } else {
//            viewHolder.txv_Perishablecheck.setText("Perishable");
//            viewHolder.txv_Action.setEnabled(false);
//        }
        viewHolder.tv_name.setText(item.get(i).getproduct_name());
        viewHolder.price.setText(item.get(i).getproduct_price()+" x "+item.get(i).getproduct_quantity());
        viewHolder.txv_subtotal.setText(item.get(i).getordertotal());
        // txv_qty.setText(CartList.get(position).getproduct_quantity());
//        Glide.with(context).load("https://www.tohafaa.com/" + item.get(i).getproduct_image())
//                .thumbnail(0.5f)
//                .crossFade()
//                .skipMemoryCache(true)
//                .into( viewHolder.img);

        imgloader.init(ImageLoaderConfiguration.createDefault(context));
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_flag_blank)
                .showImageOnFail(R.drawable.ic_flag_blank)
                .showImageOnLoading(R.drawable.ic_flag_blank)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        //download and display image from url
        imgloader.displayImage("http://www.tohafaa.com/" + item.get(i).getproduct_image(), viewHolder.img, options);

        if (item.get(i).getproduct_superimpose_image().equalsIgnoreCase("") || item.get(i).getproduct_superimpose_image().equalsIgnoreCase("0") || item.get(i).getproduct_superimpose_image().equalsIgnoreCase("null") || item.get(i).getproduct_superimpose_image().equalsIgnoreCase(null)) {
            viewHolder.image_superimpose.setVisibility(View.GONE);
            viewHolder.txv_superimposetext.setVisibility(View.GONE);
            viewHolder.Superimpose.setText("No superimpose image");
            viewHolder.Superimposelayout.setVisibility(View.GONE);
        }
        viewHolder.li_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type="bank";
                product_id = item.get(i).getproduct_id();
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_bankaccountdetails);
                TextView txv_terms = (TextView) dialog.findViewById(R.id.txv_terms);
                TextView txv_reason = (TextView) dialog.findViewById(R.id.txv_reason);
                edtx_ifsccode = (EditText) dialog.findViewById(R.id.edtx_ifsccode);//find_editext
                edtx_bankname = (EditText) dialog.findViewById(R.id.edtx_bankname);
                edtx_ConfirmAccountNumber = (EditText) dialog.findViewById(R.id.edtx_ConfirmAccountNumber);
                edtx_branchname = (EditText) dialog.findViewById(R.id.edtx_branchname);
                edtx_AccountHolder = (EditText) dialog.findViewById(R.id.edtx_AccountHolder);
                edtx_AccountNumber = (EditText) dialog.findViewById(R.id.edtx_AccountNumber);
                edtx_PhoneNumber = (EditText) dialog.findViewById(R.id.edtx_PhoneNumber);
                IFSCCodeinputlayout = (TextInputLayout) dialog.findViewById(R.id.IFSCCodeinputlayout);
                BankNameinputlayout = (TextInputLayout) dialog.findViewById(R.id.BankNameinputlayout);
                BranchNameinputlayout = (TextInputLayout) dialog.findViewById(R.id.BranchNameinputlayout);
                AccountHolderinputlayout = (TextInputLayout) dialog.findViewById(R.id.AccountHolderinputlayout);
                AccountNumberinputlayout = (TextInputLayout) dialog.findViewById(R.id.AccountNumberinputlayout);
                ConfirmAccountinputlayout = (TextInputLayout) dialog.findViewById(R.id.ConfirmAccountinputlayout);
                PhoneNumberinputlayout = (TextInputLayout) dialog.findViewById(R.id.PhoneNumberinputlayout);
                list = (ListView) dialog.findViewById(R.id.list);
               // viewHolder.view11.setVisibility(View.GONE);
                ((TextView) dialog.findViewById(R.id.txv_terms2)).setVisibility(View.GONE);
                list.setVisibility(View.GONE);
                txv_reason.setVisibility(View.GONE);
                txv_terms.setVisibility(View.GONE);
                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ifsccode = edtx_ifsccode.getText().toString();
                        bankname = edtx_bankname.getText().toString();
                        ConfirmAccountNumber = edtx_ConfirmAccountNumber.getText().toString();
                        branchname = edtx_branchname.getText().toString();
                        AccountHolder = edtx_AccountHolder.getText().toString();
                        AccountNumber = edtx_AccountNumber.getText().toString();
                        PhoneNumber = edtx_PhoneNumber.getText().toString();
                        Log.d("iiiii", req_reason);
                        validationofbankdetails();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            }
        });
        viewHolder.li_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type="Exchange";
                product_id = item.get(i).getproduct_id();
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_bankaccountdetails);
                TextView txv_terms = (TextView) dialog.findViewById(R.id.txv_terms);
                TextView txv_reason = (TextView) dialog.findViewById(R.id.txv_reason);
                edtx_ifsccode = (EditText) dialog.findViewById(R.id.edtx_ifsccode);//find_editext
                edtx_bankname = (EditText) dialog.findViewById(R.id.edtx_bankname);
                edtx_ConfirmAccountNumber = (EditText) dialog.findViewById(R.id.edtx_ConfirmAccountNumber);
                edtx_branchname = (EditText) dialog.findViewById(R.id.edtx_branchname);
                edtx_AccountHolder = (EditText) dialog.findViewById(R.id.edtx_AccountHolder);
                edtx_AccountNumber = (EditText) dialog.findViewById(R.id.edtx_AccountNumber);
                edtx_PhoneNumber = (EditText) dialog.findViewById(R.id.edtx_PhoneNumber);
                IFSCCodeinputlayout = (TextInputLayout) dialog.findViewById(R.id.IFSCCodeinputlayout);
                BankNameinputlayout = (TextInputLayout) dialog.findViewById(R.id.BankNameinputlayout);
                BranchNameinputlayout = (TextInputLayout) dialog.findViewById(R.id.BranchNameinputlayout);
                AccountHolderinputlayout = (TextInputLayout) dialog.findViewById(R.id.AccountHolderinputlayout);
                AccountNumberinputlayout = (TextInputLayout) dialog.findViewById(R.id.AccountNumberinputlayout);
                ConfirmAccountinputlayout = (TextInputLayout) dialog.findViewById(R.id.ConfirmAccountinputlayout);
                PhoneNumberinputlayout = (TextInputLayout) dialog.findViewById(R.id.PhoneNumberinputlayout);
                list = (ListView) dialog.findViewById(R.id.list);
                new Retrive_RequestReason().execute();
                list.setVisibility(View.VISIBLE);
                Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ifsccode = edtx_ifsccode.getText().toString();
                        bankname = edtx_bankname.getText().toString();
                        ConfirmAccountNumber = edtx_ConfirmAccountNumber.getText().toString();
                        branchname = edtx_branchname.getText().toString();
                        AccountHolder = edtx_AccountHolder.getText().toString();
                        AccountNumber = edtx_AccountNumber.getText().toString();
                        PhoneNumber = edtx_PhoneNumber.getText().toString();
                        Log.d("iiiii", req_reason);
                        validationofbankdetails();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.FILL_PARENT);
            }
        });
        viewHolder.li_Action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product_id = item.get(i).getproduct_id();
                if(item.get(i).getstatus_by_admin().equalsIgnoreCase("Delivered"))
                { req_type="Return";}
                else
                {req_type="Cancel";}

                if (item.get(i).getproduct_brand_id().equalsIgnoreCase("0") && item.get(i).getproduct_tax().equalsIgnoreCase(req_type)) {
                    Toast.makeText(view.getContext(),"Already Sent Request",Toast.LENGTH_LONG).show();
                } else {
                    dialog = new Dialog(view.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_bankaccountdetails);
                    TextView txv_terms = (TextView) dialog.findViewById(R.id.txv_terms);
                    TextView txv_reason = (TextView) dialog.findViewById(R.id.txv_reason);
                    edtx_ifsccode = (EditText) dialog.findViewById(R.id.edtx_ifsccode);//find_editext
                    edtx_bankname = (EditText) dialog.findViewById(R.id.edtx_bankname);
                    edtx_ConfirmAccountNumber = (EditText) dialog.findViewById(R.id.edtx_ConfirmAccountNumber);
                    edtx_branchname = (EditText) dialog.findViewById(R.id.edtx_branchname);
                    edtx_AccountHolder = (EditText) dialog.findViewById(R.id.edtx_AccountHolder);
                    edtx_AccountNumber = (EditText) dialog.findViewById(R.id.edtx_AccountNumber);
                    edtx_PhoneNumber = (EditText) dialog.findViewById(R.id.edtx_PhoneNumber);
                    IFSCCodeinputlayout = (TextInputLayout) dialog.findViewById(R.id.IFSCCodeinputlayout);
                    BankNameinputlayout = (TextInputLayout) dialog.findViewById(R.id.BankNameinputlayout);
                    BranchNameinputlayout = (TextInputLayout) dialog.findViewById(R.id.BranchNameinputlayout);
                    AccountHolderinputlayout = (TextInputLayout) dialog.findViewById(R.id.AccountHolderinputlayout);
                    AccountNumberinputlayout = (TextInputLayout) dialog.findViewById(R.id.AccountNumberinputlayout);
                    ConfirmAccountinputlayout = (TextInputLayout) dialog.findViewById(R.id.ConfirmAccountinputlayout);
                    PhoneNumberinputlayout = (TextInputLayout) dialog.findViewById(R.id.PhoneNumberinputlayout);
                    list = (ListView) dialog.findViewById(R.id.list);
                    new Retrive_RequestReason().execute();
                    list.setVisibility(View.VISIBLE);
                    Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ifsccode = edtx_ifsccode.getText().toString();
                            bankname = edtx_bankname.getText().toString();
                            ConfirmAccountNumber = edtx_ConfirmAccountNumber.getText().toString();
                            branchname = edtx_branchname.getText().toString();
                            AccountHolder = edtx_AccountHolder.getText().toString();
                            AccountNumber = edtx_AccountNumber.getText().toString();
                            PhoneNumber = edtx_PhoneNumber.getText().toString();
                            Log.d("iiiii", req_reason);
                            validationofbankdetails();
                        }
                    });
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.FILL_PARENT);
                }
            }
        });
        viewHolder.txv_shippingaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_shipingaddress);
                TextView txv_Name = (TextView) dialog.findViewById(R.id.txv_Name);//findtext
                TextView txv_Contact = (TextView) dialog.findViewById(R.id.txv_Contact);
                TextView txv_Cost = (TextView) dialog.findViewById(R.id.txv_Cost);
                TextView txv_Email = (TextView) dialog.findViewById(R.id.txv_Email);
                TextView txv_Address1 = (TextView) dialog.findViewById(R.id.txv_Address1);
                TextView txv_Address2 = (TextView) dialog.findViewById(R.id.txv_Address2);
                TextView txv_DeliveryDate = (TextView) dialog.findViewById(R.id.txv_DeliveryDate);
                TextView txv_City = (TextView) dialog.findViewById(R.id.txv_City);
                TextView txv_State = (TextView) dialog.findViewById(R.id.txv_State);
                TextView txv_Country = (TextView) dialog.findViewById(R.id.txv_Country);
                LinearLayout li_shippingaddress = (LinearLayout) dialog.findViewById(R.id.li_shippingaddress);
                //setext
                txv_DeliveryDate.setText(item.get(i).getproduct_size());
                txv_Name.setText(item.get(i).getshippingname());
                if (shipping_address_same_as_billing_address.equalsIgnoreCase("1")) {
                    txv_Contact.setText(shipping_contact);
                    txv_Cost.setText(shipping_cost);
                    txv_Email.setText(shipping_email);
                    txv_Address1.setText(shipping_address_1);
                    txv_Address2.setText(shipping_address_2);
                    txv_City.setText(shipping_city);
                    txv_State.setText(shipping_state);
                    txv_Country.setText(shipping_country);
                } else {
                    txv_Contact.setText(item.get(i).getproduct_sub2category_id());
                    txv_Cost.setText(item.get(i).getshipping_cost());
                    txv_Email.setText(item.get(i).getimage_url());
                    txv_Address1.setText(item.get(i).getname());
                    li_shippingaddress.setVisibility(View.GONE);
                    txv_City.setText(item.get(i).getproduct_spc_id());
                    txv_State.setText(item.get(i).getproduct_description());
                    txv_Country.setText(item.get(i).getproduct_category_id());
                }
                Button btn_close = (Button) dialog.findViewById(R.id.btn_close);
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            }
        });
    }
    @Override
    public int getItemCount() {
        return item.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        LinearLayout Superimposelayout;
       // View view11,view1;
        LinearLayout li_exchange,li_bank,li_Action,txv_shippingaddress;
        private TextView  txv_bank,txv_exchange,txv_status,txv_ActionStatus,txv_Action,txv_adminActionStatus, txv_Perishablecheck, tv_name, price, Superimpose,  txv_subtotal, txv_superimposetext;
        private ImageView image_superimpose, img;
        private ItemClickListener clickListener;
        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.txv_name);
            price = (TextView) view.findViewById(R.id.txv_price);
            txv_subtotal = (TextView) view.findViewById(R.id.txv_subtotal);
            txv_superimposetext = (TextView) view.findViewById(R.id.txv_superimposetext);
            img = (ImageView) view.findViewById(R.id.image_wishlist);
            image_superimpose = (ImageView) view.findViewById(R.id.image_superimpose);
            Superimposelayout = (LinearLayout) view.findViewById(R.id.Superimposelayout);
            Superimpose = (TextView) view.findViewById(R.id.Superimpose);
            txv_shippingaddress = (LinearLayout) view.findViewById(R.id.txv_shippingaddress);
            li_Action = (LinearLayout) view.findViewById(R.id.li_Action);
            txv_Action = (TextView) view.findViewById(R.id.txv_Action);
            txv_Perishablecheck = (TextView) view.findViewById(R.id.txv_Perishablecheck);
            txv_adminActionStatus = (TextView) view.findViewById(R.id.txv_adminActionStatus);
            txv_ActionStatus=(TextView)view.findViewById(R.id.txv_ActionStatus);
            txv_status=(TextView)view.findViewById(R.id.txv_status);
            li_exchange=(LinearLayout)view.findViewById(R.id.li_exchange);
            txv_exchange=(TextView)view.findViewById(R.id.txv_exchange);
            li_bank=(LinearLayout)view.findViewById(R.id.li_bank);
            txv_bank=(TextView)view.findViewById(R.id.txv_bank);
        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }
        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getPosition(), true);
            return true;
        }
    }
    private void validationofbankdetails() {
        boolean isValid = true;
        if (ifsccode.isEmpty()) {
            IFSCCodeinputlayout.setError("ifscode Name is mandatory");
            isValid = false;
        } else {
            IFSCCodeinputlayout.setErrorEnabled(false);
        }
        if (bankname.isEmpty()) {
            BankNameinputlayout.setError("Bank Name is mandatory");
            isValid = false;
        } else {
            BankNameinputlayout.setErrorEnabled(false);
        }
        if (branchname.isEmpty()) {
            BranchNameinputlayout.setError("Branch Name is mandatory");
            isValid = false;
        }  else {
            BranchNameinputlayout.setErrorEnabled(false);
        }
        if (AccountHolder.isEmpty()) {
            AccountHolderinputlayout.setError("AccountHolder cannot blank");
            isValid = false;
        }  else {
            AccountHolderinputlayout.setErrorEnabled(false);
        }
        if (AccountNumber.isEmpty()) {
            AccountNumberinputlayout.setError("AccountNumber is mandatory");
            isValid = false;
        }  else {
            AccountNumberinputlayout.setErrorEnabled(false);
        }
        if (ConfirmAccountNumber.isEmpty()) {
            ConfirmAccountinputlayout.setError("ConfirmAccountNumber is mandatory");
            isValid = false;
        } else {
            ConfirmAccountinputlayout.setErrorEnabled(false);
        }
        if (PhoneNumber.isEmpty()) {
            PhoneNumberinputlayout.setError("PhoneNumber is mandatory");
            isValid = false;
        }else if (PhoneNumber.length() < 10) {
            PhoneNumberinputlayout.setError("mobile should be valid");
            isValid = false;
        }
        else {
            PhoneNumberinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            if((req_reason.equalsIgnoreCase("") || req_reason.isEmpty()) && req_type!="bank")
            {
                Toast.makeText(context,"Please Select Reason for Cancellation",Toast.LENGTH_LONG).show();
            }else {
                if (ConfirmAccountNumber.equalsIgnoreCase(AccountNumber)) {
                    new InsertRequestforOrder().execute();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please check Account No", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    /******************************************* closed send record to back end ************************************/
    class InsertRequestforOrder extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("req_product_order_id",product_id);
            params.put("req_order_id", orderid);
            params.put("req_type",req_type);
            params.put("req_reason", req_reason);
            params.put("req_status","0");
            params.put("admin_req_status","0");
            params.put("ifsc_code",ifsccode);
            params.put("bank_name", bankname);
            params.put("branch_name",branchname);
            params.put("account_number",AccountNumber);
            params.put("account_holder",AccountHolder);
            params.put("confirm_account_number", ConfirmAccountNumber);
            params.put("phone_number", PhoneNumber);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"InsertRequestforOrder", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("Json response:", s);
                if(s.equalsIgnoreCase("Inserted Successfully")) {
                    Toast.makeText(context," Request for Order Cancellation Successfully.You will be notified about approval of the cancellation request.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
    }
    class Retrive_RequestReason extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("rr_req_type", req_type);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "retrive_request_reason", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            ArrayList Listofrequestresons = new ArrayList<Item>();
            if (s.contains("No Results Found")) {
            } else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    Item item;
                    Listofrequestresons.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        int rr_id = jsonObject.getInt("rr_id");
                        String rr_title = jsonObject.getString("rr_title");
                        String rr_req_type = jsonObject.getString("rr_req_type");
                        item = new Item(rr_id, rr_title, rr_req_type);
                        Listofrequestresons.add(item);
                    }
                    ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(context, android.R.layout.simple_list_item_single_choice, Listofrequestresons) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView item = (TextView) super.getView(position, convertView, parent);
                            item.setTextSize(13);
//                            item.setTypeface(mTypeface);
//                            item.setTextColor(Color.parseColor("#FF3E80F1"));
//                            item.setTypeface(item.getTypeface(), Typeface.BOLD);
                            return item;
                        }
                    };
                    list.setAdapter(adapter);
                    list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Item ii = (Item) parent.getItemAtPosition(position);
                            req_reason = Integer.toString(ii.getid());
                            req_type = ii.getphonecode();
                            Log.d("Item", req_reason + req_type);
                        }
                    });
                } catch (Exception e) {e.getMessage();}
            }
        }
    }


}