package Objects.Database;

public class Sim {
    private long simNo;
    private int subsid;
    private int networkId;

    public Sim() {}
    public void setSimNo(long simno) {
        this.simNo = simno;
    }
    public long getSimNo() {
        return simNo;
    }
    public void setSubsid(int subsid) {
        this.subsid = subsid;
    }
    public int getSubsid() {
        return subsid;
    }
    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }
    public int getNetworkId() {
        return networkId;
    }
}
