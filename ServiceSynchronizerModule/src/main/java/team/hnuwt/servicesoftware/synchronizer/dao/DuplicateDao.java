package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.Duplicate;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.util.List;

public class DuplicateDao {

    private static Logger logger = LoggerFactory.getLogger(DuplicateDao.class);

    public void insertBatch(List<Duplicate> datas)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Duplicate.addBatch", datas);
        sqlSession.commit();
        sqlSession.close();
    }

}
