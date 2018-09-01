package team.hnuwt.data.perm;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataProcessThreadPool {

    private static volatile ThreadPoolExecutor executor;


    public DataProcessThreadPool() {
        /**
         * 消息较少时可能出现忙等问题
         * 考虑获得处理器个数
         */
        this.executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(100));
    }

    public ThreadPoolExecutor getDataProcessThreadPool(){
        return executor;
    }
}
