package team.hnuwt.data.perm.bean;

import java.sql.Timestamp;

public class CollectorinfoBean {

    private String collectorID;
    private String collectorType;
    private String CollectorName;
    private String CollectorAddress;
    private Timestamp CreateTime;
    private Timestamp ReadDate;
    private int ReadSuccessCount;

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }

    public String getCollectorType() {
        return collectorType;
    }

    public void setCollectorType(String collectorType) {
        this.collectorType = collectorType;
    }

    public String getCollectorName() {
        return CollectorName;
    }

    public void setCollectorName(String collectorName) {
        CollectorName = collectorName;
    }

    public String getCollectorAddress() {
        return CollectorAddress;
    }

    public void setCollectorAddress(String collectorAddress) {
        CollectorAddress = collectorAddress;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public Timestamp getReadDate() {
        return ReadDate;
    }

    public void setReadDate(Timestamp readDate) {
        ReadDate = readDate;
    }

    public int getReadSuccessCount() {
        return ReadSuccessCount;
    }

    public void setReadSuccessCount(int readSuccessCount) {
        ReadSuccessCount = readSuccessCount;
    }
}
