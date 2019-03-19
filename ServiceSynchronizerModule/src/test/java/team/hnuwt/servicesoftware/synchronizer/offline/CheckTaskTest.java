package team.hnuwt.servicesoftware.synchronizer.offline;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import team.hnuwt.servicesoftware.synchronizer.dao.CheckDao;
import team.hnuwt.servicesoftware.synchronizer.offlinecheck.CheckTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class CheckTaskTest {

    @Test
    public void runCheckTask(){
        List<String> offList = new ArrayList<>();
        offList.add("67587777");
        offList.add("43536475");
        String offListStr = JSON.toJSONString(offList);
        System.out.println(offListStr);
    }

    @Test
    public void runTimer() throws InterruptedException {
        Timer timer = new Timer(false);
        timer.schedule(new CheckTask(), 0, 3);
        Thread.sleep(10000);
    }
}
