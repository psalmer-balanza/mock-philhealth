package Objects.Database;

public class Registration {
    private int registrationID;
    private long simNo;
    private String owner; //outsider
    private String registrationDate;
    private String registrationType;
    public Registration() {

    }

    public Registration(int registrationID, long simNo, String owner, String registrationDate, String registrationType) {
        this.owner = owner;
        this.registrationID = registrationID;
        this.simNo = simNo;
        this.registrationDate = registrationDate;
        this.registrationType = registrationType;
    }
    public long getSimNo() {
        return simNo;
    }

    public void setSimNo(long simNo) {
        this.simNo = simNo;
    }

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }
}
