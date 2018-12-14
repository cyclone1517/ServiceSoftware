package team.hnuwt.servicesoftware.server.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.servicesoftware.server.util.RedisUtil;

public class RedisTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        RedisUtil.updateHeatBeat("123");
    }

}
