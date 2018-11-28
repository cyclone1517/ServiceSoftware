package team.hnuwt.message;

import team.hnuwt.util.ByteBuilder;

/**
 * ������¼ճ������µ�ǰ���ĵ�״̬�Լ��ַ���
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
