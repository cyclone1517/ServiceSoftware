package team.hnuwt.data.perm;

import team.hnuwt.data.heartBeat.RedisHelper;
import team.hnuwt.data.heartBeat.RedisHelper2;
import team.hnuwt.data.perm.bean.CollectorinfoBean;
import team.hnuwt.data.perm.dao.CollectorinfoDao;
import team.hnuwt.data.perm.impl.CollectorinfoImpl;
import team.hnuwt.tool.PackageTranslateTool;

import java.util.List;

public class DataCarryWorker implements Runnable {

    RedisHelper redisHelper;
    String pkgData;

    public DataCarryWorker(String pkgData){
        this.redisHelper = RedisHelper.getInstance();
        this.pkgData = pkgData;
    }

    @Override
    public void run() {
        List<String> list = RedisHelper2.getJedis().blpop(0, "UpdatedData");
        System.out.println("[DataCarryWorker]UpdatedDataNum: "+list.size());

        PackageTranslateTool ptt = new PackageTranslateTool(pkgData);

        CollectorinfoBean clb = new CollectorinfoBean();
        clb.setCollectorID(ptt.getCollectorID());
        clb.setCollectorType(ptt.getCollectorType());

        CollectorinfoDao cd = new CollectorinfoImpl();
        cd.insertOrUpdateCollectorinfo(clb);
    }
}
