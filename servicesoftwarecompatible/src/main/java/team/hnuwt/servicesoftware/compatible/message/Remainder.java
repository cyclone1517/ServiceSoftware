package team.hnuwt.servicesoftware.compatible.message;

import team.hnuwt.servicesoftware.compatible.util.ByteBuilder;

/**
 * 用来记录粘包情况下当前报文的状态以及字符串
 */
public class Remainder {
    private ByteBuilder result;
    private int state;

    public Remainder(ByteBuilder result, int state)
    {
        this.result = result;
        this.state = state;
    }

    public ByteBuilder getResult()
    {
        return result;
    }

    public void ByteBuilder(ByteBuilder result)
    {
        this.result = result;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < result.length(); i++)
        {
            s.append(String.format("%02x", new Integer(result.getByte(i) & 0xFF)).toUpperCase());
        }
        return "Remainder [result=" + s + ", state=" + state + "]";
    }

}
