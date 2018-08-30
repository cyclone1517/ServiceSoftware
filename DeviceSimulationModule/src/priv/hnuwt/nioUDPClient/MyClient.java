package priv.hnuwt.nioUDPClient;

import java.io.IOException;
import java.net.*;

public class MyClient implements Runnable{

    private String meterCode;
    private String serverAddress;
    private int serverPort;

    public MyClient(String meterCode, String serverAddress, int serverPort){
        this.meterCode = meterCode;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void send() {
        byte[] msg = ("FEFE" + meterCode).getBytes();
        DatagramSocket datagramSocket = null;
        //创建一个数据报
        try {
            datagramSocket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(msg, 0, msg.length,
                    InetAddress.getByName(serverAddress), serverPort);
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    @Override
    public void run() {
        while (true){
            send();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
