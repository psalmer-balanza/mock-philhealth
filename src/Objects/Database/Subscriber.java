package Objects.Database;

import java.sql.Blob;

public class Subscriber {
    private int subsID = 0;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String unitNo;
    private String street;
    private String subdivision;
    private String province;
    private String city;
    private String barangay;
    private int zipCode;
    private String sex;
    private String birthDate;
    private String nationality;
    private int typeId;
    private Blob idPic;
    private String idNumber;
    public Subscriber(int subsID, String firstName, String middleName, String lastName,
                      String suffix, String unitNo, String street, String subdivision, String province,
                      String city, String barangay, int zipCode, String sex, String birthDate, String nationality,
                      int idType, Blob idPic, String idNumber) {
        this.subsID = subsID;
        this.idNumber = idNumber;
        this.firstName = firstName;
        this. middleName = middleName;
        this.lastName = lastName;
        this.suffix = suffix;
        this.unitNo = unitNo;
        this.street = street;
        this.subdivision = subdivision;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.zipCode = zipCode;
        this.sex = sex;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.typeId = idType;
    }
    public Subscriber(String firstName, String middleName, String lastName,
                       String suffix, String unitNo, String street, String subdivision, String province,
                       String city, String barangay, int zipCode, String sex, String birthDate, String nationality,
                       int idType, Blob idPic, String idNumber) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this. middleName = middleName;
        this.lastName = lastName;
        this.suffix = suffix;
        this.unitNo = unitNo;
        this.street = street;
        this.subdivision = subdivision;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.zipCode = zipCode;
        this.sex = sex;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.typeId = idType;
        this.idPic = idPic;
    }

    public Subscriber() {

    }

    public int getSubsID() {
        return subsID;
    }

    public void setSubsID(int subsID) {
        this.subsID = subsID;
    }

    public String  getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public void setIdPic(Blob idPic) { this.idPic = idPic; }

    public Blob getIdPic() {
        return idPic;
    }
}
