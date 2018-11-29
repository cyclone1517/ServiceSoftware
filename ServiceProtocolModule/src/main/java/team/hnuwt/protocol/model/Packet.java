package team.hnuwt.protocol.model;

import java.util.List;

public class Packet {
    private String firstStartChar; // 起始字符
    private int firstLength; // 长度
    private int secondLength; // 长度
    private String secondStartChar; // 起始字符
    private String control; // 控制域
    private String address; // 地址域
    private String afn;
    private String seq;
    private String dataId; // 数据单元标识
    private int number; // 表数量
    List<Meter> meter;
    private String cs;
    private String endChar; // 结束字符

    public String getFirstStartChar()
    {
        return firstStartChar;
    }

    public void setFirstStartChar(String firstStartChar)
    {
        this.firstStartChar = firstStartChar;
    }

    public int getFirstLength()
    {
        return firstLength;
    }

    public void setFirstLength(int firstLength)
    {
        this.firstLength = firstLength;
    }

    public int getSecondLength()
    {
        return secondLength;
    }

    public void setSecondLength(int secondLength)
    {
        this.secondLength = secondLength;
    }

    public String getSecondStartChar()
    {
        return secondStartChar;
    }

    public void setSecondStartChar(String secondStartChar)
    {
        this.secondStartChar = secondStartChar;
    }

    public String getControl()
    {
        return control;
    }

    public void setControl(String control)
    {
        this.control = control;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAfn()
    {
        return afn;
    }

    public void setAfn(String afn)
    {
        this.afn = afn;
    }

    public String getSeq()
    {
        return seq;
    }

    public void setSeq(String seq)
    {
        this.seq = seq;
    }

    public String getDataId()
    {
        return dataId;
    }

    public void setDataId(String dataId)
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

    public String getCs()
    {
        return cs;
    }

    public void setCs(String cs)
    {
        this.cs = cs;
    }

    public String getEndChar()
    {
        return endChar;
    }

    public void setEndChar(String endChar)
    {
        this.endChar = endChar;
    }

    @Override
    public String toString()
    {
        return "Packet [firstStartChar=" + firstStartChar + ", firstLength=" + firstLength + ", secondLength="
                + secondLength + ", secondStartChar=" + secondStartChar + ", control=" + control + ", address="
                + address + ", afn=" + afn + ", seq=" + seq + ", dataId=" + dataId + ", number=" + number + ", meter="
                + meter + ", cs=" + cs + ", endChar=" + endChar + "]";
    }
}
