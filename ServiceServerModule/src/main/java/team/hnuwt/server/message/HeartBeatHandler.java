package team.hnuwt.server.message;

import java.nio.channels.SocketChannel;

import team.hnuwt.server.util.ByteBuilder;
import team.hnuwt.server.util.ConcentratorUtil;
import team.hnuwt.server.util.RedisUtil;

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
