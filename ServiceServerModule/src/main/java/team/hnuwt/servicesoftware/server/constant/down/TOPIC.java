package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;

public enum TOPIC {

    DOWNSTREAM, UPSTREAM, DIRECT;

    private static HashMap<String, TOPIC> topics;

    public static TOPIC getTopic(String key){
        return topics.get(key.toUpperCase());
    }

    static {
        topics = new HashMap<>();

        topics.put("DOWNSTREAM", DOWNSTREAM);
        topics.put("UPSTREAM", UPSTREAM);
        topics.put("DIRECT", DIRECT);
    }

}
