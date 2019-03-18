package team.hnuwt.servicesoftware.synchronizer.constant;

import java.util.HashMap;

public enum MESSAGE {

    DATA, HEARTBEAT, LOGIN, LOGOUT;

    private static HashMap<String, MESSAGE> msgs;

    static {
        msgs = new HashMap<>();
        msgs.put("DATA", DATA);
        msgs.put("HEARTBEAT", HEARTBEAT);
        msgs.put("LOGIN", LOGIN);
        msgs.put("LOGOUT", LOGOUT);
    }

    public static MESSAGE getMSG(String key){
        return msgs.get(key.toUpperCase());
    }

}
