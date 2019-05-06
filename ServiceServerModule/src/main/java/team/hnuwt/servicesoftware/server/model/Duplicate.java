package team.hnuwt.servicesoftware.server.model;

public class Duplicate {

    private long id;
    private String oldScInfo;
    private String newScInfo;

    public Duplicate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOldScInfo() {
        return oldScInfo;
    }

    public void setOldScInfo(String oldScInfo) {
        this.oldScInfo = oldScInfo;
    }

    public String getNewScInfo() {
        return newScInfo;
    }

    public void setNewScInfo(String newScInfo) {
        this.newScInfo = newScInfo;
    }
}
