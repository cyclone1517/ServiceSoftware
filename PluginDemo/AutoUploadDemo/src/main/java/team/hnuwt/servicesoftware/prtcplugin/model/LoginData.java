package team.hnuwt.servicesoftware.prtcplugin.model;

import java.sql.Timestamp;

public class LoginData {
    private long addr;
    private Timestamp timestamp;

    public long getAddr() {
        return addr;
    }

    public void setAddr(long addr) {
        this.addr = addr;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
