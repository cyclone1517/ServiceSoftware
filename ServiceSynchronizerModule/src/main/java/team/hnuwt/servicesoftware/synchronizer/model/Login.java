package team.hnuwt.servicesoftware.synchronizer.model;

import java.sql.Timestamp;

public class Login {
    private long collectorId; // 集中器Id
    private int port; // 表序号
    private int state;
    private Timestamp time;

    public Login()
    {
    }

    public Login(long collectorId)
    {
        this(collectorId, 0, 1);
    }

    public Login(long collectorId, int port, int state) {
        this.collectorId = collectorId;
        this.port = port;
        this.state = state;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(long collectorId) {
        this.collectorId = collectorId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
