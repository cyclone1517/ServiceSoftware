package team.hnuwt.servicesoftware.prtcplugin.model;

/**
 * 多数据实体的对应关系，比如多抄表
 */
public class ListInformation {
    private int start;
    private int end;
    private String className;
    private String fieldName;

    public ListInformation(int start, int end, String className, String fieldName)
    {
        this.start = start;
        this.end = end;
        this.className = className;
        this.fieldName = fieldName;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getEnd()
    {
        return end;
    }

    public void setEnd(int end)
    {
        this.end = end;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String filedName)
    {
        this.fieldName = filedName;
    }
}
