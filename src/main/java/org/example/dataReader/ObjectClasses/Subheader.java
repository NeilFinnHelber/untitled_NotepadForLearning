package org.example.dataReader.ObjectClasses;

import java.util.ArrayList;

public class Subheader {
    String title;
    ArrayList<String> searchTerms;
    String text;

    public Subheader(){}
    public Subheader(String title, ArrayList<String> searchTerms, String text) {
        super();
        this.title = title;
        this.searchTerms = searchTerms;
        this.text = text;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(ArrayList<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
