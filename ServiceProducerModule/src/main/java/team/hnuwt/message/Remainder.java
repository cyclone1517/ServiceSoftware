package team.hnuwt.message;

/**
 * 用来记录粘包情况下当前报文的状态以及字符串
 */
public class Remainder {
    private StringBuilder result;
    private int state;
    
    public Remainder(StringBuilder result, int state)
    {
        this.result = result;
        this.state = state;
    }
    
    public StringBuilder getResult() {
        return result;
    }
    public void setResult(StringBuilder result) {
        this.result = result;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Remainder [result=" + result + ", state=" + state + "]";
    }
    
}
