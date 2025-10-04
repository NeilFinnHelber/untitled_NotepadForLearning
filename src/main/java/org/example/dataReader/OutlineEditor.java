package org.example.dataReader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.awt.*;
import java.io.*;
import java.util.*;

public class OutlineEditor {

    private static JTextArea textArea;
    private static JTree tree;
    private static DefaultTreeModel treeModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OutlineEditor::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Outline with Folding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton parseButton = new JButton("Parse & Show Tree");
        JButton saveButton = new JButton("Save as XML");

        // Tree view
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("document");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        JScrollPane treeScrollPane = new JScrollPane(tree);

        parseButton.addActionListener(e -> {
            String input = textArea.getText();
            DefaultMutableTreeNode newRoot = parseOutlineToTree(input);
            treeModel.setRoot(newRoot);
            expandAll(tree, true); // expand all initially
        });

        saveButton.addActionListener(e -> {
            try {
                Element root = parseOutlineToXML(textArea.getText());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                doc.appendChild(doc.importNode(root, true));

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                JFileChooser chooser = new JFileChooser();
                if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    transformer.transform(new DOMSource(doc), new StreamResult(file));
                    JOptionPane.showMessageDialog(frame, "Saved to " + file.getAbsolutePath());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(parseButton);
        buttonPanel.add(saveButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, treeScrollPane);
        splitPane.setDividerLocation(400);

        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Parse the outline text into a Swing tree model
     */
    private static DefaultMutableTreeNode parseOutlineToTree(String input) {
        Stack<DefaultMutableTreeNode> stack = new Stack<>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("document");
        stack.push(root);

        StringTokenizer tokenizer = new StringTokenizer(input, "{}\n", true);
        DefaultMutableTreeNode current = null;

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            switch (token) {
                case "{":
                    if (current != null) {
                        stack.push(current);
                        current = null;
                    }
                    break;
                case "}":
                    if (stack.size() > 1) {
                        stack.pop();
                    }
                    break;
                default:
                    DefaultMutableTreeNode section = new DefaultMutableTreeNode(token);
                    stack.peek().add(section);
                    current = section;
                    break;
            }
        }
        return root;
    }

    /**
     * Parse the outline into XML structure
     */
    private static Element parseOutlineToXML(String input) throws ParserConfigurationException {
        Stack<Element> stack = new Stack<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("document");
        stack.push(root);

        StringTokenizer tokenizer = new StringTokenizer(input, "{}\n", true);
        Element current = null;

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            switch (token) {
                case "{":
                    if (current != null) {
                        stack.push(current);
                        current = null;
                    }
                    break;
                case "}":
                    if (stack.size() > 1) {
                        stack.pop();
                    }
                    break;
                default:
                    Element section = doc.createElement("section");
                    section.setAttribute("title", token);
                    stack.peek().appendChild(section);
                    current = section;
                    break;
            }
        }
        return root;
    }

    /** Expand or collapse all tree nodes */
    private static void expandAll(JTree tree, boolean expand) {
        int row = 0;
        while (row < tree.getRowCount()) {
            if (expand) {
                tree.expandRow(row);
            } else {
                tree.collapseRow(row);
            }
            row++;
        }
    }
}
