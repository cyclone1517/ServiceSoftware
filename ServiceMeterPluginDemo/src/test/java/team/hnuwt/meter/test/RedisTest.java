package team.hnuwt.meter.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.meter.util.RedisUtil;

public class RedisTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; i++)
            RedisUtil.pushQueue(String.valueOf(i));
    }

}
