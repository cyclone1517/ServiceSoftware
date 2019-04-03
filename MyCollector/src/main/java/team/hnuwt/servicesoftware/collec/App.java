package team.hnuwt.servicesoftware.collec;

import team.hnuwt.servicesoftware.collec.simu.Collector;
import team.hnuwt.servicesoftware.collec.util.ByteBuilder;

import java.nio.ByteBuffer;

public class App {

    public static void main(String[] args)
    {
        int num = 1;
        String head = "683500350068C9";
        String loginProp = "02701000010012";
        String heartProp = "02701000040012";
        int baseLoginCS = Integer.parseInt("5D", 16);
        int baseHeartCS = Integer.parseInt("60", 16);
        String tail = "16";
        int baseAddr = 1020;

        for (int i=1; i<num+1; i++){        // 增量从1开始记，共num个表
            String loginStr = head + toHexAddrId(baseAddr + i) + loginProp + Integer.toHexString((baseLoginCS + i) % 256) + tail;
            String heartStr = head + toHexAddrId(baseAddr + i) + heartProp + Integer.toHexString((baseHeartCS + i) % 256) + tail;

            Collector collector = new Collector(loginStr, heartStr);
            collector.run();
        }

//        MainReactor mainReactor = new MainReactor();
//        TCPMessageHandler.openTCPCompatible();

//        启动集中器连接和数据读取侦听
//        new Thread(mainReactor).start();

    }

    private static String toHexAddrId(int addr){
        return "0000" + getNBitHexNum(addr, 4) + "00";
    }

    private static String getNBitHexNum(int num, int bit){   /* 自带反转 */
        StringBuilder result = new StringBuilder();
        result.append(Integer.toHexString(num));
        while (result.length() < bit) result.insert(0, "0");
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

}
