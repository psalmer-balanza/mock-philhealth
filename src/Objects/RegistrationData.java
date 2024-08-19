//package Objects;
//
//import Objects.Database.Subscriber;
//
//public class RegistrationData {
//    private long sim;
//    private String  networkProvider; //network table
//    private Subscriber subscriber; //other cl;ass
//    private String registrationDate; //registration table
//    private String registrationType; //registration table
//
//    public RegistrationData(){
//    }
//
//    public long getSim() {
//        return sim;
//    }
//
//    public void setSim(long sim) {
//        this.sim = sim;
//    }
//
//    public String getNetworkProvider() {
//        return networkProvider;
//    }
//
//    public void setNetworkProvider(String networkProvider) {
//        this.networkProvider = networkProvider;
//    }
//
//    public Subscriber getSubscriber() {
//        return subscriber;
//    }
//
//    public void setSubscriber(Subscriber subscriber) {
//        this.subscriber = subscriber;
//    }
//
//    public String getRegistrationDate() {
//        return registrationDate;
//    }
//
//    public void setRegistrationDate(String registrationDate) {
//        this.registrationDate = registrationDate;
//    }
//
//    public String getRegistrationType() {
//        return registrationType;
//    }
//
//    public void setRegistrationType(String registrationType) {
//        this.registrationType = registrationType;
//    }
//
//    public String concatName(){
//        return this.getSubscriber().getFirstName()
//                + " " + this.getSubscriber().getMiddleName()
//                + " " + this.getSubscriber().getLastName()
//                + " " + this.getSubscriber().getSuffix();
//    }
//}
