package com.example.kiarx_test;

public class HeaderItem extends ListItem {
    private String date;

    public HeaderItem(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    int getType() {
        return TYPE_HEADER;
    }
}
