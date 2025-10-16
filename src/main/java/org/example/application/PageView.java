package org.example.application;

import org.example.application.components.MenuBarClass;
import org.example.dataReader.ObjectClasses.Header;
import org.example.dataReader.ObjectClasses.Page;
import org.example.dataReader.ObjectClasses.Subheader;
import org.example.dataReader.Reader;
import org.example.dataReader.Writer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageView extends JFrame implements ActionListener {
    JPanel mainHeaderPanel = new JPanel();
    JPanel mainPageTextPanel = new JPanel();
    JPanel mainPageTitlePanel = new JPanel();
    JPanel treeNodePanel = new JPanel();

    JScrollPane treeScrollPane;

    JButton createHeaderButton;
    JButton removeSubheaderButton;
    JButton removeHeaderButton;

    JButton createSubheaderButton;

    Page page;
    public List<Header> headers;
    List<Subheader> subheaders;
    Reader reader = new Reader();
    Writer writer = new Writer();

    JTextField pageTitle = new JTextField();
    JTextArea pageText = new JTextArea();

    JTextField headerTitle = new JTextField();
    JTextField subheaderTitle = new JTextField();


    JTextArea headerText;
    JTextArea subheaderText;

    public File file_fromFilePath;

    private String mainPageTitle = "";
    private String mainPageText = "";

    private final List<JTextField> allHeaderTitleFields = new ArrayList<>();
    private final List<JTextArea> allHeaderTextAreas = new ArrayList<>();

    private final List<JTextField> allSubheaderTitleFields = new ArrayList<JTextField>();
    private final List<JTextArea> allSubheaderTextAreas = new ArrayList<JTextArea>();




    public PageView(File file_fromFilePath) {
        this.file_fromFilePath = file_fromFilePath;

        String pageName = file_fromFilePath.getName().substring(0, file_fromFilePath.getName().length() - 4);
        this.setTitle(pageName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 550);

        /*
        allListsOfAres_and_fields.add(pageTitleField);
        allListsOfAres_and_fields.add(pageTextArea);
        allListsOfAres_and_fields.add(allHeaderTitleFields);
        allListsOfAres_and_fields.add(allHeaderTextAreas);
        allListsOfAres_and_fields.add(allSubheaderTitleFields);
        allListsOfAres_and_fields.add(allSubheaderTextAreas);
        */


        JScrollPane expandableHeaders_subheadersScrollPane = new JScrollPane(
                treeNodePanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainHeaderPanel.setBackground(Color.BLUE);
        mainHeaderPanel.setLayout(new GridLayout(0, 1, 0, 19)); //sets height between elements
        //treeNodePanel.setBackground(Color.GREEN);


        expandableHeaders_subheadersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        expandableHeaders_subheadersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        expandableHeaders_subheadersScrollPane.setPreferredSize(new Dimension(300, 80));

        load_reload_ExpandablePanel(file_fromFilePath);
        load_reload_mainPanel(file_fromFilePath);

        JScrollPane mainScrollPane = new JScrollPane(
                mainHeaderPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        page = reader.readDataFromXMLFile(file_fromFilePath);
        pageTitle.setText(page.getTitle());
        pageText.setText(page.getText());

        mainPageTitle = page.getTitle();
        mainPageText = page.getText();


        MenuBarClass menuBarClass = new MenuBarClass();
        this.setJMenuBar(menuBarClass.menuBarFromClass(null, this));

        this.setLayout(new BorderLayout());

        mainPageTitlePanel.add(pageTitle);

        mainPageTextPanel.setLayout(new BorderLayout());
        mainPageTextPanel.add(mainPageTitlePanel, BorderLayout.NORTH);
        mainPageTextPanel.add(pageText, BorderLayout.CENTER);


        this.add(mainPageTextPanel, BorderLayout.NORTH);
        this.add(mainScrollPane, BorderLayout.CENTER);
        this.add(expandableHeaders_subheadersScrollPane, BorderLayout.EAST);


        this.setVisible(true);
    }

    public void load_reload_ExpandablePanel(File file_fromFilePath) {
        treeNodePanel.removeAll();

        page = reader.readDataFromXMLFile(file_fromFilePath);
        headers = reader.getAllHeadersFromPage(file_fromFilePath);


        DefaultMutableTreeNode root_node = new DefaultMutableTreeNode(page.getTitle()); //setting the top part with the name of the page

        DefaultTreeModel treeModel = new DefaultTreeModel(root_node);
        JTree tree = new JTree(treeModel);

        for (Header header : headers) { //sets each header
            DefaultMutableTreeNode header_node = new DefaultMutableTreeNode(header.getTitle()); //sets each headers title

            for (Subheader subheader : header.getSubheaders()) {
                header_node.add(new DefaultMutableTreeNode(subheader.getTitle()));
            }
            root_node.add(header_node); //add the tree node to the original tree "pane", then down to the actual pane
        }

        treeModel.reload();

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        treeNodePanel.setLayout(new BorderLayout());
        treeNodePanel.add(treeScrollPane = new JScrollPane(tree), BorderLayout.NORTH);
        JPanel rootNodeButtonPanel = new JPanel();

        createHeaderButton = new JButton("Create Header");
        createHeaderButton.addActionListener(this);



        //createSubheaderButton = new JButton("Create Subheader");
        //createSubheaderButton.addActionListener(this);

        rootNodeButtonPanel.add(createHeaderButton);

        //rootNodeButtonPanel.add(createSubheaderButton);

        treeNodePanel.add(rootNodeButtonPanel, BorderLayout.WEST);
        treeNodePanel.revalidate();
        treeNodePanel.repaint();

    }


    public void load_reload_mainPanel(File file_fromFilePath) {
        mainHeaderPanel.removeAll();
        mainPageTitle = "";
        mainPageText = "";
        allHeaderTitleFields.clear();
        allHeaderTextAreas.clear();
        allSubheaderTitleFields.clear();
        allSubheaderTextAreas.clear();


        page = reader.readDataFromXMLFile(file_fromFilePath);
        headers = reader.getAllHeadersFromPage(file_fromFilePath);

        JLabel headerTitleLabel;
        JLabel headerTextLabel;

        JLabel subheaderTitleLabel;
        JLabel subheaderTextLabel;

        for (Header header : headers) {
            removeHeaderButton = new JButton("delete header bellow");

            headerTitleLabel = new JLabel("Title:");
            headerTitle = new JTextField(header.getTitle(), 20);

            headerTextLabel = new JLabel("Description:");
            headerText = new JTextArea(header.getText(), (header.getText().length() / 55) + 2, 20);
            headerText.setLineWrap(true);
            headerText.setWrapStyleWord(true);


            JScrollPane scrollPane = new JScrollPane(headerText);


            JPanel headerPanel = new JPanel();
            headerPanel.add(removeHeaderButton, BorderLayout.EAST);

            final String pageFileName = file_fromFilePath.getName().substring(0, file_fromFilePath.getName().length() - 4);
            final int headerIndex = headers.indexOf(header);

            removeHeaderButton.addActionListener(e -> {
                int input = JOptionPane.showConfirmDialog(this, "do you want to remove the header from the page?", "removes header: " + header.getTitle(), JOptionPane.YES_NO_OPTION);
                if (input == JOptionPane.YES_OPTION) {
                    writer.writeToFile_removeHeader(pageFileName, headerIndex);

                    load_reload_mainPanel(file_fromFilePath);
                    load_reload_ExpandablePanel(file_fromFilePath);

                    JOptionPane.showMessageDialog(null, "Header removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }

            });


            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


            headerPanel.add(headerTitleLabel);
            headerPanel.add(headerTitle);
            headerPanel.add(Box.createVerticalStrut(5));
            headerPanel.add(headerTextLabel);
            headerPanel.add(scrollPane);


            for (Subheader subheader : header.getSubheaders()) {
                removeSubheaderButton = new JButton("delete subheader bellow ");

                JPanel subheaderPanel = new JPanel();
                subheaderPanel.add(removeSubheaderButton, BorderLayout.EAST);
                subheaderPanel.setLayout(new BoxLayout(subheaderPanel, BoxLayout.Y_AXIS));
                subheaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                subheaderTitleLabel = new JLabel("Subheader Title:");
                subheaderTitle = new JTextField(subheader.getTitle(), 20);

                subheaderTextLabel = new JLabel("Subheader Description:");
                int rowsize = 4;
                if (subheader.getText() != null) rowsize = (subheader.getText().length() / 55) + 2;


                subheaderText = new JTextArea(subheader.getText(), rowsize, 20);
                subheaderText.setLineWrap(true);
                subheaderText.setWrapStyleWord(true);

                int subheaderIndex = header.getSubheaders().indexOf(subheader); //the position of the subheader

                subheaderText.putClientProperty("headerID", headerIndex);
                subheaderText.putClientProperty("subheaderID", subheaderIndex);
                subheaderTitle.putClientProperty("headerID", headerIndex);
                subheaderTitle.putClientProperty("subheaderID", subheaderIndex);

                allSubheaderTextAreas.add(subheaderText);
                allSubheaderTitleFields.add(subheaderTitle);

                



                removeSubheaderButton.addActionListener(e -> {
                    int input = JOptionPane.showConfirmDialog(this, "do you want to remove the subheader from the page?", "removes subheader: " + subheader.getTitle(), JOptionPane.YES_NO_OPTION);
                    if (input == JOptionPane.YES_OPTION) {
                        writer.writeToFile_removeSubheader(pageFileName, headerIndex, subheaderIndex);

                        load_reload_mainPanel(file_fromFilePath);
                        load_reload_ExpandablePanel(file_fromFilePath);

                        JOptionPane.showMessageDialog(null, "Subheader removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }

                });


                subheaderPanel.add(subheaderTitleLabel);
                subheaderPanel.add(subheaderTitle);
                subheaderPanel.add(Box.createVerticalStrut(5));
                subheaderPanel.add(subheaderTextLabel);
                subheaderPanel.add(new JScrollPane(subheaderText));

                headerPanel.add(subheaderPanel);
            }
            headerTitle.putClientProperty("headerID", headers.indexOf(header));
            headerText.putClientProperty("headerID", headers.indexOf(header));

            allHeaderTitleFields.add(headerTitle);
            allHeaderTextAreas.add(headerText);

            


            createSubheaderButton = new JButton("Create Subheader");

            createSubheaderButton.addActionListener(e -> {

                String input = JOptionPane.showInputDialog("Enter Subheader Title");

                if (input != null) {
                    if (input.isBlank()) {
                        JOptionPane.showMessageDialog(null, "wont create subheader without title", "warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        saveDataToPage();

                        System.out.println("create subheader for header " + header.getTitle());
                        System.out.println(header);
                        writer.writeToFile_createSubheader_usingHeader(pageFileName, header, input);

                        load_reload_mainPanel(file_fromFilePath);
                        load_reload_ExpandablePanel(file_fromFilePath);
                    }

                }
            }); //create subheader for the header

            headerPanel.add(createSubheaderButton);
            mainHeaderPanel.add(headerPanel);

        }
        mainHeaderPanel.revalidate();
        mainHeaderPanel.repaint();

        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();
        Object[] keys = actionMap.keys();

        if (keys == null || java.util.Arrays.stream(keys).noneMatch(k -> k.equals("updateComponentsOfPage"))) {
            inputMap.put(KeyStroke.getKeyStroke("control S"), "updateComponentsOfPage"); //use control s to invoke "updateComponentsOfPage"
            actionMap.put("updateComponentsOfPage", new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    saveDataToPage();
                    JOptionPane.showMessageDialog(null, "info", "file has been saved!", JOptionPane.INFORMATION_MESSAGE);

                }
            });
        }
    }

    public void saveDataToPage() {


        String pageFileName = file_fromFilePath.getName();
        pageFileName = pageFileName.substring(0, pageFileName.length() - 4);

        int headerID, subheaderID;

        for (JTextArea subheaderTextArea : allSubheaderTextAreas) {
            headerID = (int) subheaderTextArea.getClientProperty("headerID");
            subheaderID = (int) subheaderTextArea.getClientProperty("subheaderID");

            Header header = headers.get(headerID);
            Subheader subheader = header.getSubheaders().get(subheaderID);


            String subheaderText = subheaderTextArea.getText();
            ArrayList<String> searchTerms = (ArrayList<String>) subheader.getSearchTerm();

            writer.writeToFile_updateSubheader(pageFileName, headerID, subheaderID, null, subheaderText, searchTerms);
        }
        for (JTextField subheaderTitleField : allSubheaderTitleFields) {
            headerID = (int) subheaderTitleField.getClientProperty("headerID");
            subheaderID = (int) subheaderTitleField.getClientProperty("subheaderID");

            Header header = headers.get(headerID);
            Subheader subheader = header.getSubheaders().get(subheaderID);

            String subheaderTitle = subheaderTitleField.getText();
            ArrayList<String> searchTerms = (ArrayList<String>) subheader.getSearchTerm();

            writer.writeToFile_updateSubheader(pageFileName, headerID, subheaderID, subheaderTitle, null, searchTerms);
        }
        for (JTextArea headerTextArea : allHeaderTextAreas) {
            headerID = (int) headerTextArea.getClientProperty("headerID");

            Header header = headers.get(headerID);


            String headerText = headerTextArea.getText();
            ArrayList<String> searchTerms = (ArrayList<String>) header.getSearchTerm();

            writer.writeToFile_updateHeader(pageFileName, headerID, null, headerText, searchTerms);
        }
        for (JTextField headerTitleField : allHeaderTitleFields) {
            headerID = (int) headerTitleField.getClientProperty("headerID");

            Header header = headers.get(headerID);

            String headerTitle = headerTitleField.getText();

            ArrayList<String> searchTerms = (ArrayList<String>) header.getSearchTerm();

            writer.writeToFile_updateHeader(pageFileName, headerID, headerTitle, null, searchTerms);
        }


        writer.writeToFile_updatePage(pageFileName, mainPageTitle, mainPageText);


        System.out.println("saved page");
    }

    public int checkIfAlreadySaved() {
        /// check if a file has already been saved
            int choiceInput = JOptionPane.showConfirmDialog(this, "do you want to save before closing?", "save the document?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choiceInput == JOptionPane.YES_OPTION) {
                System.out.println("save before closing");
                saveDataToPage();
                return JOptionPane.YES_OPTION;
            } else if (choiceInput == JOptionPane.NO_OPTION) return JOptionPane.NO_OPTION;

            return JOptionPane.CANCEL_OPTION;
    }

public void createHeader() {
    String pageFileName = file_fromFilePath.getName().substring(0, file_fromFilePath.getName().length() - 4); //removes .xml from the fileName
    page = reader.readDataFromXMLFile(file_fromFilePath);
    headers = page.getHeaders();

    String input = JOptionPane.showInputDialog("please type in a title for the header");
    if (input != null) {
        if (input.isBlank()) {
            JOptionPane.showMessageDialog(null, "wont create header without title", "warning", JOptionPane.WARNING_MESSAGE);//if the input is "" BLANK, nothing shall be created
        } else {

            System.out.println(pageFileName);
            saveDataToPage();

            writer.writeToFile_createHeader(pageFileName, input);

            //reload the panels
            load_reload_ExpandablePanel(file_fromFilePath);
            load_reload_mainPanel(file_fromFilePath);


        }
    }
}


    @Override
    public void actionPerformed(ActionEvent e) {
        String pageFileName = file_fromFilePath.getName().substring(0, file_fromFilePath.getName().length() - 4); //removes .xml from the fileName
        page = reader.readDataFromXMLFile(file_fromFilePath);
        headers = page.getHeaders();

        if (e.getSource() == createHeaderButton) {
            createHeader();
        }

    }

    public static void main(String[] args) {
        Reader reader = new Reader();
        String folder = reader.file_folderUnderFullPath.getAbsolutePath();

        File file = new File(folder, "testingEdits.xml");
        PageView view = new PageView(file);
    }
}
