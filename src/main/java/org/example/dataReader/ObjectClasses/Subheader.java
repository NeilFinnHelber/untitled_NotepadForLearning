package org.example.dataReader.ObjectClasses;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class Subheader {

    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title = "";

    @JacksonXmlProperty(localName = "text")
    private String text;

    @JacksonXmlElementWrapper(localName = "searchTerms")
    @JacksonXmlProperty(localName = "searchTerm")
    private List<String> searchTerm;

    public Subheader(){}



    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(List<String> searchTerm) {
        this.searchTerm = searchTerm;
    }
}
