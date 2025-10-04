package org.example.dataReader.ObjectClasses;

import java.util.ArrayList;
import java.util.List;

public class Header {
    String title = "";
    ArrayList<String> searchTerms;

    String text;

    List<Subheader> subheaders;


    public Header(){}
    public Header(String title, ArrayList<String> searchTerms, String text, List<Subheader> subheaders) {
        super();
        this.title = title;
        this.searchTerms = searchTerms;
        this.text = text;
        this.subheaders = subheaders;
    }


    public String getTitle() {
        return title;
    }

    public ArrayList<String> getSearchTerms() {
        return searchTerms;
    }

    public String getText() {
        return text;
    }

    public List<Subheader> getSubheaders() {
        return subheaders;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSearchTerms(ArrayList<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubheaders(List<Subheader> subheaders) {
        this.subheaders = subheaders;
    }
}
