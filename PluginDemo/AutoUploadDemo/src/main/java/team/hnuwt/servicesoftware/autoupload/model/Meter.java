package team.hnuwt.servicesoftware.autoupload.model;

public class Meter {
    private int id; // �����
    private long data; // �����
    private int state; // ����״̬

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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
        return "Meter [id=" + id + ", data=" + data + ", state=" + state + "]";
    }

}
