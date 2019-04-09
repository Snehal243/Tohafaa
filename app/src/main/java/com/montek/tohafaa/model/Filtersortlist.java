package com.montek.tohafaa.model;

public class Filtersortlist {
    String sorting_title = null;
    String image = null;
    public Filtersortlist(int id , String sorting_title, String image) {
        super();
        this.sorting_title = sorting_title;
        this.image = image;
    }
    public String getsorting_title() {
        return sorting_title;
    }
    public void setsorting_title(String sorting_title) {
        this.sorting_title = sorting_title;
    }
    public String getimage() {
        return image;
    }
    public void setimage(String image) {
        this.image = image;
    }
    @Override
    public String toString() {
        return  sorting_title ;
    }
}