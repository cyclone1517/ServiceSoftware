package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.io.IOException;
import java.util.List;

public class HeartBeatDao {

    private static Logger logger = LoggerFactory.getLogger(HeartBeatDao.class);

    public void insertBatch(List<HeartBeat> datas)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("HeartBeat.updateRespAndStat", datas);    /* 因为表合并了，所以更新的是登录表 */
        sqlSession.commit();
        sqlSession.close();
    }

}
