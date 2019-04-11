package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanlong chen
 */
public class CheckDao {

    private static Logger logger = LoggerFactory.getLogger(CheckDao.class);

    /**
     * 定时查询在登录状态却没有心跳的集中器
     * @param checkTime 判断离线的时间范围
     * @return 离线设备ID
     */
    public List<String> check(int checkTime)
    {
        int currTime = checkTime;
        List<String> offList;

        SqlSession sqlSession = DBUtil.getSqlSession();
        currTime = (checkTime == -1)? Integer.MAX_VALUE : currTime;
        offList = sqlSession.selectList("Check.offline", currTime);
        sqlSession.commit();
        sqlSession.close();

        return (offList==null)? new ArrayList<>(): offList;
    }

    /**
     * 将关闭资源的集中器登录状态设为离线
     * @param offlineList 操作成功的离线设备
     * @return
     */
    public void resetOffline(List<String> offlineList)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.update("Check.resetOffline", offlineList);
        sqlSession.commit();
        sqlSession.close();

        logger.info("FINISHED OFFLINE DEVICE RESET");
    }
}
