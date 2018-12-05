package team.hnuwt.protocol.model;

public class Data {
    private long collectorId; // ������Id
    private int meterId; // �����
    private int data; // �����
    private int valveState; // ����״̬

    public Data()
    {
    }

    public Data(long collectorId, int data, int meterId, int valveState)
    {
        this.collectorId = collectorId;
        this.meterId = meterId;
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
        return meterId;
    }

    public void setMeterId(int meterId)
    {
        this.meterId = meterId;
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

    @Override
    public String toString()
    {
        return "Data [collectorId=" + collectorId + ", meterId=" + meterId + ", data=" + data + ", valveState="
                + valveState + "]";
    }
}
