package team.hnuwt.servicesoftware.prtcplugin.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.prtcplugin.constant.TAG;
import team.hnuwt.servicesoftware.prtcplugin.constant.TOPIC;
import team.hnuwt.servicesoftware.prtcplugin.model.Meter;
import team.hnuwt.servicesoftware.prtcplugin.model.publish.MeterInfo;
import team.hnuwt.servicesoftware.prtcplugin.model.publish.PubAutoUpload;
import team.hnuwt.servicesoftware.prtcplugin.packet.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送数据到中间服务
 * 尽管之前都在前置机模块完成，迁移到这边可简化工作（这个模块自带解析）
 */
public class PublishUtil {

    //private static final String DATA = "Data";
    private static final Logger logger = LoggerFactory.getLogger(PublishUtil.class);

    public static void publishAutoUpload(PacketAutoUpload pkg){
        String pubStr = getAutoUploadStr(pkg);
        ProduceUtil.addQueue(TOPIC.UPSTREAM.getStr(), TAG.AUTO_UPLOAD.getStr(), pubStr);
    }

    private static String getAutoUploadStr(PacketAutoUpload pkg){
        PubAutoUpload pubAutoUpload = new PubAutoUpload();

        /* 组装中间服务需要的字段 */
        pubAutoUpload.setTermAddr(pkg.getAddress());
        pubAutoUpload.setCount(pkg.getNumber());
        pubAutoUpload.setMeter(transMeterStruct(pkg.getMeter()));

        return JSON.toJSONString(pubAutoUpload);
    }

    /*
     * 因为改了字段名，要做一个赋值转换
     */
    private static List<MeterInfo> transMeterStruct(List<Meter> mtList){
        List<MeterInfo> result = new ArrayList<>();

        mtList.forEach(mt -> {
            String stateBinStr = Integer.toBinaryString(mt.getState());
            result.add(new MeterInfo(
                    mt.getId(),
                    mt.getData(),
                    stateBinStr.substring(stateBinStr.length()-2)));
        });

        return result;
    }

}
