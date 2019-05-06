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
 * 操作成败回复处理类
 */
public class StateReHandler implements Runnable{

    private ByteBuilder data;
    private TAG tag;
    private boolean success;
    private Logger logger = LoggerFactory.getLogger(StateReHandler.class);

    public StateReHandler(ByteBuilder data, TAG tag, boolean success){
        this.data = data;
        this.success = success;
        this.tag = tag;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        long addr = FieldPacker.getId(data);

        root.put("termAddr", addr);       // 集中器编号
        root.put("success", success);       // 操作结果

        ProduceUtil.addQueue(TOPIC.UPSTREAM.getStr(), tag.getStr(), root.toString());

    }
}
