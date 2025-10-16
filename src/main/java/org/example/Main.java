package org.example;

import org.example.application.LandingPage;

import java.io.File;

public class Main {
    static String localAppData = System.getenv("LOCALAPPDATA"); //get users %LocalAppData% path
    static File folderPath = new File(localAppData, "untitled-learnApp");

    public static void main(String[] args) {
        if (!folderPath.exists()) folderPath.mkdirs();
        new LandingPage();
    }

}