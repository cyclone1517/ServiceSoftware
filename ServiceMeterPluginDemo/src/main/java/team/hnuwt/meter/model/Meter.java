package team.hnuwt.meter.model;

public class Meter {
    private int id; // �����
    private int data; // �����
    private int valveState; // ����״̬

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public void setValveState(int valueState)
    {
        this.valveState = valueState;
    }

    @Override
    public String toString()
    {
        return "Meter [id=" + id + ", data=" + data + ", valveState=" + valveState + "]";
    }

}
