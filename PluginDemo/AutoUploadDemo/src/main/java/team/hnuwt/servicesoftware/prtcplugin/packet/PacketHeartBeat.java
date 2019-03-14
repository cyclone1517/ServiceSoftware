package team.hnuwt.servicesoftware.prtcplugin.packet;

public class PacketHeartBeat implements Packet{
    private int firstStartChar; // 起始字符
    private int firstLength; // 长度
    private int secondLength; // 长度
    private int secondStartChar; // 起始字符
    private int control; // 控制域
    private long address; // 地址域
    private int afn;
    private int seq;
    private long dataId; // 数据单元标识
    private long sigBCD;    //信号强度BCD码
    private int cs;
    private int endChar; // 结束字符

    public long getSigBCD() {
        return sigBCD;
    }

    public void setSigBCD(long sigBCD) {
        this.sigBCD = sigBCD;
    }

    public int getFirstStartChar()
    {
        return firstStartChar;
    }

    public void setFirstStartChar(int firstStartChar)
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

    public int getSecondStartChar()
    {
        return secondStartChar;
    }

    public void setSecondStartChar(int secondStartChar)
    {
        this.secondStartChar = secondStartChar;
    }

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

    public int getCs()
    {
        return cs;
    }

    public void setCs(int cs)
    {
        this.cs = cs;
    }

    public int getEndChar()
    {
        return endChar;
    }

    public void setEndChar(int endChar)
    {
        this.endChar = endChar;
    }

    @Override
    public String toString() {
        return "PacketHeartBeat{" +
                "firstStartChar=" + firstStartChar +
                ", firstLength=" + firstLength +
                ", secondLength=" + secondLength +
                ", secondStartChar=" + secondStartChar +
                ", control=" + control +
                ", address=" + address +
                ", afn=" + afn +
                ", seq=" + seq +
                ", dataId=" + dataId +
                ", sigBCD=" + sigBCD +
                ", cs=" + cs +
                ", endChar=" + endChar +
                '}';
    }
}
