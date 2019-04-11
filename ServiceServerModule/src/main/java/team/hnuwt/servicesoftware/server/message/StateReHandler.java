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
    private TAG tag;
    private boolean success;
    private Logger logger = LoggerFactory.getLogger(StateReHandler.class);

    public StateReHandler(ByteBuilder meterData, TAG tag, boolean success){
        this.meterData = meterData;
        this.success = success;
        this.tag = tag;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        String result = meterData.toString();

        String addr = result.substring(CONSTANT.ADD_START, CONSTANT.ADD_END);
        String addrId = FieldPacker.toIntAddrId(addr);


        root.put("addr", addrId);       // 集中器编号
        root.put("bustype", tag.getStr());  // 业务类型
        root.put("success", success);   // 操作结果

        ProduceUtil.addQueue(TOPIC.UPSTREAM.getStr(), TAG.OPER_RE.getStr(), root.toString());

    }
}
