package team.hnuwt.servicesoftware.synchronizer.model;

public class Data {
    private long collectorId; // 集中器Id
    private int meterId; // 表序号
    private long data; // 表读数
    private int state; // 表状态

    public Data()
    {
    }

    public Data(long collectorId, int meterId, long data, int state)
    {
        this.collectorId = collectorId;
        this.meterId = meterId;
        this.data = data;
        this.state = state;
    }

    public long getCollectorId()
    {
        return collectorId;
    }

    public void setCollectorId(long collectorId)
    {
        this.collectorId = collectorId;
    }

    public int getMeterId()
    {
        return meterId;
    }

    public void setMeterId(int meterId)
    {
        this.meterId = meterId;
    }

    public long getData()
    {
        return data;
    }

    public void setData(long data)
    {
        this.data = data;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "Data [collectorId=" + collectorId + ", meterId=" + meterId + ", data=" + data + ", state=" + state
                + "]";
    }

}
