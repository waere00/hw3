package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TreeGUI extends JFrame {

    private JPanel buttonPanel;
    private JButton button1;
    private JButton button2;
    private JTextArea contentBox;
    private JButton button3;
    private JTextField textField3a;
    private JTextField textField3b;
    private JButton button4;
    private JTextField textField4a;
    private JTextField textField4b;
    private JButton button5;
    private JTextField textField5;
    private List<Tree> trees;

    public TreeGUI(Session session) {
        setTitle("Деревья");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());
        trees = TreeDao.read(session);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        for (Tree tree : trees) {
            JButton button = new JButton("Корень "+String.valueOf(tree.getRoot().getId()));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentBox.setText(tree.toString());
                }
            });
            buttonPanel.add(button);
        }
        contentBox = new JTextArea();
        contentBox.setEditable(false);

        button1 = new JButton("Загрузить из базы данных");
        button2 = new JButton("Записать в базу данных");
        button3 = new JButton("Добавить ребенка");
        button4 = new JButton("Добавить родителя");
        button5 = new JButton("Удалить узел");
        textField3a = new JTextField(15);
        textField3b = new JTextField(15);
        textField4a = new JTextField(15);
        textField4b = new JTextField(15);
        textField5 = new JTextField(15);


        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentBox), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(4, 3, 10, 10));
        bottomPanel.add(button1);
        bottomPanel.add(button2);
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(button3);
        bottomPanel.add(textField3a);
        bottomPanel.add(textField3b);
        bottomPanel.add(button4);
        bottomPanel.add(textField4a);
        bottomPanel.add(textField4b);
        bottomPanel.add(button5);
        bottomPanel.add(textField5);
        add(bottomPanel, BorderLayout.SOUTH);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trees = TreeDao.read(session);
                setVisible(false);
                remove(buttonPanel);
                remove(contentBox);
                remove(buttonPanel);
                buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                for (Tree tree : trees) {
                    JButton button = new JButton("Корень "+String.valueOf(tree.getRoot().getId()));
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            contentBox.setText(tree.toString());
                        }
                    });
                    buttonPanel.add(button);
                }
                add(buttonPanel, BorderLayout.NORTH);
                add(new JScrollPane(contentBox), BorderLayout.CENTER);
                add(bottomPanel, BorderLayout.SOUTH);
                setVisible(true);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeDao.write(trees, session);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int childId = Integer.parseInt(textField3a.getText());
                int parentId = Integer.parseInt(textField3b.getText());
                for (Tree tree : trees) {
                    tree.addChildById(parentId, childId);
                }
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int childId = Integer.parseInt(textField4a.getText());
                int parentId = Integer.parseInt(textField4b.getText());
                for (Tree tree : trees) {
                    tree.addParentById(childId, parentId);
                }
            }
        });
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nodeId = Integer.parseInt(textField5.getText());
                for (Tree tree : trees) {
                    tree.removeNodeById(nodeId);
                }
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SessionFactory factory = new Configuration().configure().buildSessionFactory();
                Session session = factory.openSession();
                TreeDao.populateDB(session);
                new TreeGUI(session);
            }
        });
    }
}
