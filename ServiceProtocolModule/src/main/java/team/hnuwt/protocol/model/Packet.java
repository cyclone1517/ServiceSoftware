package team.hnuwt.protocol.model;

import java.util.List;

public class Packet {
    private String firstStartChar; // ��ʼ�ַ�
    private int firstLength; // ����
    private int secondLength; // ����
    private String secondStartChar; // ��ʼ�ַ�
    private String control; // ������
    private String address; // ��ַ��
    private String afn;
    private String seq;
    private String dataId; // ���ݵ�Ԫ��ʶ
    private int number; // ������
    List<Meter> meter;
    private String cs;
    private String endChar; // �����ַ�

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
