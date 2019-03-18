package team.hnuwt.servicesoftware.synchronizer.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class HeartBeat {
    private long collectorId; // 集中器Id
    private int port; // 表序号
    private Timestamp time;

    Logger logger = LoggerFactory.getLogger(HeartBeat.class);

    public HeartBeat()
    {
    }

    public HeartBeat(long collectorId)
    {
        this(collectorId, 0);
    }

    public HeartBeat(long collectorId, int port) {
        this.collectorId = collectorId;
        this.port = port;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
