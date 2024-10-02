package com.abby.ui;

public class TreeString implements TreeDisplayable {
    private String text; 

    public TreeString(String t) {
        this.text = t;
    }

    public String getText() {
        return this.text;
    }
}
