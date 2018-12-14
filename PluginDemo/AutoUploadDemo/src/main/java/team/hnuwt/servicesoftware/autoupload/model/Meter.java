package team.hnuwt.servicesoftware.autoupload.model;

public class Meter {
    private int id; // 表序号
    private long data; // 表读数
    private int state; // 阀门状态

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
