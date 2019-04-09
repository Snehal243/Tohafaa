package com.montek.tohafaa.model;
public class CartModel {
    String customerid,estimated_days,discount,product_quantity,qty,id,subtotal,rowid,product_with_package,customer_pincode,order_color,superimpose_image,superimpose_text,name,price,src,product_shipping_cost,options,order_size_id,order_color_id,order_size,product_id;
    public CartModel(String id, String product_id,String product_quantity, String qty, String discount, String price, String name,
                     String src, String product_shipping_cost, String options, String order_size_id, String order_color_id,
                     String order_size, String order_color, String superimpose_image, String superimpose_text, String customer_pincode, String product_with_package, String rowid, String subtotal,String estimated_days) {
        this.id = id;
        this.estimated_days = estimated_days;
        this.name = name;
        this.product_id = product_id;
        this.discount = discount;
        this.qty = qty;
        this.price = price;
        this.src = src;
        this.product_shipping_cost = product_shipping_cost;
        this.options = options;
        this.order_size_id = order_size_id;
        this.order_color_id = order_color_id;
        this.order_size = order_size;
        this.order_color = order_color;
        this.superimpose_image = superimpose_image;
        this.superimpose_text = superimpose_text;
        this.customer_pincode = customer_pincode;
        this.product_with_package = product_with_package;
        this.rowid = rowid;
        this.subtotal = subtotal;
        this.product_quantity = product_quantity;
    }
    public CartModel() {
    }
    public String getestimated_days() {
        return estimated_days;
    }
    public void setestimated_days(String estimated_days) {
        this.estimated_days = estimated_days;
    }
    public String getproduct_quantity() {
        return product_quantity;
    }
    public void setproduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
    public String getcustomer_pincode() {
        return customer_pincode;
    }
    public void setcustomer_pincode(String customer_pincode) {
        this.customer_pincode = customer_pincode;
    }
    public String getproduct_with_package() {
        return product_with_package;
    }
    public void setproduct_with_package(String product_with_package) {
        this.product_with_package = product_with_package;
    }
    public String getrowid() {
        return rowid;
    }
    public void setrowid(String rowid) {
        this.rowid = rowid;
    }
    public String getsubtotal() {
        return subtotal;
    }
    public void setsubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
    public String getorder_color_id() {
        return order_color_id;
    }
    public void setorder_color_id(String order_color_id) {
        this.order_color_id = order_color_id;
    }
    public String getorder_size() {
        return order_size;
    }
    public void setorder_size(String order_size) {
        this.order_size = order_size;
    }
    public String getorder_color() {
        return order_color;
    }
    public void setorder_color(String order_color) {
        this.order_color = order_color;
    }
    public String getsuperimpose_image() {
        return superimpose_image;
    }
    public void setsuperimpose_image(String superimpose_image) {
        this.superimpose_image = superimpose_image;
    }
    public String getsuperimpose_text() {
        return superimpose_text;
    }
    public void setsuperimpose_text(String superimpose_text) {
        this.superimpose_text = superimpose_text;
    }
    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }
    public String getsrc() {
        return src;
    }
    public void setsrc(String src) {
        this.src = src;
    }
    public String getproduct_shipping_cost() {
        return product_shipping_cost;
    }
    public void setproduct_shipping_cost(String product_shipping_cost) {
        this.product_shipping_cost = product_shipping_cost;
    }
    public String getoptions() {
        return options;
    }
    public void setoptions(String options) {
        this.options = options;
    }
    public String getorder_size_id() {
        return order_size_id;
    }
    public void setorder_size_id(String order_size_id) {
        this.order_size_id = order_size_id;
    }
    public String getId() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getproduct_id() {
        return product_id;
    }
    public void setproduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getdiscount() {
        return discount;
    }
    public void setdiscount(String discount) {
        this.discount = discount;
    }
    public String getqty() {
        return qty;
    }
    public void setqty(String qty) {
        this.qty = qty;
    }
    public void setcustomerid(String customerid) {
        this.customerid = customerid;
    }
}
