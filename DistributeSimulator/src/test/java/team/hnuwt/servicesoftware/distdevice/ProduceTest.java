package team.hnuwt.servicesoftware.distdevice;

import org.junit.Test;
import team.hnuwt.servicesoftware.disdevice.mode.Archive;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author yuanlong chen
 * 来自中间服务的消息示例
 */
public class ProduceTest {

    @Test
    public void choseOrder(){
        Scanner s = new Scanner(System.in);
        boolean out = false;
        while (true){
            System.out.println("please enter a choice...\n");
            int choice = s.nextInt();
            switch (choice){
                case -1:
                    out = true;
                    break;
                case 1:
                    sendReadMeter();
                    break;
                case 2:
                    sendOn();
                    break;
                case 3:
                    sendArchive();
                    break;
            }

            if (out) break;
        }
    }

    @Test
    public void sendReadMeter() {
        String topic = "DOWNSTREAM";
        String tag = "READ_METER";
        String msg = ProduceTestUtil.geneReadMeterJson("1044", "0002");
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
    public void sendArchive(){
//        String topic = "TEST";
        String topic = "DOWNSTREAM";
        String tag = "ARCHIVE_DOWNLOAD";

        List<Archive> archives = new ArrayList<>();
        Archive ac1 = new Archive();
        ac1.setId("0300");
        ac1.setMadd("180910810100");
        ac1.setPort("00");
        ac1.setPrtc("00");
        ac1.setCadd("00000304");
        archives.add(ac1);

//        Archive ac2 = new Archive();
//        ac2.setId("0200");
//        ac2.setMadd("630006090000");
//        ac2.setPort("04");
//        ac2.setPrtc("01");
//        ac2.setCadd("00000000");
//        archives.add(ac2);
//
//        Archive ac3 = new Archive();
//        ac3.setId("0300");
//        ac3.setMadd("010000000000");
//        ac3.setPort("04");
//        ac3.setPrtc("01");
//        ac3.setCadd("00000000");
//        archives.add(ac3);

        String msg = ProduceTestUtil.geneArchive("1004", archives.size(), archives);
        ProduceUtil.addQueue(topic, tag, msg);

    }

    @Test
    public void sendCloseArchive(){
        String topic = "DOWNSTREAM";
        String tag = "ARCHIVE_CLOSE";
        List<String> meterIds = new ArrayList<>();
        meterIds.add("0100");
        meterIds.add("0200");
        meterIds.add("0300");

        String msg = ProduceTestUtil.geneCloseArchive("1021", meterIds);
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
        String msg = ProduceTestUtil.geneUploadOnOffJson("1004","0001");
        ProduceUtil.addQueue(topic, tag, msg);
    }

    @Test
    public void setUploadOff(){
        String topic = "DOWNSTREAM";
        String tag = "UPLOAD_OFF";
        String msg = ProduceTestUtil.geneUploadOnOffJson("1004", "0001");
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

    private void waitForSending(){
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
