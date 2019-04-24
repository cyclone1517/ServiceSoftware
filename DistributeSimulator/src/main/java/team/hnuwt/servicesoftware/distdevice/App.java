package team.hnuwt.servicesoftware.distdevice;


import org.junit.Test;
import team.hnuwt.servicesoftware.distdevice.util.InnerProduceUtil;

/**
 * @author yuanlong chen
 * 中间服务模拟模块，可以下发和接受报文
 */
public class App {

    @Test
    public void sendAutoUpload(){
        String data = "685500550068880000EC03008C60100001070100080000000015FF9816";
        InnerProduceUtil.addQueue("PROTOCOL", "AUTO_UPLOAD", data);
    }

}
