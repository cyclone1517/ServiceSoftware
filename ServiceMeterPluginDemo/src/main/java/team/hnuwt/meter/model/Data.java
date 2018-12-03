package team.hnuwt.meter.model;

public class Data {
    private long collectorId; // 集中器Id
    private int MeterId; // 表序号
    private int data; // 表读数
    private int valveState; // 阀门状态

    public Data(long collectorId, int MeterId, int data, int valveState)
    {
        this.collectorId = collectorId;
        this.MeterId = MeterId;
        this.data = data;
        this.valveState = valveState;
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
        return MeterId;
    }

    public void setMeterId(int meterId)
    {
        MeterId = meterId;
    }

    public int getData()
    {
        return data;
    }

    public void setData(int data)
    {
        this.data = data;
    }

    public int getValveState()
    {
        return valveState;
    }

    public void setValveState(int valveState)
    {
        this.valveState = valveState;
    }
}
