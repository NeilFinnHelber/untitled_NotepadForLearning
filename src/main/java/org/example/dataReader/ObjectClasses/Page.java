package org.example.dataReader.ObjectClasses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class Page {

    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title = "untitled";
    
    @JacksonXmlProperty(localName = "text")
    private String text;

    @JacksonXmlElementWrapper(localName = "Headers")
    @JacksonXmlProperty(localName = "Header")
    private List<Header> header = new ArrayList<>();
    
    public Page() {}




    public List<Header> getHeaders() {
        return header;
    }

    public void setHeaders(List<Header> headers) {
        this.header = headers;
    }


    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
}
