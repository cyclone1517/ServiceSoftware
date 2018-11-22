package team.hnuwt.message;

import team.hnuwt.util.RedisUtil;

public class HeartBeatHandler implements Runnable {
    private String heartBeat;
    
    public HeartBeatHandler(String heartBeat)
    {
        this.heartBeat = heartBeat;
    }

    @Override
    public void run() {
        RedisUtil.updateHeatBeat(heartBeat.toString());
    }
}
