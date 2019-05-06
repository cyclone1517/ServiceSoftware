package team.hnuwt.servicesoftware.collec;

import team.hnuwt.servicesoftware.collec.simu.Collector;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * @author yuanlong chen
 * 集中器模拟类，可输入启动参数 线程个数/循环次数/心跳周期(秒)
 */
public class App {

    private static final String APPLICATION_FILE = "application.properties";
    private static Properties prop;

    /* 加载类变量的静态代码块 */
    static {
        prop = new Properties();
        try {
            prop.load(Collector.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

        int num = Integer.parseInt(prop.getProperty("simu.thread.num"));
        int loop = Integer.parseInt(prop.getProperty("simu.thread.loop"));
        int interval = Integer.parseInt(prop.getProperty("simu.thread.interval"));
        int delayVary = Integer.parseInt(prop.getProperty("simu.delay.vary"));
        int baseAddr = Integer.parseInt(prop.getProperty("simu.start.id"));
        boolean openLogger = Boolean.valueOf(prop.getProperty("logger.open"));
        Random random = new Random();

        System.out.println("Parameters-------\nnum: " + num + "\nloop:" + loop + "\ninterval:" + interval +
                "\ndelayVary:" + delayVary + "\n-----------------");

        String head = "683500350068C9";
        String loginProp = "02701000010012";
        String heartProp = "02701000040012";
        String tail = "16";

        for (int i=0; i<num; i++){        // 增量从1开始记，共num个表

            // 生成登录报文
            String loginPrev = head + toHexAddrId(baseAddr + i) + loginProp;
            String loginCs = calcuCs(loginPrev);
            String login = loginPrev + loginCs + tail;

            // 生成心跳报文
            String heartPrev = head + toHexAddrId(baseAddr + i) + heartProp;
            String heartCs = calcuCs(heartPrev);
            String heart = heartPrev + heartCs + tail;

            Collector collector = new Collector(login, heart, loop, interval, random.nextInt(delayVary), openLogger, baseAddr + i);
            new Thread(collector).start();
        }
    }

    private static String toHexAddrId(int addr){
        return "0000" + getNBitHexNum(addr, 4) + "00";
    }

    private static String getNBitHexNum(int num, int bit){   /* 自带反转 */
        StringBuilder result = new StringBuilder();
        result.append(Integer.toHexString(num));
        while (result.length() < bit) {
            result.insert(0, "0");
        }
        return reverseEnd(result.toString());
    }

    private static String reverseEnd(String code){
        StringBuilder result = new StringBuilder();

        int len = code.length();
        for (int i=len-2; i>=0; i-=2){
            result.append(code, i, i+2);
        }

        return result.toString();
    }

    public static String calcuCs(String prev){
        String csStr = prev.substring(12);

        int sum = 0;
        for (int i = 0; i < csStr.length(); i += 2)
        {
            sum += Integer.parseInt(csStr.substring(i, i + 2), 16);
        }

        return getNBitHexNum(sum%256, 2);
    }

}
