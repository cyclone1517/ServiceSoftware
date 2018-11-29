package team.hnuwt.server.util;

import java.util.Arrays;

public class ByteBuilder {
    private byte[] value;
    private int count;

    public ByteBuilder()
    {
        value = new byte[16];
        count = 0;
    }

    public ByteBuilder(int capacity)
    {
        value = new byte[capacity];
        count = 0;
    }

    public ByteBuilder(byte b)
    {
        value = new byte[16];
        count = 0;
        value[count++] = b;
    }

    public ByteBuilder(String s)
    {
        int len = s.length();
        value = new byte[len / 2 + 16];
        count = 0;
        for (int i = 0; i < len; i += 2)
        {
            int x = Integer.parseInt(s.substring(i, i + 2), 16);
            value[count++] = (byte) x;
        }
    }

    public ByteBuilder(byte[] value, int begin, int end)
    {
        this.value = new byte[end - begin + 16];
        count = 0;
        for (int i = begin; i < end; i++)
            this.value[count++] = value[i];
    }

    public ByteBuilder(byte[] value)
    {
        int len = value.length;
        this.value = new byte[len + 16];
        count = 0;
        for (int i = 0; i < len; i++)
            this.value[count++] = value[i];
    }

    public int capacity()
    {
        return value.length;
    }

    public int length()
    {
        return count;
    }

    private int newCapacity(int minCapacity)
    {
        int newCapacity = (value.length << 1) + 2;
        if (newCapacity - minCapacity < 0)
        {
            newCapacity = minCapacity;
        }
        return newCapacity;
    }

    private void ensureCapacityInternal(int minimumCapacity)
    {
        if (minimumCapacity - value.length > 0)
        {
            value = Arrays.copyOf(value, newCapacity(minimumCapacity));
        }
    }

    public ByteBuilder append(byte b)
    {
        ensureCapacityInternal(count + 1);
        value[count++] = b;
        return this;
    }

    public ByteBuilder append(byte[] b)
    {
        int len = b.length;
        ensureCapacityInternal(count + len);
        for (int i = 0; i < len; i++)
            value[count++] = value[i];
        return this;
    }

    public ByteBuilder append(ByteBuilder b)
    {
        int len = b.length();
        ensureCapacityInternal(count + len);
        for (int i = 0; i < len; i++)
            value[count++] = value[i];
        return this;
    }

    public byte getByte(int index)
    {
        return value[index];
    }

    public int getInt(int index)
    {
        int x = (value[index] & 127) | (value[index] & 128);
        return x;
    }

    public byte[] getBytes()
    {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++)
            result[i] = value[i];
        return result;
    }

    public long toLong()
    {
        return toLong(0, this.count);
    }

    public long toLong(int begin, int end)
    {
        long ans = 0;
        for (int i = begin; i < end; i++)
        {
            int x = (value[i] & 127) | (value[i] & 128);
            ans |= (x << ((i - begin) << 3));
        }
        return ans;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            s.append(String.format("%02x", new Integer(value[i] & 0xFF)).toUpperCase());
        }
        return s.toString();
    }
}
