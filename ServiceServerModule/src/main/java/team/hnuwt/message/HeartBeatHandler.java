package team.hnuwt.message;

import java.nio.channels.SocketChannel;

import team.hnuwt.util.ByteBuilder;
import team.hnuwt.util.ConcentratorUtil;
import team.hnuwt.util.RedisUtil;

public class HeartBeatHandler implements Runnable {
    private ByteBuilder heartBeat;

    private SocketChannel sc;

    public HeartBeatHandler(SocketChannel sc, ByteBuilder heartBeat)
    {
        this.heartBeat = heartBeat;
        this.sc = sc;
    }

    @Override
    public void run()
    {
        long id = heartBeat.toLong(7, 12);
        RedisUtil.updateHeatBeat(String.valueOf(id));
        ConcentratorUtil.add(id, sc);
    }
}
