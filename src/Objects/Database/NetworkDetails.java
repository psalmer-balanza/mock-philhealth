package Objects.Database;

public class NetworkDetails {
    private int networkId;
    private String networkName;

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public String getNetworkName() {
        return networkName;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }
}
