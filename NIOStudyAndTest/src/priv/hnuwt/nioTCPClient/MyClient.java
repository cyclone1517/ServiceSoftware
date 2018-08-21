package priv.hnuwt.nioTCPClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient implements Runnable{

    private Socket socket;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private final String meterCode;

    public MyClient(String meterCode) throws IOException {
        System.out.println("a new client has been created");
        this.socket = new Socket("localhost", 9090);
        this.outputStream = socket.getOutputStream();   //字符输出流
        this.printWriter = new PrintWriter(outputStream);
        this.meterCode = meterCode;
    }

    public void sendHeartBeat() throws IOException {
        printWriter.write("ENNPINGLSD" + meterCode);
        printWriter.flush();
    }

    @Override
    public void run() {
        while(true){
            try {
                sendHeartBeat();
                //System.out.println(meterCode + "is alive");
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
