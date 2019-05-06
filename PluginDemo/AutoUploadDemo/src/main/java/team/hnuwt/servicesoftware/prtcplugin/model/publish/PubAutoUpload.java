package team.hnuwt.servicesoftware.prtcplugin.model.publish;

import java.util.List;

public class PubAutoUpload {
    private long termAddr;  // 集中器Id
    private int count;      // 抄表数量
    private List<MeterInfo> meter; // 表信息

    public long getTermAddr() {
        return termAddr;
    }

    public void setTermAddr(long termAddr) {
        this.termAddr = termAddr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MeterInfo> getMeter() {
        return meter;
    }

    public void setMeter(List<MeterInfo> meter) {
        this.meter = meter;
    }
}
