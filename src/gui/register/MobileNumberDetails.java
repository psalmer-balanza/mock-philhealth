package gui.register;

import Objects.DataAccess;
import Objects.Database.NetworkDetails;
import Objects.Database.Sim;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class MobileNumberDetails extends JFrame{
    private JPanel mainPanel;
    private JButton proceedBtn;
    private JButton cancelBtn;
    private JLabel netproLbl;
    private JLabel numLbl;
    private JLabel titleLbl;
    private JTextArea simnoField;
    private JTextField mobileNumberField;
    private JComboBox<String> comboBox1;

    public MobileNumberDetails(DataAccess functions, Sim sim){
        functions.setCon();
        new JFrame();
        setTitle("Confirm SIM");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 320, 200);
        setLocationRelativeTo(null);

        mobileNumberField.setText(String.valueOf(sim.getSimNo()));
        try {
            for(String network: functions.getNetworkProviders()){
                comboBox1.addItem(network);
            }
//        String [] array = new String[functions.getNetworkProviders().size()];
//        for (int i = 0; i < array.length; i++) {
//            array[i] = functions.getNetworkProviders().get(i);
//        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e);
        }

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterMobileNumber(functions);
                dispose();
            }
        });

        proceedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NetworkDetails netdet = new NetworkDetails();
                netdet.setNetworkName((String)comboBox1.getSelectedItem());
                new EnterPersonalInformation(functions, sim, netdet);
                dispose();
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

    public MobileNumberDetails(DataAccess functions, Sim sim, int subsID){
        new JFrame();
        setTitle("Confirm SIM");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 320, 200);
        setLocationRelativeTo(null);

        mobileNumberField.setText(String.valueOf(sim.getSimNo()));
        try {
            for(String network: functions.getNetworkProviders()){
                comboBox1.addItem(network);
            }
//        String [] array = new String[functions.getNetworkProviders().size()];
//        for (int i = 0; i < array.length; i++) {
//            array[i] = functions.getNetworkProviders().get(i);
//        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e);
        }

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterMobileNumber(functions);
                dispose();
            }
        });

        proceedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NetworkDetails netdet = new NetworkDetails();
                netdet.setNetworkName((String)comboBox1.getSelectedItem());
                try {
                    dispose();
                    new EnterPersonalInformation(functions, sim, netdet, subsID, true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
