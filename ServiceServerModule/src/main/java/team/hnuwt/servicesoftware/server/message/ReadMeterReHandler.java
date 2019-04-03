package team.hnuwt.servicesoftware.server.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.constant.down.TOPIC;
import team.hnuwt.servicesoftware.server.constant.up.CONSTANT;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.FieldPacker;
import team.hnuwt.servicesoftware.server.util.ProduceUtil;

import java.io.File;
import java.nio.channels.SocketChannel;

/**
 * 抄表回复处理类
 */
public class ReadMeterReHandler implements Runnable{

    private ByteBuilder meterData;
    private SocketChannel sc;
    private Logger logger = LoggerFactory.getLogger(ReadMeterReHandler.class);
    private static final int[] fieldlen = {4, 8};       /* 数据域单元每个域的长度，表序号4位，表读数8位，阀门状态2位 */

    public ReadMeterReHandler(SocketChannel sc, ByteBuilder meterData){
        this.sc = sc;
        this.meterData = meterData;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        String result = meterData.toString();

        String addr = result.substring(CONSTANT.ADD_START, CONSTANT.ADD_END);
        String addrId = FieldPacker.toIntAddrId(addr);
        String numStr = FieldPacker.reverseEnd(result.substring(CONSTANT.NUM_START, CONSTANT.NUM_END));
        int num = 0;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e){
            logger.error("return number string packing error:" + numStr);
            return;
        }

        root.put("termAddr", addrId);
        root.put("count", num);
        root.set("meter", geneMeterData(result.substring(CONSTANT.DATA_LOC), num, mapper));
        root.put("time", FieldPacker.getSysTime());

        /*
         * 返回给中间服务
         * 还没有做刷新本地数据库的需求
         */
        ProduceUtil.addQueue(TOPIC.UPSTREAM.getStr(), TAG.READ_METER.getStr(), root.toString());

    }

    private JsonNode geneMeterData(String userStr, int num, ObjectMapper mapper){
        int locate = 0;

        if (num > 1) {
            ArrayNode result = mapper.createArrayNode();
            for (int i = 0; i < num; i++) {
                ObjectNode meter = mapper.createObjectNode();
                meter.put("meterAddr", FieldPacker.reverseEnd(userStr.substring(locate, locate + fieldlen[0])));
                locate += fieldlen[0];
                meter.put("readValue", FieldPacker.reverseEnd(userStr.substring(locate, locate + fieldlen[1])).substring(0, 6));
                locate += fieldlen[1];

                result.add(meter);
            }
            return result;

        }
        else if (num == 1){
            ArrayNode result = mapper.createArrayNode();
            ObjectNode meter = mapper.createObjectNode();
            meter.put("meterAddr", FieldPacker.reverseEnd(userStr.substring(locate, locate + fieldlen[0])));
            locate += fieldlen[0];
            meter.put("readValue", FieldPacker.reverseEnd(userStr.substring(locate, locate + fieldlen[1])).substring(0, 6));
            locate += fieldlen[1];
            String stateStr = FieldPacker.parseHexStr2Byte(userStr.substring(locate, locate + 2));
            if (stateStr != null && stateStr.length() > 2) {
                meter.put("state", stateStr.substring(stateStr.length() - 2));
            }
            result.add(meter);
            return result;
        }
        else {
            return null;
        }
    }
}
