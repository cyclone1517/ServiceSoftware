package team.hnuwt.servicesoftware.distdevice.test;

import org.junit.Test;

/**
 * @author yuanlong chen
 * 来自中间服务的消息示例
 */
public class ProduceTest {

    @Test
    public void sendReadMeter() {
        String topic = "DOWNSTREAM";
        String tag = "READ_METER";
        String msg = ProduceTestUtil.geneReadMeterJson("1021");
        ProduceUtil.addQueue(topic, tag, msg);
    }

    @Test
    public void sendOn(){
        String topic = "DOWNSTREAM";
        String tag = "CTRL_ON";
        String msg = ProduceTestUtil.geneCtrlOnOffJson("0008");
        ProduceUtil.addQueue(topic, tag, msg);
    }

    @Test
    public void sendClose(){
        String topic = "DOWNSTREAM";
        String tag = "CTRL_OFF";
        String msg = ProduceTestUtil.geneCtrlOnOffJson("0008");
        ProduceUtil.addQueue(topic, tag, msg);
    }


    @Test
    public void testLen(){
        System.out.println("686900690068880000FD03008C74000001070200080000000015080000000015CC16".substring(14,24));
    }

    @Test
    public void packNumTest(){
        System.out.println(Integer.parseInt("0008"));
    }

    @Test
    public void setUploadOn(){
        String topic = "DOWNSTREAM";
        String tag = "UPLOAD_ON";
        String msg = ProduceTestUtil.geneUploadOnOffJson("0008");
        ProduceUtil.addQueue(topic, tag, msg);
    }

    @Test
    public void setUploadOff(){
        String topic = "DOWNSTREAM";
        String tag = "UPLOAD_OFF";
        String msg = ProduceTestUtil.geneUploadOnOffJson("0008");
        ProduceUtil.addQueue(topic, tag, msg);
    }

    @Test
    public void setReadOff(){
        String topic = "DOWNSTREAM";
        String tag = "READ_UPLOAD";
        String msg = ProduceTestUtil.geneReadUploadJson("0008");
        ProduceUtil.addQueue(topic, tag, msg);
    }

    @Test
    public void mimicAutoUpload(){
        String topic = "PROTOCOL";
        String tag = "AUTO_UPLOAD";
        String msg = "685500550068880000FD03008C60100001070100010000000015FFA216";
        ProduceUtil.addQueue(topic, tag, msg);

    }

}
