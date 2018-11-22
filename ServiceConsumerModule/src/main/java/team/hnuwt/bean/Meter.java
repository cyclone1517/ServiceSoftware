package team.hnuwt.bean;

public class Meter {
    private int id; //表序号
    private int data; //表读数
    private int valveState; //阀门状态
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getData() {
        return data;
    }
    public void setData(int data) {
        this.data = data;
    }
    public int isValveState() {
        return valveState;
    }
    public void setValveState(int valveState) {
        this.valveState = valveState;
    }
    @Override
    public String toString() {
        return "Meter [id=" + id + ", data=" + data + ", valveState=" + valveState + "]";
    }
    
}
