package team.hnuwt.servicesoftware.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.servicesoftware.server.util.ProduceUtil;

public class ProduceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ProduceUtil.addQueue("12345");
    }

}
