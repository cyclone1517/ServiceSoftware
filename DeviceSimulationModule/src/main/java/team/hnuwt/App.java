package team.hnuwt;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

import team.hnuwt.service.Send;

public class App {
	
	private final static String APPLICATION_FILE = "application.properties";
	
	private static Properties props;

    public static void main(String[] args) throws UnknownHostException, IOException {
    	init();
    	Scanner in = new Scanner(System.in);
    	System.out.println("Please enter the number of thread and the number of data would send in a thread.");
        int num = in.nextInt();
        int meterNum = in.nextInt();
        
        String pkg = "68";
        pkg += reverse(String.format("%04x", meterNum * 7 + 14)).toUpperCase();
        pkg += reverse(String.format("%04x", meterNum * 7 + 14)).toUpperCase();
        pkg += "688803130100008C6900000107";
        pkg += reverse(String.format("%04x", meterNum)).toUpperCase();
        for (int i = 0; i < meterNum; i++)
        {
        	pkg += reverse(String.format("%04x", i)).toUpperCase();
        	pkg += reverse(String.format("%08x", i)).toUpperCase();
        	pkg += reverse(String.format("%02x", i%2)).toUpperCase();
        }
        pkg += "F716";
        
        Socket socket[] = new Socket[num];
        for (int i = 0; i < num; i++)
        {
        	socket[i] = new Socket(props.getProperty("socket.ip"), Integer.parseInt(props.getProperty("socket.port")));
        }
        
        System.out.println("Please enter 'y' to start sending.");
        String s = in.nextLine();
        s = in.nextLine();
        if ("y".equals(s) || "Y".equals(s))
        {
	        for (int i = 0; i < num; i++)
	        {
	            new Thread(new Send(pkg, socket[i])).start();
	        }
        }
    }
    
    private static String reverse(String s)
    {
        StringBuilder sb = new StringBuilder(s);
        StringBuilder result = new StringBuilder();
        for (int i = sb.length() - 1; i > 0; i -= 2)
        {
            result.append(sb.substring(i-1, i+1));
        }
        return result.toString();
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
