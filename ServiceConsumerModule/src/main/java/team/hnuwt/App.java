package team.hnuwt;

import team.hnuwt.service.DataCarryManager;
import team.hnuwt.util.ConsumerUtil;

public class App {
    
    public static void main(String[] args) {
        
        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();
        
        new DataCarryManager().run();
    }

}
