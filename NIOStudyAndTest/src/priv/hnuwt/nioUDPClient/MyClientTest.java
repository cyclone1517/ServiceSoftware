package priv.hnuwt.nioUDPClient;

import java.io.IOException;
import java.util.Random;

public class MyClientTest {

    public static Random random = new Random();

    public static void main(String[] args) throws IOException {
        Thread myclient1 = new Thread(new MyClient(randomOx(),"localhost",9091));
        myclient1.start();
        Thread myclient2 = new Thread(new MyClient(randomOx(),"localhost",9091));
        myclient2.start();
    }

    public static String randomOx(){
        int temp = random.nextInt(1000000000);
        System.out.println(temp);
        return  temp + "";
    }
}
