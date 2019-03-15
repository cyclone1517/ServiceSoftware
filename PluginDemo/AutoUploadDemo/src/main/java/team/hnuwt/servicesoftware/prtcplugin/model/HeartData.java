package team.hnuwt.servicesoftware.prtcplugin.model;

import java.sql.Timestamp;

public class HeartData {
    private String addr;
    private Timestamp timestamp;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
