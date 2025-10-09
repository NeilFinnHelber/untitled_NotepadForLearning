package org.example.dataReader;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reader {
    public String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
    public File file_folderUnderFullPath = new File(localAppData, "untitled-learnApp");
    List<File> filesInDirectory = new ArrayList<File>();

    List<Page> pagesInDirectory = new ArrayList<Page>();
    List<Header> headersInDirectory = new ArrayList<Header>();
    List<Subheader> subheadersInDirectory = new ArrayList<Subheader>();

    XmlMapper mapper = new XmlMapper();
    InputStream inputStream;
    TypeReference<ArrayList<Page>> typeReferenceForPage = new TypeReference<ArrayList<Page>>() {};

public Reader (){
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
}

    public List<File> getAllFilesInPageDirectory() {
        filesInDirectory.clear();
        if (file_folderUnderFullPath.exists() && file_folderUnderFullPath.isDirectory()) {
            File[] files = file_folderUnderFullPath.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filesInDirectory.add(file);
                    }
                }
                return filesInDirectory;
            }
        } else {
            System.out.println("No Directory under this Path " + file_folderUnderFullPath.getAbsolutePath());
        }
        return null;
    }

    public List<List<?>> getAllHeaders_SubheadersInPageDirectory(String fullPathToFile) {
        headersInDirectory.clear();
        subheadersInDirectory.clear();
        pagesInDirectory.clear();

        file_folderUnderFullPath = new File(localAppData, "untitled-learnApp/" + fullPathToFile);
        if (file_folderUnderFullPath.exists()) {
            try (InputStream input = new FileInputStream(file_folderUnderFullPath)) {

                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                

                Page page = mapper.readValue(input, Page.class);
                

                if (page != null) {
                    pagesInDirectory.add(page);
                    

                    if (page.getHeaders() != null) {
                        headersInDirectory.addAll(page.getHeaders());
                        System.out.println(page.getTitle());
                        System.out.println(page.getHeaders());

                        for (Header header : page.getHeaders()) {
                            System.out.println(header.getSubheaders());
                            subheadersInDirectory.addAll(header.getSubheaders());
                        }
                    }

                }
                List<List<?>> combinedList = new ArrayList<>();
                combinedList.add(headersInDirectory);
                combinedList.add(subheadersInDirectory);

                return combinedList;
                
            } catch (IOException e) {
                System.err.println("Error reading XML file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("File not found: " + file_folderUnderFullPath.getAbsolutePath());
        }
        return Collections.emptyList();
    }

    Page readDataFromXMLFile(File file_fromFilePath){

        try{
            Page page;

            if (file_fromFilePath.exists()) {
                page = mapper.readValue(file_fromFilePath, Page.class);
                return page;
            }
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Page();
    }

    public static void main(String[] args) {
        Reader reader = new Reader();
        reader.getAllHeaders_SubheadersInPageDirectory("title.xml");
    }
}


