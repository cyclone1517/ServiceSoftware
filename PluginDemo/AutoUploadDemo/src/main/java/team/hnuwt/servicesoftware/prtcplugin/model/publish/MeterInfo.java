package team.hnuwt.servicesoftware.prtcplugin.model.publish;

/**
 * 表数据实体
 */
public class MeterInfo {

    private String meterAddr; // 表序号
    private long readValue; // 表读数
    private String state; // 阀门状态(只有单抄表才回复)

    public MeterInfo(int addr, long readValue, String state) {
        this.meterAddr = toNBitStr(4, addr);
        this.readValue = readValue;
        this.state = state;
    }

    private String toNBitStr(int bit, int addr){
        StringBuilder sb = new StringBuilder();
        sb.append(addr);
        while (sb.length() < bit)sb.insert(0, "0");
        return sb.toString();
    }

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr;
    }

    public long getReadValue() {
        return readValue;
    }

    public void setReadValue(long readValue) {
        this.readValue = readValue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
