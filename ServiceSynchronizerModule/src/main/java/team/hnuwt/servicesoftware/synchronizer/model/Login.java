package team.hnuwt.servicesoftware.synchronizer.model;

import java.sql.Timestamp;

public class Login {
    private long collectorId; // 集中器Id
    private int state;
    private Timestamp time;

    public Login()
    {
    }

    public Login(long collectorId, int state) {
        this.collectorId = collectorId;
        this.state = state;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(long collectorId) {
        this.collectorId = collectorId;
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
