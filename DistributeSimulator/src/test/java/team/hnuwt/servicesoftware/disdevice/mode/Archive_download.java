package team.hnuwt.servicesoftware.disdevice.mode;

import java.util.List;

public class Archive_download {
    private String termAddr;
    private String count;
    private List<Archive> archive;

    public String getTermAddr() {
        return termAddr;
    }

    public void setTermAddr(String termAddr) {
        this.termAddr = termAddr;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Archive> getArchive() {
        return archive;
    }

    public void setArchive(List<Archive> archive) {
        this.archive = archive;
    }
}
