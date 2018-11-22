package team.hnuwt.data.perm;

import team.hnuwt.data.heartBeat.RedisHelper;
import team.hnuwt.data.heartBeat.RedisHelper2;

import java.util.List;

public class DataWorkerManager implements Runnable{

    private boolean runFlag;
    private RedisHelper redisHelper;

    public DataWorkerManager(){
        runFlag = true;
        redisHelper = RedisHelper.getInstance();
    }

    public void closeCarrier(){
        runFlag = false;
    }

    @Override
    public void run() {
        DataProcessThreadPool dptp = new DataProcessThreadPool();
        while (runFlag){
            //dataList是取出来的消息队列
            //List<String> dataList = redisHelper.popUpdatedDataQueueList(10);
            List<String> dataList = RedisHelper2.getJedis().blpop(1, "UpdatedData");
            if (dataList.size() == 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                int length = dataList.size();
                for (int i=1; i<length; i+=2) {
                    System.out.println("[datalist" + i + "]" + dataList.get(i));
                    Thread t = new Thread(new DataCarryWorker(dataList.get(i)));
                    dptp.getDataProcessThreadPool().execute(t);
                }
            }
        }
    }
}
