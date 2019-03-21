package team.hnuwt.servicesoftware.server.compatible;

import java.util.TimerTask;

/**
 * 集中器心跳模拟定时任务，假设本机是集中器1021
 */
@Deprecated
public class HeartBeatTask extends TimerTask {

    private CollecSimulator clecSim;
    private static final String TOPIC = "AGENCY_HEARTBEAT";
    private static final String myHeart = "683500350068C90000FD0300027010000400126116";

    public HeartBeatTask(CollecSimulator clecSim){
        this.clecSim = clecSim;
    }

    @Override
    public void run() {
        clecSim.sendMsg(TOPIC, myHeart);
    }
}
