package team.hnuwt.servicesoftware.synchronizer.dao;

import org.junit.Test;

import java.util.List;

/**
 *  测试 mybatis 中的方法
 */
public class CheckDaoTest {

    @Test
    public void runCheckDao(){
        List<String> result = new CheckDao().check();
        result.forEach(System.out::println);
    }
}
