package gui.register;

import Objects.DataAccess;
import Objects.Database.NetworkDetails;
import Objects.Database.Sim;
import Objects.Database.Subscriber;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;

public class EnterPersonalInformation extends JFrame{
    private JPanel mainPanel;
    private JTextField mobileNumberField;
    private JLabel mobileNumberLabel;
    private JLabel subscriberNameLabel;
    private JTextField firstNameField;
    private JTextField suffixField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JTextField birthdateField;
    private JComboBox<String> sexDropDown;
    private JTextField nationalityField;
    private JTextField unitnoField;
    private JTextField streetField;
    private JTextField provinceField;
    private JTextField cityField;
    private JTextField barangayField;
    private JTextField idNumField;
    private JButton backButton;
    private JButton proceedButton;
    private JTextField zipField;
    private JTextField villagesubField;
    private JButton selectAnImageButton;
    private JLabel imageLabel;
    private JPanel idPicPanel;
    private JComboBox<String> idTypeDropDown;
    private JLabel requiredInfo;
    private JLabel firstNameLabel;
    private JLabel middleNameLabel;
    private JLabel lastNameLabel;
    private JLabel birthdateLabelFormat;
    private JLabel sexLabel;
    private JLabel nationalityLabel;
    private JLabel unitLabel;
    private JLabel streetLabel;
    private JLabel provinceLabel;
    private JLabel cityLabel;
    private JLabel barangayLabel;
    private JLabel zipLabel;
    private JLabel idTypeLabel;
    private JLabel idpicLabel;
    private JLabel idNumLabel;
    private byte[] idPic;
    private Blob idPicBlob;

    public EnterPersonalInformation(DataAccess functions, Sim sim, NetworkDetails netdet){
        functions.setCon();

        requiredInfo.setText("<html><font color='white'>Fields with </font> <font color='red'>* </font>  <font color='white'> are required information.</font> </html>");
        firstNameLabel.setText("<html><font color='white'>First Name</font> <font color='red'> *</font> </html>");
        middleNameLabel.setText("<html><font color='white'>Middle Name</font> <font color='red'> *</font> </html>");
        lastNameLabel.setText("<html><font color='white'>Last Name</font> <font color='red'> *</font> </html>");
        birthdateLabelFormat.setText("<html><font color='white'>YYYY-MM-DD</font> <font color='red'> *</font> </html>");
        sexLabel.setText("<html><font color='white'>Sex</font> <font color='red'> *</font> </html>");
        nationalityLabel.setText("<html><font color='white'>Nationality</font> <font color='red'> *</font> </html>");
        unitLabel.setText("<html><font color='white'>Unit No./House No./Building Name:</font> <font color='red'> *</font> </html>");
        streetLabel.setText("<html><font color='white'>Street:</font> <font color='red'> *</font> </html>");
        provinceLabel.setText("<html><font color='white'>Province:</font> <font color='red'> *</font> </html>");
        cityLabel.setText("<html><font color='white'>City:</font> <font color='red'> *</font> </html>");
        barangayLabel.setText("<html><font color='white'>Barangay:</font> <font color='red'> *</font> </html>");
        zipLabel.setText("<html><font color='white'>Zip Code:</font> <font color='red'> *</font> </html>");
        idTypeLabel.setText("<html><font color='white'>ID Type:</font> <font color='red'> *</font> </html>");
        idpicLabel.setText("<html><font color='white'>ID Picture:</font> <font color='red'> *</font> </html>");
        idNumLabel.setText("<html><font color='white'>Id Number:</font> <font color='red'> *</font> </html>");

        firstNameField.setDocument(new JTextFieldLimit(30));
        middleNameField.setDocument(new JTextFieldLimit(30));
        lastNameField.setDocument(new JTextFieldLimit(30));
        suffixField.setDocument(new JTextFieldLimit(5));
        unitnoField.setDocument(new JTextFieldLimit(30));
        streetField.setDocument(new JTextFieldLimit(30));
        villagesubField.setDocument(new JTextFieldLimit(30));
        provinceField.setDocument(new JTextFieldLimit(30));
        cityField.setDocument(new JTextFieldLimit(30));
        barangayField.setDocument(new JTextFieldLimit(30));
        zipField.setDocument(new JTextFieldLimit(4));
        birthdateField.setDocument(new JTextFieldLimit(10));
        nationalityField.setDocument(new JTextFieldLimit(30));
        idNumField.setDocument(new JTextFieldLimit(15));


        new JFrame();
        setTitle("Enter Personal Information");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 1000, 700);
        setLocationRelativeTo(null);

        String [] sex = {"Female", "Male"};
        for (String s : sex) {
            sexDropDown.addItem(s);
        }

        try {
            ArrayList<String> idNameList = functions.getIdNameArray();
            idNameList.add(0, "Select ID Type");
            for (String s : idNameList) {
                idTypeDropDown.addItem(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mobileNumberField.setText(String.valueOf(sim.getSimNo()));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MobileNumberDetails(functions, sim);
                dispose();
            }
        });

        selectAnImageButton.setEnabled(false);
        imageLabel.setMaximumSize(new Dimension(200, 200));

        //THIS BASICALLY ONLY ALLOWS THE IMAGE BUTTON TO BE USABLE WHEN AN IDTYPE IS SELECTED
        idTypeDropDown.getAccessibleContext().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                selectAnImageButton.setEnabled(idTypeDropDown.getSelectedItem() != "Select ID Type");
            }
        });

        selectAnImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(EnterPersonalInformation.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        //pass this to sql
                        idPic = Files.readAllBytes(selectedFile.toPath());
                        if (idPic.length > 15000000) {
                            JOptionPane.showMessageDialog(EnterPersonalInformation.this,
                                    "Error: Image size exceeds 15 MB.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            ImageIcon icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(idPic)));

                            //some stuff about pictures
                            int width = 200, height = 200;
                            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale the image to 100x100 pixels
                            ImageIcon scaledIcon = new ImageIcon(scaledImage);
                            imageLabel.setPreferredSize(new Dimension(width, height));
                            imageLabel.setIcon(scaledIcon);
                            imageLabel.setLayout(null);
                            imageLabel.setText("");
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(EnterPersonalInformation.this, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean nameHasNumbers = false;
                    String firstName, middleName, lastName, unitNo, street, village, province, city, barangay, zip, nationality, idNum;
                    firstName = firstNameField.getText().trim();
                    middleName = middleNameField.getText().trim();
                    lastName = lastNameField.getText().trim();
                    unitNo = unitnoField.getText().trim();
                    street = streetField.getText().trim();
                    village = villagesubField.getText().trim();
                    province = provinceField.getText().trim();
                    city = cityField.getText().trim();
                    barangay = barangayField.getText().trim();
                    zip = zipField.getText().trim();
                    nationality = nationalityField.getText().trim();
                    idNum = idNumField.getText().trim();
                    String fullName = firstName + middleName + lastName;
                    for (int i = 0; i < fullName.length(); i++) {
                        if (Character.isDigit(fullName.charAt(i))) {
                            nameHasNumbers = true;
                            break;
                        }
                    }
                    if (firstName.equals("") | middleName.equals("") | lastName.equals("") |
                            unitNo.equals("") | street.equals("") | province.equals("") |
                            city.equals("") | barangay.equals("") | village.equals("") |
                            zip.equals("") | birthdateField.getText().equals("") |
                            nationality.equals("") | idNum.equals("")) {
                        JOptionPane.showMessageDialog(null, "There should be no blank fields.");
                    } else if (nameHasNumbers) {
                        JOptionPane.showMessageDialog(null, "Name should not contain numbers.");
                    } else if (zip.length() != 4) {
                        JOptionPane.showMessageDialog(null, "The zip code should consist of 4 digits.");
                    } else if (idPic==null) {
                        JOptionPane.showMessageDialog(null, "Please enter an ID picture.");
                    } else if (functions.checkIfIdIsAlreadyRegistered(functions.getIdType((String)idTypeDropDown.getSelectedItem()),
                            idNumField.getText())) {
                        int choice = JOptionPane.showConfirmDialog(null, "This id is already registered, " +
                                "would you like to redo with an existing subscriber entry?", "ID Already Registered", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            dispose();
                            new ExistingInfo(functions);
                        }
                    } else if (!validateDate(birthdateField.getText())) {
                        JOptionPane.showMessageDialog(null, "Please follow the correct format for the date.");
                    } else {
                        try{
                            idPicBlob = new SerialBlob(idPic);
                        }catch(Exception blob){
                            JOptionPane.showMessageDialog(null, "ERROR: " +blob);
                        }
                        Subscriber subscriber = new Subscriber(firstName, middleName, lastName, suffixField.getText(),
                                unitNo, street, village, province, city, barangay,
                                Integer.parseInt(zip), (String) sexDropDown.getSelectedItem(), birthdateField.getText(),
                                nationality, functions.getIdType((String)idTypeDropDown.getSelectedItem()), idPicBlob, idNum);

                        new ConfirmRegistration(functions, sim, netdet, subscriber, false);
                        dispose();
                    }
                }catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Entry! Please check if your entered Zip Code are numbers.");
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }

            }
        });
    }
    public boolean validateDate (String birthDate) {
        boolean result = true;
        String day = "";
        String month = "";
        String year = "";
        try {
            birthDate = birthdateField.getText();
            char day1, day2, month1, month2, year1, year2, year3, year4;
            month1 = birthDate.charAt(5);
            month2 = birthDate.charAt(6);
            day1 = birthDate.charAt(8);
            day2 = birthDate.charAt(9);
            year1 = birthDate.charAt(0);
            year2 = birthDate.charAt(1);
            year3 = birthDate.charAt(2);
            year4 = birthDate.charAt(3);
            day = "" + day1 + day2;
            month = "" + month1 + month2;
            year = "" + year1 + year2 + year3 + year4;
        } catch (StringIndexOutOfBoundsException exc) {
            result = false;
            JOptionPane.showMessageDialog(null, "The format of the birthdate should be: YYYY-MM-DD");
        }
        try {
            int bDay = Integer.parseInt(day);
            int bMonth = Integer.parseInt(month);
            int bYear = Integer.parseInt(year);
            if (!validateDate(bYear, bMonth, bDay)) {
                JOptionPane.showMessageDialog(null, "Invalid Date Format. the format should be YYYY-MM-DD");
            }
        } catch (NumberFormatException exc) {
            result = false;
            JOptionPane.showMessageDialog(null, "Invalid Date. it should be numbers with the format: YYYY-MM-DD");
        }

        return result;
    }

    public EnterPersonalInformation(DataAccess functions, Sim sim, NetworkDetails netdet, int subsID, boolean exist) throws SQLException {

        requiredInfo.setText("<html><font color='white'>Fields with </font> <font color='red'>* </font>  <font color='white'> are required information.</font> </html>");
        firstNameLabel.setText("<html><font color='white'>First Name</font> <font color='red'> *</font> </html>");
        middleNameLabel.setText("<html><font color='white'>Middle Name</font> <font color='red'> *</font> </html>");
        lastNameLabel.setText("<html><font color='white'>Last Name</font> <font color='red'> *</font> </html>");
        birthdateLabelFormat.setText("<html><font color='white'>YYYY-MM-DD</font> <font color='red'> *</font> </html>");
        sexLabel.setText("<html><font color='white'>Sex</font> <font color='red'> *</font> </html>");
        nationalityLabel.setText("<html><font color='white'>Nationality</font> <font color='red'> *</font> </html>");
        unitLabel.setText("<html><font color='white'>Unit No./House No./Building Name:</font> <font color='red'> *</font> </html>");
        streetLabel.setText("<html><font color='white'>Street:</font> <font color='red'> *</font> </html>");
        provinceLabel.setText("<html><font color='white'>Province:</font> <font color='red'> *</font> </html>");
        cityLabel.setText("<html><font color='white'>City:</font> <font color='red'> *</font> </html>");
        barangayLabel.setText("<html><font color='white'>Barangay:</font> <font color='red'> *</font> </html>");
        zipLabel.setText("<html><font color='white'>Zip Code:</font> <font color='red'> *</font> </html>");
        idTypeLabel.setText("<html><font color='white'>ID Type:</font> <font color='red'> *</font> </html>");
        idpicLabel.setText("<html><font color='white'>ID Picture:</font> <font color='red'> *</font> </html>");
        idNumLabel.setText("<html><font color='white'>Id Number:</font> <font color='red'> *</font> </html>");


        new JFrame();
        setTitle("Subscriber Information");
        setContentPane(mainPanel);
        setVisible(true);
        setSize( 1000, 700);
        setLocationRelativeTo(null);

        if (exist) {
            firstNameField.setEditable(false);
            middleNameField.setEditable(false);
            lastNameField.setEditable(false);
            suffixField.setEditable(false);
            birthdateField.setEditable(false);
            nationalityField.setEditable(false);
            unitnoField.setEditable(false);
            streetField.setEditable(false);
            villagesubField.setEditable(false);
            provinceField.setEditable(false);
            cityField.setEditable(false);
            barangayField.setEditable(false);
            zipField.setEditable(false);
            idNumField.setEditable(false);
            selectAnImageButton.setEnabled(false);
        }


        Subscriber subscriber = functions.retrieveSubscriber(subsID);
        firstNameField.setText(subscriber.getFirstName());
        middleNameField.setText(subscriber.getMiddleName());
        lastNameField.setText(subscriber.getLastName());
        suffixField.setText(subscriber.getSuffix());
        birthdateField.setText(subscriber.getBirthDate());
        sexDropDown.addItem(subscriber.getSex());
        nationalityField.setText(subscriber.getNationality());
        unitnoField.setText(String.valueOf(subscriber.getUnitNo()));
        streetField.setText(subscriber.getStreet());
        villagesubField.setText(subscriber.getSubdivision());
        provinceField.setText(subscriber.getProvince());
        cityField.setText(subscriber.getCity());
        barangayField.setText(subscriber.getBarangay());
        zipField.setText(String.valueOf(subscriber.getZipCode()));
        String idType = functions.getIdNameFromIdType(subscriber.getTypeId());
        idTypeDropDown.addItem(idType);
        idNumField.setText(subscriber.getIdNumber());
        if (exist) {
            imageLabel.setIcon(functions.getSubscriberIdPicUsingSubsID(subscriber.getSubsID()));
        } else {
            imageLabel.setIcon(functions.getSubscriberIdPicNotYetSubscribed(subscriber.getIdPic()));
        }
        mobileNumberField.setText(String.valueOf(sim.getSimNo()));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MobileNumberDetails(functions, sim, subsID);
                dispose();
            }
        });

        selectAnImageButton.setEnabled(false);
        imageLabel.setMaximumSize(new Dimension(200, 200));

        //THIS BASICALLY ONLY ALLOWS THE IMAGE BUTTON TO BE USABLE WHEN AN IDTYPE IS SELECTED
        idTypeDropDown.getAccessibleContext().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                selectAnImageButton.setEnabled(idTypeDropDown.getSelectedItem() != "Select ID Type");
            }
        });

        selectAnImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(EnterPersonalInformation.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        //pass this to sql
                        idPic = Files.readAllBytes(selectedFile.toPath());
                        if (idPic.length > 15000000) {
                            JOptionPane.showMessageDialog(EnterPersonalInformation.this,
                                    "Error: Image size exceeds 15 MB.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            ImageIcon icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(idPic)));

                            //some stuff about pictures
                            int width = 200, height = 200;
                            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale the image to 100x100 pixels
                            ImageIcon scaledIcon = new ImageIcon(scaledImage);
                            imageLabel.setPreferredSize(new Dimension(width, height));
                            imageLabel.setIcon(scaledIcon);
                            imageLabel.setLayout(null);
                            imageLabel.setText("");
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(EnterPersonalInformation.this, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfirmRegistration(functions, sim, netdet, subscriber, true);
                dispose();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }

            }
        });
    }

    public boolean validateDate(int yr, int mo, int dy)   {
        boolean result = true;
        boolean isLeapYear = false;
        GregorianCalendar calendar = new GregorianCalendar();
        isLeapYear = calendar.isLeapYear(yr);
        if (yr < 0) {
            return false;
        }
        switch (mo) {
            case 1, 3, 5, 7, 8, 10, 12:
                if (dy < 1 || dy > 31) {
                    result = false;
                }
                break;
            case 2:
                if (isLeapYear) {
                    if (dy < 1 || dy > 29) {
                        result = false;
                    }
                } else {
                    if (dy < 1 || dy > 28) {
                        result = false;
                    }
                }
                break;

            case 4, 6, 9, 11:
                if (dy < 1 || dy > 30) {
                    result = false;
                }
                break;

            default:
                result = false;

        }
        return result;
    }
}
