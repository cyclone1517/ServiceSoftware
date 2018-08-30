package priv.hnuwt.nioTCPClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class MyClient implements Runnable{

    private Socket socket;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private final String meterCode;

    //临时测试变量
    private final String testUpdateData;
    private Random random = new Random();

    public MyClient(String meterCode) throws IOException {
        System.out.println("a new client has been created");
        this.socket = new Socket("localhost", 9090);
        this.outputStream = socket.getOutputStream();   //字符输出流
        this.printWriter = new PrintWriter(outputStream);
        this.meterCode = meterCode;
        //上行数据包测试示例
        this.testUpdateData = "AA00002B0002050202A28DB910E0B92E0472060000000000" +
                "000000000011071C00570100000000000000000020E837003CFF";
    }

    //上传心跳数据
    public void sendHeartBeat() throws IOException {
        printWriter.write("ENNPINGLSD" + meterCode);
        printWriter.flush();
    }

    //上传上行数据
    public void sendUpdateData(String data) throws IOException {
        printWriter.write(data);
        printWriter.flush();
    }

    @Override
    public void run() {
        while(true){
            //随机传输心跳包或上行数据包
            try {
                if (random.nextInt(10) == 1) {
                    sendHeartBeat();
                    System.out.println(meterCode + "updated a heart beat message");
                } else {
                    sendUpdateData(testUpdateData);
                    System.out.println(meterCode + "updated a service message");
                }
                Thread.sleep(5000 + random.nextInt(5000));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
