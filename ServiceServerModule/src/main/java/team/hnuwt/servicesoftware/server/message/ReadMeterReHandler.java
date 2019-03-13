package team.hnuwt.servicesoftware.server.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.up.CONSTANT;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.FieldPacker;
import team.hnuwt.servicesoftware.server.util.ProduceUtil;

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
        String numStr = FieldPacker.reverseEnd(result.substring(CONSTANT.NUM_START, CONSTANT.NUM_END));
        int num = 0;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e){
            logger.error("return number string packing error:" + numStr);
            return;
        }

        root.put("addr", addr);
        root.put("num", num);
        root.set("data", geneMeterData(result.substring(CONSTANT.DATA_LOC), num, mapper));


        ProduceUtil.addQueue("UPSTREAM", "READ_METER", root.toString());

    }

    private ArrayNode geneMeterData(String userStr, int num, ObjectMapper mapper){
        ArrayNode result = mapper.createArrayNode();

        int locate = 0;
        for (int i=0; i<num; i++){
            ObjectNode meter = mapper.createObjectNode();
            meter.put("id", FieldPacker.reverseEnd(userStr.substring(locate, locate + fieldlen[0])));
            locate += fieldlen[0];
            meter.put("read", userStr.substring(locate, locate + fieldlen[1]));
            locate += fieldlen[1];

            result.add(meter);
        }

        return result;
    }
}
