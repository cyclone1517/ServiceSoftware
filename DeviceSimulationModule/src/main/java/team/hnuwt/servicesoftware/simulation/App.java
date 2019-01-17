package team.hnuwt.servicesoftware.simulation;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Properties;
import java.util.Scanner;

import team.hnuwt.servicesoftware.simulation.service.Client;
import team.hnuwt.servicesoftware.simulation.service.TCPReadHandler;
import team.hnuwt.servicesoftware.simulation.service.TCPSendHandler;
import team.hnuwt.servicesoftware.simulation.service.UDPSendHandler;

public class App {

    private final static String APPLICATION_FILE = "application.properties";

    private static Properties props;

    private static Scanner in = new Scanner(System.in);
    private static int num, meterNum;

    public static void main(String[] args) throws IOException {
        init();

        System.out.println("Please choose tcp(t) or udp(u).");
        String s = in.nextLine();
        while (!("t".equals(s) || "T".equals(s) || "u".equals(s) || "U".equals(s)))
        {
            System.out.println("Please choose tcp(t) or udp(u).");
            s = in.nextLine();
        }

        System.out.println("Please enter the number of concentrator and the number of meter in a concentrator.");
        num = in.nextInt();
        meterNum = in.nextInt();

        if ("u".equals(s) || "U".equals(s))
        {
            udp();
        } else
        {
            tcp();
        }
    }

    private static void udp()
    {
        System.out.println("Please enter 'd' to start sending data, or 'c' to close.");
        String s = in.nextLine();
        s = in.nextLine();
        while (!("c".equals(s) || "C".equals(s)))
        {
            if ("d".equals(s) || "D".equals(s))
            {
                Thread t[] = new Thread[num];
                for (int i = 0; i < num; i++)
                    t[i] = new Thread(new UDPSendHandler((long) i, meterNum));
                for (int i = 0; i < num; i++)
                    t[i].start();
            }
            System.out.println("Please enter 'd' to start sending data, or 'c' to close.");
            s = in.nextLine();
        }
    }

    private static void tcp() throws IOException
    {
        Selector selector = Selector.open();
        Client c[] = new Client[num];
        for (int i = 0; i < num; i++)
            c[i] = new Client(i, selector);

        Thread readThread = new Thread(new TCPReadHandler(selector));
        readThread.start();

        System.out
                .println("Please enter 'h' to start sending heartbeat, or 'd' to start sending data, or 'c' to close.");
        String s = in.nextLine();
        s = in.nextLine();
        while (!("c".equals(s) || "C".equals(s)))
        {
            if ("h".equals(s) || "H".equals(s))
            {
                for (int i = 0; i < num; i++)
                {
                    TCPSendHandler sh = new TCPSendHandler((long) i, meterNum, c[i].sc, 1);
                    sh.run();
                }
            } else if ("d".equals(s) || "D".equals(s))
            {
                Thread t[] = new Thread[num];
                for (int i = 0; i < num; i++)
                    t[i] = new Thread(new TCPSendHandler((long) i, meterNum, c[i].sc, 0));
                for (int i = 0; i < num; i++)
                    t[i].start();
            }
            System.out.println(
                    "Please enter 'h' to start sending heartbeat, or 'd' to start sending data, or 'c' to close.");
            s = in.nextLine();
        }

        readThread.interrupt();
        for (int i = 0; i < num; i++)
            c[i].close();
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
