package gui.register;

import Objects.DataAccess;
import gui.MainMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExistingInfo {
    private JFrame frame;
    private JTextField idNumField;
    private JButton confirmBtn;
    private JButton cancelBtn;
    private JPanel mainPanel;
    private JComboBox idTypeDropDown;
    private JTextField birthdateField;

    public ExistingInfo(DataAccess functions) {
        functions.setCon();
        idNumField.setDocument(new JTextFieldLimit(15));
        birthdateField.setDocument(new JTextFieldLimit(10));
        frame = new JFrame();
        frame.setTitle("Registration from Existing Entry");
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
        frame.setSize( 400, 290);
        frame.setLocationRelativeTo(null);

        try {
            ArrayList<String> idNameList = functions.getIdNameArray();
            idNameList.add(0, "Select ID Type");
            for (String s : idNameList) {
                idTypeDropDown.addItem(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        idTypeDropDown.getAccessibleContext().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                idNumField.setEnabled(idTypeDropDown.getSelectedItem() != "Select ID Type");
                birthdateField.setEnabled(idTypeDropDown.getSelectedItem() != "Select ID Type");
            }
        });

        birthdateField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkEntry(functions);
            }
        });

        idNumField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkEntry(functions);
            }
        });

        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               checkEntry(functions);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ExistingEntry(functions);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }

            }
        });

    }

    public void checkEntry(DataAccess functions){
        String idNumber, birthdate;
        int idType = 0, subsID;
        idNumber = idNumField.getText();
        birthdate = birthdateField.getText();
        try {
            idType = functions.getIdTypeFromIdName((String) idTypeDropDown.getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            subsID = functions.checkExistingSubscriber(idType, idNumber, birthdate);
            if (subsID == 0) {
                JOptionPane.showMessageDialog(null, "The application did not find a subscriber with that data.");
            } else {
                frame.dispose();
                new EnterMobileNumber(functions, subsID);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
