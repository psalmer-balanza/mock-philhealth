package gui.register;

import Objects.DataAccess;
import Objects.Database.Sim;
import gui.MainMenu;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class JTextFieldLimit extends PlainDocument {
    private int limit;
    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null)
            return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}

public class EnterMobileNumber extends JFrame{
    private JPanel mainPanel;
    private JTextField simNumberTextArea;
    private JButton confirmBtn;
    private JButton backBtn;
    private JLabel titleLbl;

    public EnterMobileNumber(DataAccess functions) {
        functions.setCon();
        simNumberTextArea.setDocument(new JTextFieldLimit(11));
        new JFrame();
        setTitle("SIM Number");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 350, 200);
        setLocationRelativeTo(null);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ExistingEntry(functions);
            }
        });

        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateSim(functions);
            }
        });

        simNumberTextArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateSim(functions);
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

    public EnterMobileNumber(DataAccess functions, int subsID) {
        simNumberTextArea.setDocument(new JTextFieldLimit(11));
        new JFrame();
        setTitle("SIM Number");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 350, 200);
        setLocationRelativeTo(null);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ExistingInfo(functions);
            }
        });

        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateSim(functions, subsID);
            }
        });

        simNumberTextArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateSim(functions, subsID);
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

    public void validateSim(DataAccess functions){
        String simNo = simNumberTextArea.getText().replaceAll(" ", "");
        if (simNo.equals("")) {
            JOptionPane.showMessageDialog(null,"Sim number field should not be blank");
        } else if (simNo.charAt(1) != '9' || simNo.charAt(0) != '0') {
            JOptionPane.showMessageDialog(null, "Please enter a valid mobile number. eg. 09279960955");
        } else if(functions.checkIfSimnoIsRegistered(simNumberTextArea.getText())) {
            JOptionPane.showMessageDialog(null, "This sim number is already registered! " +
                    "Please try again");
        } else {
            try {
                //limit this to only allow 11 characters of input in the field
                if(simNo.length() != 11){
                    JOptionPane.showMessageDialog(null, "Please enter a valid mobile number (11 digits). eg. 09279960955");
                    simNumberTextArea.setText(null);
                } else {
                    dispose();
                    Sim sim = new Sim();
                    sim.setSimNo(Long.parseLong(simNumberTextArea.getText()));
                    new MobileNumberDetails(functions,sim);

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, ex);
                simNumberTextArea.setText(null);
            }
        }
    }

    public void validateSim(DataAccess functions, int subsID){
        String simNo = simNumberTextArea.getText().replaceAll(" ", "");
        if (simNo.equals("")) {
            JOptionPane.showMessageDialog(null,"Sim number field should not be blank");
        } else if (simNo.charAt(0) != '0' && simNo.charAt(1) != '9') {
            JOptionPane.showMessageDialog(null, "Please enter a valid mobile number. eg. 09279960955");
            simNumberTextArea.setText(null);
        } else if(functions.checkIfSimnoIsRegistered(simNumberTextArea.getText())) {
            JOptionPane.showMessageDialog(null, "This sim number is already registered! " +
                    "Please try again");
            simNumberTextArea.setText(null);
        } else {
            try {
                //limit this to only allow 11 characters of input in the field
                if(simNo.length() != 11){
                    JOptionPane.showMessageDialog(null, "Please enter a valid mobile number (11 digits). eg. 09279960955");
                    simNumberTextArea.setText(null);
                } else {
                    dispose();
                    Sim sim = new Sim();
                    sim.setSimNo(Long.parseLong(simNumberTextArea.getText()));
                    new MobileNumberDetails(functions,sim, subsID);

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, ex);
                simNumberTextArea.setText(null);
            }
        }
    }
}
