package team.hnuwt.servicesoftware.synchronizer.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

public class DataDao {

    private static Logger logger = LoggerFactory.getLogger(DataDao.class);

    public void insert(Data data)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Data.add", data);
        sqlSession.commit();
        sqlSession.close();
    }

    public void insertBatch(List<Data> datas)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Data.addBatch", datas);
        sqlSession.commit();
        sqlSession.close();
    }
}
