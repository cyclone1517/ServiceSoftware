package team.hnuwt.servicesoftware.model;

public class Logout {

    private long id;
    private int port;

    public Logout(long id, int port) {
        this.id = id;
        this.port = port;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
