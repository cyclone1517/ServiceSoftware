package team.hnuwt.servicesoftware.simulation.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class UDPSendHandler implements Runnable {
    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;
    private static String ip;
    private static int port;

    private byte[] b;

    static {
        try {
            props = new Properties();
            props.load(UDPSendHandler.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            ip = props.getProperty("socket.ip");
            port = Integer.parseInt(props.getProperty("socket.udp.port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UDPSendHandler(long id, int num)
    {
        b = new byte[18 + 2 + 8 * num + 2];
        int len = 12 + 2 + 8 * num;
        int cnt = 0;
        b[cnt++] = (byte) 0x68;
        b[cnt++] = (byte) (((len & 63) << 2) | 1);
        b[cnt++] = (byte) (len >> 6);
        b[cnt++] = (byte) (((len & 63) << 2) | 1);
        b[cnt++] = (byte) (len >> 6);
        b[cnt++] = (byte) 0x68;
        b[cnt++] = (byte) 0x88;
        b[cnt++] = (byte) (id & 0xff);
        b[cnt++] = (byte) ((id >> 8) & 0xff);
        b[cnt++] = (byte) ((id >> 16) & 0xff);
        b[cnt++] = (byte) ((id >> 24) & 0xff);
        b[cnt++] = (byte) ((id >> 32) & 0xff);
        b[cnt++] = (byte) 0x8c;
        b[cnt++] = (byte) 0x60;
        b[cnt++] = (byte) 0x01;
        b[cnt++] = (byte) 0x00;
        b[cnt++] = (byte) 0x01;
        b[cnt++] = (byte) 0x07;
        b[cnt++] = (byte) (num & 255);
        b[cnt++] = (byte) (num >> 8);
        for (int i = 0; i < num; i++)
        {
            b[cnt++] = (byte) (i & 255);
            b[cnt++] = (byte) (i >> 8);
            long data = i + 12345678;
            b[cnt++] = (byte) (data % 10 + ((data / 10 % 10) << 4));
            data /= 100;
            b[cnt++] = (byte) (data % 10 + ((data / 10 % 10) << 4));
            data /= 100;
            b[cnt++] = (byte) (data % 10 + ((data / 10 % 10) << 4));
            data /= 100;
            b[cnt++] = (byte) (data % 10 + ((data / 10 % 10) << 4));
            int f = (i & 1);
            b[cnt++] = (byte) (f & 255);
            b[cnt++] = (byte) (f >> 8);
        }
        byte sum = 0;
        for (int i = 6; i < cnt; i++)
            sum += b[i];
        b[cnt++] = (byte) sum;
        b[cnt++] = (byte) 0x16;
    }

    @Override
    public void run()
    {
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(b, b.length, address, port);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
