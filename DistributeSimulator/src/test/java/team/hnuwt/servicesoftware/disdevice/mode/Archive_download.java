package team.hnuwt.servicesoftware.disdevice.mode;

import java.util.List;

public class Archive_download {
    private String addr;
    private String num;
    private List<Archive> archive;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<Archive> getArchive() {
        return archive;
    }

    public void setArchives(List<Archive> archive) {
        this.archive = archive;
    }
}
