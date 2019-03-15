package team.hnuwt.servicesoftware.synchronizer.constant;

import java.util.HashMap;

public enum MESSAGE {

    DATA, HEARTBEAT;

    private static HashMap<String, MESSAGE> msgs;

    static {
        msgs = new HashMap<>();
        msgs.put("DATA", DATA);
        msgs.put("HEARTBEAT", HEARTBEAT);
    }

    public static MESSAGE getMSG(String key){
        return msgs.get(key.toUpperCase());
    }

}
