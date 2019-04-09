package com.montek.tohafaa.model;

public  class TempClass {
    public String customerid,slectedqty;
    public String product_id,SameasShippingAdress,contry,firstname,email,state,lastname,mobile,address1,adress2,city;
    public TempClass(String customerid,String product_id,String firstname,String lastname,String email,String mobile,String address1,String city,String contry,String state,String SameasShippingAdress) {
        this.customerid=customerid;
        this.contry=contry;
        this.state=state;
        this.product_id=product_id;
        this.SameasShippingAdress=SameasShippingAdress;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.mobile=mobile;
        this.address1=address1;
        this.adress2=adress2;
        this.city=city;
    }
    public TempClass(String product_id, String slectedqty) {
        this.product_id=product_id;
        this.slectedqty=slectedqty;
    }
    @Override
    public String toString() {
        return product_id;
    }
}
