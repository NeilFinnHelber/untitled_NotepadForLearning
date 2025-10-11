package org.example.application;

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
import java.util.List;

public class PageView extends JFrame implements ActionListener {
    JPanel mainHeaderPanel = new JPanel();
    JPanel mainPageTextPanel = new JPanel();
    JPanel mainPageTitlePanel = new JPanel();
    JPanel treeNodePanel = new JPanel();

    JScrollPane treeScrollPane;

    JButton createHeaderButton;
    JButton createSubheaderButton;

    Page page;
    List<Header> headers;
    List<Subheader> subheaders;
    Reader reader = new Reader();
    Writer writer = new Writer();

    JTextField pageTitle = new JTextField();
    JTextArea pageText = new JTextArea();

    JTextField headerTitle = new JTextField();
    JTextField subheaderTitle = new JTextField();


    JTextArea headerText;
    JTextArea subheaderText;

    File file_fromFilePath;


    public PageView(File file_fromFilePath) {
        this.file_fromFilePath = file_fromFilePath;

        this.setTitle("title of page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 500);


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


        page = reader.readDataFromXMLFile(file_fromFilePath);
        headers = reader.getAllHeadersFromPage(file_fromFilePath);

        JLabel headerTitleLabel;
        JLabel headerTextLabel;

        JLabel subheaderTitleLabel;
        JLabel subheaderTextLabel;

        for (Header header : headers) {

            headerTitleLabel = new JLabel("Title:");
            headerTitle = new JTextField(header.getTitle(), 20);

            headerTextLabel = new JLabel("Description:");
            headerText = new JTextArea(header.getText(), (header.getText().length() / 55) + 2, 20);
            headerText.setLineWrap(true);
            headerText.setWrapStyleWord(true);


            JScrollPane scrollPane = new JScrollPane(headerText);


            JPanel headerPanel = new JPanel();

            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


            headerPanel.add(headerTitleLabel);
            headerPanel.add(headerTitle);
            headerPanel.add(Box.createVerticalStrut(5));
            headerPanel.add(headerTextLabel);
            headerPanel.add(scrollPane);


            for (Subheader subheader : header.getSubheaders()) {
                JPanel subheaderPanel = new JPanel();
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

                subheaderPanel.add(subheaderTitleLabel);
                subheaderPanel.add(subheaderTitle);
                subheaderPanel.add(Box.createVerticalStrut(5));
                subheaderPanel.add(subheaderTextLabel);
                subheaderPanel.add(subheaderText);

                headerPanel.add(subheaderPanel);
            }
            createSubheaderButton = new JButton("Create Subheader");
            String pageFileName = file_fromFilePath.getName().substring(0, file_fromFilePath.getName().length() - 4);
            createSubheaderButton.addActionListener(e -> {

                String input = JOptionPane.showInputDialog("Enter Subheader Title");

                if (input != null) {
                    if (input.isBlank()) {
                        JOptionPane.showMessageDialog(null, "wont create subheader without title", "warning", JOptionPane.WARNING_MESSAGE);
                    } else {
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createHeaderButton) {


            String input = JOptionPane.showInputDialog("please type in a title for the header");
            if (input != null) {
                if (input.isBlank()) {
                    JOptionPane.showMessageDialog(null, "wont create header without title", "warning", JOptionPane.WARNING_MESSAGE);//if the input is "" BLANK, nothing shall be created
                } else {

                    String pageFileName = file_fromFilePath.getName().substring(0, file_fromFilePath.getName().length() - 4); //removes .xml from the fileName
                    System.out.println(pageFileName);
                    writer.writeToFile_createHeader(pageFileName, input);

                    //reload the panels
                    load_reload_ExpandablePanel(file_fromFilePath);
                    load_reload_mainPanel(file_fromFilePath);


                }
            }
        }
    }

    public static void main(String[] args) {
        Reader reader = new Reader();
        String folder = reader.file_folderUnderFullPath.getAbsolutePath();

        File file = new File(folder, "testingEdits.xml");
        PageView view = new PageView(file);
    }
}
