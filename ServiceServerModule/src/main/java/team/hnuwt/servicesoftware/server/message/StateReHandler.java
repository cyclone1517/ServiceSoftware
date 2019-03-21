package team.hnuwt.servicesoftware.server.message;

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

import java.nio.channels.SocketChannel;

/**
 * 抄表回复处理类
 */
public class StateReHandler implements Runnable{

    private ByteBuilder meterData;
    private SocketChannel sc;
    private boolean success;
    private Logger logger = LoggerFactory.getLogger(StateReHandler.class);
    private static final int[] fieldlen = {4, 8};       /* 数据域单元每个域的长度，表序号4位，表读数8位，阀门状态2位 */

    public StateReHandler(SocketChannel sc, ByteBuilder meterData, boolean success){
        this.sc = sc;
        this.meterData = meterData;
        this.success = success;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        String result = meterData.toString();

        String addr = result.substring(CONSTANT.ADD_START, CONSTANT.ADD_END);
        String addrId = FieldPacker.toIntAddrId(addr);


        root.put("addr", addrId);
        root.put("success", success);

        ProduceUtil.addQueue(TOPIC.UPSTREAM.getStr(), TAG.OPER_RE.getStr(), root.toString());

    }
}
