package team.hnuwt.servicesoftware.synchronizer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HandlerHelper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(HandlerHelper.class);

    public static JsonNode getJsonNodeRoot(String loginStateNode){
        try {
            return mapper.readTree(loginStateNode);
        } catch (IOException e) {
            logger.error("NOT A LOGIN/LOGOUT FORMAT" ,e);
            return null;
        }
    }
}
