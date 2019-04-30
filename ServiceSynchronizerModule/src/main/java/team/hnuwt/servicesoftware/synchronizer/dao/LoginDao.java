package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginDao {

    private static Logger logger = LoggerFactory.getLogger(LoginDao.class);

    public void insert(Login data)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Login.add", data);
        sqlSession.commit();
        sqlSession.close();
    }

    public void insertBatch(List<Login> datas)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Login.addBatch", datas);
        sqlSession.commit();
        sqlSession.close();

    }

    @Deprecated
    public void resetOnline(List<HeartBeat> datas)
    {
        Set<Long> updateSet = new HashSet<>();
        for (HeartBeat hb: datas){
            updateSet.add(hb.getCollectorId());
        }
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.update("Login.resetOnline", updateSet);
        sqlSession.commit();
        sqlSession.close();
    }
}
