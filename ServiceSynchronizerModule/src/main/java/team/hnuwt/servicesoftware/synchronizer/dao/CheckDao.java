package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckDao {

    private static Logger logger = LoggerFactory.getLogger(CheckDao.class);

    public List<String> check(int checkTime)
    {
        int currTime = checkTime;
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        List<String> offList = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            currTime = (checkTime == -1)? Integer.MAX_VALUE : currTime;
            offList = sqlSession.selectList("Check.offline", currTime);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
        return (offList==null)? new ArrayList<>(): offList;
    }
}
