package gui;

import Objects.DataAccess;
import gui.delete.DeleteSearchRegistration;
import gui.register.EnterMobileNumber;
import gui.register.ExistingEntry;
import gui.update.UpdateSearchRegistration;
import gui.view.ViewSearchRegistration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainMenu extends JFrame{
    private JButton registerBtn;
    private JButton deleteBtn;
    private JButton updateBtn;
    private JButton viewBtn;
    private JPanel mainPanel;
    private JLabel titleLbl;
    private JButton exitBtn;

    public MainMenu(DataAccess functions){
        functions.setCon();
        new JFrame();
        setTitle("Home");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 340, 290);
        setLocationRelativeTo(null);

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExistingEntry(functions);
                dispose();
            }
        });

        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewSearchRegistration(functions);
                dispose();
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateSearchRegistration(functions);
                dispose();
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteSearchRegistration(functions);
                dispose();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }
}

