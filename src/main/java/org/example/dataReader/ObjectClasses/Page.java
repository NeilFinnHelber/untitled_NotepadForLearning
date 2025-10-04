package org.example.dataReader.ObjectClasses;

import java.util.ArrayList;
import java.util.List;

public class Page {
    String title = "untitled";
    String text;

    List<Header> headers;


    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
}
