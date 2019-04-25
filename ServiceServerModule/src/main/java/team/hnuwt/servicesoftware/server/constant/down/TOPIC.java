package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;

public enum TOPIC {

    DOWNSTREAM("DOWNSTREAM"),   /* 来自中间服务的命令，或来根据心跳分析的过期资源关闭请求 */
    UPSTREAM("UPSTREAM"),       /* 上传到中间服务 */
    DIRECT("DIRECT"),           /* 透明转发 */
    PROTOCOL("PROTOCOL"),       /* 前置机内部通讯用 */
    TEST("TEST");               /* 测试用 */

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
        topics.put("TEST", TEST);
    }

    @Override
    public String toString(){
        return name;
    }

}
