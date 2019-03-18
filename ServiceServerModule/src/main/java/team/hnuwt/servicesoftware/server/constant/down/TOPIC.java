package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;

public enum TOPIC {

    DOWNSTREAM("DOWNSTREAM"),
    UPSTREAM("UPSTREAM"),
    DIRECT("DIRECT"),
    PROTOCOL("PROTOCOL");

    private String name;
    TOPIC(String name){
        this.name = name;
    }
    private static HashMap<String, TOPIC> topics;

    public static TOPIC getTopic(String key){
        return topics.get(key.toUpperCase());
    }

    public String getStr(){
        return name;
    }

    static {
        topics = new HashMap<>();

        topics.put("DOWNSTREAM", DOWNSTREAM);
        topics.put("UPSTREAM", UPSTREAM);
        topics.put("DIRECT", DIRECT);
        topics.put("PROTOCOL", PROTOCOL);
    }

    @Override
    public String toString(){
        return name;
    }

}
