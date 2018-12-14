package team.hnuwt.servicesoftware.protocol.model;

public class Plugin {
    private long id;
    private String jar;
    private String className;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getJar()
    {
        return jar;
    }

    public void setJar(String jar)
    {
        this.jar = jar;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }
}
