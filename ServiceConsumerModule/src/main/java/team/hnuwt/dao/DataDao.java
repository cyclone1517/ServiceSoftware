package team.hnuwt.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.bean.Meter;
import team.hnuwt.util.DBUtil;

public class DataDao {
    
    private Logger logger = LoggerFactory.getLogger(DataDao.class);
    
    public void insert(Meter meter)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.insert("Data.add", meter);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession!=null) sqlSession.close(); 
        }
    }
    
    public void insertBatch(List<Meter> meters)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.insert("Data.addBatch", meters);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession!=null) sqlSession.close(); 
        }
    }
}
