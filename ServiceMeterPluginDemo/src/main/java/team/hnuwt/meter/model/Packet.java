package team.hnuwt.meter.model;

import java.util.List;

public class Packet {
    private int control; // 控制域
    private long address; // 地址域
    private int afn;
    private int seq;
    private long dataId; // 数据单元标识
    private int number; // 表数量
    List<Meter> meter;

    public int getControl()
    {
        return control;
    }

    public void setControl(int control)
    {
        this.control = control;
    }

    public long getAddress()
    {
        return address;
    }

    public void setAddress(long address)
    {
        this.address = address;
    }

    public int getAfn()
    {
        return afn;
    }

    public void setAfn(int afn)
    {
        this.afn = afn;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public long getDataId()
    {
        return dataId;
    }

    public void setDataId(long dataId)
    {
        this.dataId = dataId;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public List<Meter> getMeter()
    {
        return meter;
    }

    public void setMeter(List<Meter> meter)
    {
        this.meter = meter;
    }

    @Override
    public String toString()
    {
        return "Packet [control=" + control + ", address=" + address + ", afn=" + afn + ", seq=" + seq + ", dataId="
                + dataId + ", number=" + number + ", meter=" + meter + "]";
    }
}
