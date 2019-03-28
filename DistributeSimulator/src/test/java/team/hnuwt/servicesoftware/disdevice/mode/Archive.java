package team.hnuwt.servicesoftware.disdevice.mode;

public class Archive {

    private String id;
    private String madd;
    private String prtc;
    private String port;
    private String cadd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMadd() {
        return madd;
    }

    public void setMadd(String madd) {
        this.madd = madd;
    }

    public String getPrtc() {
        return prtc;
    }

    public void setPrtc(String prtc) {
        this.prtc = prtc;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCadd() {
        return cadd;
    }

    public void setCadd(String cadd) {
        this.cadd = cadd;
    }

    @Override
    public String toString() {
        return "Archive{" +
                "id='" + id + '\'' +
                ", madd='" + madd + '\'' +
                ", prtc='" + prtc + '\'' +
                ", port='" + port + '\'' +
                ", cadd='" + cadd + '\'' +
                '}';
    }
}
