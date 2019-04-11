package team.hnuwt.servicesoftware.disdevice.mode;

public class Archive {

    private String id;      /* 表序号 */
    private String madd;    /* 表地址 */
    private String prtc;    /* 协议类型 */
    private String port;    /* 端口 */
    private String cadd;    /* 采集模块地址 */

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
}
