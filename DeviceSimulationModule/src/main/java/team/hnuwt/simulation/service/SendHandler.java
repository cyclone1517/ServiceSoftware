package team.hnuwt.simulation.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendHandler implements Runnable {
    private SocketChannel sc;

    private byte[] b;

    public SendHandler(long id, int num, SocketChannel sc, int flag)
    {
        this.sc = sc;
        if (flag == 1)
        {
            b = new byte[] { 0x68, 0x35, 0x00, 0x35, 0x00, 0x68, (byte) 0xc9, (byte) (id & 0xff),
                    (byte) ((id >> 8) & 0xff), (byte) ((id >> 16) & 0xff), (byte) ((id >> 24) & 0xff),
                    (byte) ((id >> 32) & 0xff), 0x02, 0x70, 0x10, 0x00, 0x04, 0x00, 0x22, 0x00, 0x16 };
        } else
        {
            b = new byte[18 + 2 + 7 * num + 2];
            int len = 12 + 2 + 7 * num;
            int cnt = 0;
            b[cnt++] = (byte) 0x68;
            b[cnt++] = (byte) ((len & 63) << 2);
            b[cnt++] = (byte) (len >> 6);
            b[cnt++] = (byte) ((len & 63) << 2);
            b[cnt++] = (byte) (len >> 6);
            b[cnt++] = (byte) 0x68;
            b[cnt++] = (byte) 0x88;
            b[cnt++] = (byte) (id & 0xff);
            b[cnt++] = (byte) ((id >> 8) & 0xff);
            b[cnt++] = (byte) ((id >> 16) & 0xff);
            b[cnt++] = (byte) ((id >> 24) & 0xff);
            b[cnt++] = (byte) ((id >> 32) & 0xff);
            b[cnt++] = (byte) 0x8c;
            b[cnt++] = (byte) 0x69;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x01;
            b[cnt++] = (byte) 0x07;
            b[cnt++] = (byte) (num & 255);
            b[cnt++] = (byte) (num >> 8);
            for (int i = 0; i < num; i++)
            {
                b[cnt++] = (byte) (i & 255);
                b[cnt++] = (byte) (i >> 8);
                long data = i + 100;
                b[cnt++] = (byte) (data & 255);
                b[cnt++] = (byte) ((data >> 8) & 255);
                b[cnt++] = (byte) ((data >> 16) & 255);
                b[cnt++] = (byte) ((data >> 32) & 255);
                int f = (i & 1);
                b[cnt++] = (byte) f;
            }
            byte sum = 0;
            for (int i = 6; i < cnt; i++)
                sum += b[i];
            b[cnt++] = (byte) sum;
            b[cnt++] = (byte) 0x16;
        }
    }

    @Override
    public void run()
    {
        try {
            sc.write(ByteBuffer.wrap(b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
