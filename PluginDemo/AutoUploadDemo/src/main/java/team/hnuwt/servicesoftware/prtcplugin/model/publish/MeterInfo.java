package team.hnuwt.servicesoftware.prtcplugin.model.publish;

/**
 * 表数据实体
 */
public class MeterInfo {

    private int meterAddr; // 表序号
    private long readValue; // 表读数
    private String state; // 阀门状态(只有单抄表才回复)

    public MeterInfo(int meterAddr, long readValue, String state) {
        this.meterAddr = meterAddr;
        this.readValue = readValue;
        this.state = state;
    }

    public int getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(int meterAddr) {
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
