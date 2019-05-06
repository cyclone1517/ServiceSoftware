package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.util.List;

public class DetailDao {

    private static Logger logger = LoggerFactory.getLogger(DetailDao.class);

    public void insertBatch(List<Login> datas)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Detail.loginBatch", datas);
        sqlSession.commit();
        sqlSession.close();
    }

    public void fillLogout(List<Login> datas)
    {
        SqlSession sqlSession = DBUtil.getSqlSession();
        sqlSession.insert("Detail.logoutBatch", datas);
        sqlSession.commit();
        sqlSession.close();
    }
}
