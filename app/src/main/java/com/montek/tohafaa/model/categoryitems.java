package com.montek.tohafaa.model;

import java.text.ParseException;

import static com.montek.tohafaa.Adapter.OrderListAdapter.getDate;

public class categoryitems {
    private String discount,product_gst, name,po_discount, req_reason,image_url, product_wishlistid,product_shipping_cost,product_code,product_model,product_description,product_id,product_quantity,product_price,product_sku,product_substract_stock,product_free_shipping,product_free_shipping_limit, product_tax_status,product_tax,product_category_id,product_subcategory_id,product_sub2category_id,product_sub3category_id,product_sub4category_id,product_spc_id,product_image,product_offer,id,product_name,product_with_package,product_color,product_size,product_superimpose_image,product_brand_id;
    String req_from,req_bank_detail_status,txnid,status_by_admin,orderproductstatus,getproduct_sub2category_id,product_specification,invoice_no,shippingname,paymentname,shipping_cost,totalofcart,ordertotal,orderid,orderdate,orderstatus,product_cod,subtotal,rowid,pincode,qty,f,options,order_size_id,order_color_id,order_size,order_color;
    public categoryitems(String order_id,String txnid, String date_added, String gross_total, String order_status,String totalofcart,String shipping_cost,String name,String paymentname,String shippingname,String invoice_no,String shipping_contact,String shipping_email,String shipping_address_1,String  shipping_address_2,String shipping_city,String shipping_state,String  shipping_pincode,String shipping_country,String shipping_address_same_as_billing_address,String delivery_date) {
        this.orderid=order_id;
        this.txnid=txnid;
        this.orderdate=date_added;
        this.ordertotal=gross_total;
        this.orderstatus=order_status;
        this.totalofcart=totalofcart;
        this.shipping_cost=shipping_cost;
        this.name=name;
        this.paymentname=paymentname;
        this.shippingname=shippingname;
        this.invoice_no=invoice_no;
        this.product_cod=shipping_contact;
        this.image_url=shipping_email;
        this.product_sub2category_id=shipping_address_1;
        this.product_sub3category_id=shipping_address_2;
        this.product_spc_id= shipping_city;
        this.product_description= shipping_state;
        this.pincode=shipping_pincode;
        this.product_category_id=shipping_country;
        this.id=shipping_address_same_as_billing_address;
        this.product_size=delivery_date;
       }
    public categoryitems() {
    }

    public categoryitems(String id, String review_title, String review, String rating) {
        this.orderid=id;
        this.product_name=review_title;
        this.req_bank_detail_status=review;
        this.ordertotal=rating;
    }

    public String getreq_reason() {
        return req_reason;
    }
    public void setreq_reason(String req_reason) {
        this.req_reason = req_reason;
    }
    public String getreq_bank_detail_status() {
        return req_bank_detail_status;
    }
    public void setreq_bank_detail_status(String req_bank_detail_status) {
        this.req_bank_detail_status = req_bank_detail_status;
    }
    public String getreq_from() {
        return req_from;
    }
    public void setreq_from(String req_from) {
        this.req_from = req_from;
    }
    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
    public String getstatus_by_admin() {
        return status_by_admin;
    }
    public void setstatus_by_admin(String status_by_admin) {
        this.status_by_admin = status_by_admin;
    }
    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getimage_url() {
        return image_url;
    }
    public void setimage_url(String image_url) {
        this.image_url = image_url;
    }
    public void setproduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getproduct_name() {
        return product_name;
    }
    public void setproduct_with_package(String product_with_package) {
        this.product_with_package = product_with_package;
    }
    public String getproduct_with_package() {
        return product_with_package;
    }
    public void setproduct_color(String product_color) {
        this.product_color = product_color;
    }
    public String getproduct_color() {
        return product_color;
    }
    public void setproduct_size(String product_size) {
        this.product_size = product_size;
    }
    public String getproduct_size() {
        return product_size;
    }
    public void setproduct_superimpose_image(String product_superimpose_image) {
        this.product_superimpose_image = product_superimpose_image;
    }
    public String getproduct_superimpose_image() {
        return product_superimpose_image;
    }
    public void setproduct_brand_id(String product_brand_id) {
        this.product_brand_id = product_brand_id;
    }
    public String getproduct_brand_id() {
        return product_brand_id;
    }
    public void setproduct_offer(String product_offer) {
        this.product_offer = product_offer;
    }
    public String getproduct_offer() {
        return product_offer;
    }
    public void setproduct_image(String product_image) {
        this.product_image = product_image;
    }
    public String getproduct_image() {
        return product_image;
    }
    public void setproduct_spc_id(String product_spc_id) {
        this.product_spc_id = product_spc_id;
    }
    public String getproduct_spc_id() {
        return product_spc_id;
    }
    public void setproduct_sub4category_id(String product_sub4category_id) {
        this.product_sub4category_id = product_sub4category_id;
    }
    public String getproduct_sub4category_id() {
        return product_sub4category_id;
    }
    public void setproduct_sub3category_id(String product_sub3category_id) {
        this.product_sub3category_id = product_sub3category_id;
    }
    public String getproduct_sub3category_id() {
        return product_sub3category_id;
    }
    public void setproduct_sub2category_id(String product_sub2category_id) {
        this.product_sub2category_id = product_sub2category_id;
    }
    public String getproduct_sub2category_id() {
        return product_sub2category_id;
    }
    public void setproduct_subcategory_id(String product_subcategory_id) {
        this.product_subcategory_id = product_subcategory_id;
    }
    public String getproduct_subcategory_id() {
        return product_subcategory_id;
    }
    public void setproduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
    }
    public String getproduct_category_id() {
        return product_category_id;
    }
    public void setproduct_tax(String product_tax) {
        this.product_tax = product_tax;
    }
    public String getproduct_tax() {
        return product_tax;
    }
    public void setproduct_tax_status(String product_tax_status) {
        this.product_tax_status = product_tax_status;
    }
    public String getproduct_tax_status() {
        return product_tax_status;
    }
    public void setproduct_shipping_cost(String product_shipping_cost) {
        this.product_shipping_cost = product_shipping_cost;
    }
    public String getproduct_shipping_cost() {
        return product_shipping_cost;
    }
    public void setproduct_free_shipping_limit(String product_free_shipping_limit) {
        this.product_free_shipping_limit = product_free_shipping_limit;
    }
    public String getproduct_free_shipping_limit() {
        return product_free_shipping_limit;
    }
    public void setproduct_free_shipping(String product_free_shipping) {
        this.product_free_shipping = product_free_shipping;
    }
    public String getproduct_free_shipping() {
        return product_free_shipping;
    }
    public void setproduct_substract_stock(String product_substract_stock) {
        this.product_substract_stock = product_substract_stock;
    }
    public String getproduct_substract_stock() {
        return product_substract_stock;
    }
    public void setproduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }
    public String getproduct_sku() {
        return product_sku;
    }
    public void setproduct_price(String product_price) {
        this.product_price = product_price;
    }
    public String getproduct_price() {
        return product_price;
    }
    public void setproduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
    public String getproduct_quantity() {
        return product_quantity;
    }
    public void setproduct_code(String product_code) {
        this.product_code = product_code;
    }
    public String getproduct_code() {
        return product_code;
    }
    public void setproduct_model(String product_model) {
        this.product_model = product_model;
    }
    public String getproduct_model() {
        return product_model;
    }
    public void setproduct_description(String product_description) {
        this.product_description = product_description;
    }
    public String getproduct_description() {
        return product_description;
    }
    public void setproduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getproduct_id() {
        return product_id;
    }
    public String getproduct_specification() {
        return product_specification;
    }
    public void setproduct_specification(String product_specification) {
        this.product_specification = product_specification;
    }
    public void setproduct_wishlistid(String product_wishlistid) {
        this.product_wishlistid = product_wishlistid;
    }
    public String getproduct_wishlistid() {
        return product_wishlistid;
    }
    public String getorderid() {
        return orderid;
    }
    public void setorderid(String orderid) {
        this.orderid = orderid;
    }
    public String gettxnid() {
        return txnid;
    }
    public void settxnid(String txnid) {
        this.txnid = txnid;
    }
    public void setorderdate(String orderdate) {
        this.orderdate = orderdate;
    }
    public String getorderstatus() {
        return orderstatus;
    }
    public void setorderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }
    public String getordertotal() {
        return ordertotal;
    }
    public void setordertotal(String ordertotal) {
        this.ordertotal = ordertotal;
    }
    public String getorderdate() {
        return orderdate;
    }
    @Override
    public String toString() {
        String a = null;
        try {
             a = getDate(orderdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            return orderid+a+ordertotal+orderstatus;
    }
    public String gettotalofcart() {
        return totalofcart;
    }
    public String getshipping_cost() {
        return shipping_cost;
    }
    public String getpaymentname() {
        return paymentname;
    }
    public String getshippingname() {
        return shippingname;
    }
    public void setshippingname(String shippingname) {
        this.shippingname = shippingname;
    }
    public String getinvoice_no() {
        return invoice_no;
    }
    public String getproduct_cod() {
        return product_cod;
    }
    public void setproduct_cod(String product_cod) {
        this.product_cod = product_cod;
    }
    public String getpincode() {
        return pincode;
    }
    public void setpincode(String pincode) {
        this.pincode = pincode;
    }
    public String getorderproductstatus() {
        return orderproductstatus;
    }
    public void setorderproductstatus(String orderproductstatus) {
        this.orderproductstatus = orderproductstatus;
    }
    public void setdiscount(String discount) {
        this.discount = discount;
    }
    public void setpo_discount(String po_discount) {
        this.po_discount = po_discount;
    }
    public String getpo_discount() {
        return po_discount;
    }
    public void setproduct_gst(String product_gst) {
        this.product_gst = product_gst;
    }
    public String getproduct_gst() {return product_gst; }
    public String getdiscount() {return discount; }
}
