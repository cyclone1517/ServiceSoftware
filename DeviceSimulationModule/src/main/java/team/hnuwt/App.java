package team.hnuwt;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.channels.Selector;
import java.util.Properties;
import java.util.Scanner;

import team.hnuwt.service.Client;
import team.hnuwt.service.ReadHandler;
import team.hnuwt.service.SendHandler;

public class App {

    private final static String APPLICATION_FILE = "application.properties";

    private static Properties props;

    public static void main(String[] args) throws UnknownHostException, IOException {
        init();
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the number of concentrator and the number of meter in a concentrator.");
        int num = in.nextInt();
        int meterNum = in.nextInt();
        
        Selector selector = Selector.open();
        Client c[] = new Client[num];
        for (int i = 0; i < num; i++) c[i] = new Client(i, selector);
        
        Thread readThread = new Thread(new ReadHandler(selector));
        readThread.start();
        
        System.out.println("Please enter 'h' to start sending heartbeat, or 'd' to start sending data, or 'c' to close.");
        String s = in.nextLine();
        s = in.nextLine();
        while (!("c".equals(s) && !"C".equals(s)))
        {
            if ("h".equals(s) || "H".equals(s)) 
            {
                for (int i = 0; i < num; i++) 
                {
                    SendHandler sh = new SendHandler((long)i, meterNum, c[i].sc, 1);
                    sh.run();
                }
            } else if ("d".equals(s) || "D".equals(s)) 
            {
                Thread t[] = new Thread[num];
                for (int i = 0; i < num; i++) t[i] = new Thread(new SendHandler((long)i, meterNum, c[i].sc, 0));
                for (int i = 0; i < num; i++) t[i].start();
            }
            System.out.println("Please enter 'h' to start sending heartbeat, or 'd' to start sending data, or 'c' to close.");
            s = in.nextLine();
        }
        
        readThread.interrupt();
        for (int i = 0; i < num; i++) c[i].close();
    }
    
    
    private static void init()
    {
        try {
            props = new Properties();
            props.load(App.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
