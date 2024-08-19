package gui.update;

import Objects.DataAccess;
import Objects.Database.Subscriber;
import gui.MainMenu;
import gui.register.ConfirmRegistration;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditUserInformation extends JFrame {
    private JPanel mainPanel;
    private JPanel titleLbl;
    private JTextField middleNameTextField;
    private JTextField lastNameTextField;
    private JTextField unitNoTextField;
    private JTextField streetTextField;
    private JTextField subdivisionTextField;
    private JTextField provinceTextField;
    private JTextField cityTextField;
    private JTextField barangayTextField;
    private JTextField zipcodeTextField;
    private JLabel middleNameLabel;
    private JLabel lastNameLabel;
    private JLabel unitNoLabel;
    private JLabel streetLabel;
    private JLabel subdivisionLabel;
    private JLabel provinceLabel;
    private JLabel barangayLabel;
    private JLabel cityLabel;
    private JLabel zipcodeLabel;
    private JButton cancelButton;
    private JButton confirmButton;
    private JLabel controlNumberLbl;

    public EditUserInformation(String idNumber, DataAccess functions) {
        middleNameLabel.setText("<html><font color='white'>Middle Name:</font> <font color='red'> *</font> </html>");
        lastNameLabel.setText("<html><font color='white'>Last Name:</font> <font color='red'> *</font> </html>");
        unitNoLabel.setText("<html><font color='white'>Unit No.:</font> <font color='red'> *</font> </html>");
        streetLabel.setText("<html><font color='white'>Street:</font> <font color='red'> *</font> </html>");
        provinceLabel.setText("<html><font color='white'>Province:</font> <font color='red'> *</font> </html>");
        cityLabel.setText("<html><font color='white'>City:</font> <font color='red'> *</font> </html>");
        barangayLabel.setText("<html><font color='white'>Barangay:</font> <font color='red'> *</font> </html>");
        subdivisionLabel.setText("<html><font color='white'>Subdivision:</font> <font color='red'> *</font> </html>");
        zipcodeLabel.setText("<html><font color='white'>Zip Code:</font> <font color='red'> *</font> </html>");

        new JFrame();
        setContentPane(mainPanel);
        setTitle("Update Subscriber Information");
        setVisible(true);
        setSize( 500,450);
        setLocationRelativeTo(null);

        controlNumberLbl.setText(String.valueOf(idNumber));
        Subscriber subscriberToUpdate = null;

        try{
            for(Subscriber subscriber: functions.getSubscribers()){
                if(subscriber.getIdNumber().equals(idNumber)){
                    subscriberToUpdate = subscriber;
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        assert subscriberToUpdate != null;
        middleNameTextField.setText(subscriberToUpdate.getMiddleName());
        lastNameTextField.setText(subscriberToUpdate.getLastName());
        unitNoTextField.setText(String.valueOf(subscriberToUpdate.getUnitNo()));
        streetTextField.setText(subscriberToUpdate.getStreet());
        subdivisionTextField.setText(subscriberToUpdate.getSubdivision());
        provinceTextField.setText(subscriberToUpdate.getProvince());
        cityTextField.setText(subscriberToUpdate.getCity());
        barangayTextField.setText(subscriberToUpdate.getBarangay());
        zipcodeTextField.setText(String.valueOf(subscriberToUpdate.getZipCode()));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateSearchRegistration(functions);
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String middleName = middleNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String unitNo = unitNoTextField.getText();
                String street = streetTextField.getText();
                String subdivision = subdivisionTextField.getText();
                String province = provinceTextField.getText();
                String city = cityTextField.getText();
                String barangay = barangayTextField.getText();
                String zipCode = zipcodeTextField.getText();

                try {
                    boolean nameHasNumbers = false;
                    middleName = middleNameTextField.getText().trim();
                    lastName = lastNameTextField.getText().trim();
                    unitNo = unitNoTextField.getText().trim();
                    street = streetTextField.getText().trim();
                    subdivision = subdivisionTextField.getText().trim();
                    province = provinceTextField.getText().trim();
                    city = cityTextField.getText().trim();
                    barangay = barangayTextField.getText().trim();
                    zipCode = zipcodeTextField.getText().trim();

                    String fullName = middleName + lastName;
                    for (int i = 0; i < fullName.length(); i++) {
                        if (Character.isDigit(fullName.charAt(i))) {
                            nameHasNumbers = true;
                            break;
                        }
                    }
                    if (middleName.equals("") | lastName.equals("") |
                            unitNo.equals("") | street.equals("") | province.equals("") |
                            city.equals("") | barangay.equals("") | zipCode.equals("")) {
                        JOptionPane.showMessageDialog(null, "There should be no blank fields.");
                    } else if (nameHasNumbers) {
                        JOptionPane.showMessageDialog(null, "Name should not contain numbers.");
                    } else if (zipCode.length() != 4) {
                        JOptionPane.showMessageDialog(null, "The zip code should consist of 4 digits.");
                    } else {
                        if(functions.updateSubscriber(idNumber,middleName,lastName,unitNo,street,subdivision
                                ,province,city,barangay,(Integer.parseInt(zipCode)))) {

                            JOptionPane.showMessageDialog(null, "Subscriber Information Updated Successfully!");
                            dispose();
                            new MainMenu(functions);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Entry! Please check if your entered Zip Code are numbers.");
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
