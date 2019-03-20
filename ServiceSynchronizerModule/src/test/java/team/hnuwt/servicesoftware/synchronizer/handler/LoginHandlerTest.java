package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LoginHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandlerTest.class);
    private ObjectMapper mapper = new ObjectMapper();
    private int state  =1;

    @Test
    public void runGeneMsgTest(){
        List<String> loginList = new ArrayList<>();
        loginList.add("1111111");
        loginList.add("2222222");
        logger.info(geneMsg(loginList));
    }

    private String geneMsg(List<String> loginIds){
        ObjectNode result = mapper.createObjectNode();
        ArrayNode addrs = mapper.createArrayNode();

        loginIds.forEach(addrs::add);
        result.set("addr", addrs);
        result.put("state", state);
        return result.toString();
    }
}
