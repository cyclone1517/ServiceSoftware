package team.hnuwt.netservice;

import java.io.IOException;

public class ServiceSoftwareDemo {
    public static void main(String[] args){
        try {
            Reactor reactor = new Reactor(9090, 9091);
            reactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
