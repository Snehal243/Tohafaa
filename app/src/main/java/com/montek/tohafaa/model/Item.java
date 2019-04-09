package com.montek.tohafaa.model;
public class Item {
    String varient_id,price,qty,phonecode,name,image,sc_spc_id;
    int id;
    public Item(String name,int id,String image,String sc_spc_id)
    {
        this.name=name;
        this.id=id;
        this.image=image;
        this.sc_spc_id=sc_spc_id;
    }
    public Item(int id, String name,String phonecode) {
        this.name=name;
        this.id=id;
        this.phonecode=phonecode;
    }
    public Item(int id, String name) {
        this.name=name;
        this.id=id;
    }
    public Item(String name, int id,String price,String varient_id,String qty ) {
        this.name=name;
        this.id=id;
        this.price=price;
        this.varient_id=varient_id;
        this.qty=qty;
    }
//    public void setqty(String qty) {
//        this.qty = qty;
//    }
    public String getqty()
    {
        return qty;
    }
    public String getprice()
    {
        return price;
    }
//    public void setprice(String price) {
//        this.price = price;
//    }
    public String getvarient_id()
    {
        return varient_id;
    }
//    public void setvarient_id(String varient_id) {
//        this.varient_id = varient_id;
//    }
    public String getName()
    {
        return name;
    }
    public int getid()
    {
        return id;
    }
    public String  getimage()
    {
        return image;
    }
    public void setName(String Name) {
        this.name = Name;
    }
//    public void setid(int id) {
//        this.id = id;
//    }
//    public void setsc_spc_id(String sc_spc_id) {
//        this.sc_spc_id = sc_spc_id;
//    }
    public String getsc_spc_id()
    {
        return sc_spc_id;
    }
//    public void setimage(String image) {
//        this.image = image;
//    }
    public String getphonecode()
    {
        return phonecode;
    }
//    public void setphonecode(String phonecode) {
//        this.phonecode = phonecode;
//    }
    @Override
    public String toString() {
        return name;
    }
}