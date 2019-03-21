package team.hnuwt.servicesoftware.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.servicesoftware.server.util.ByteBuilder;

public class ByteBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        byte[] b = new byte[] { (byte) 0x98, 0x76 };
        ByteBuilder bb = new ByteBuilder(b);
        System.out.println(bb.BCDToLong());
        bb = new ByteBuilder("561234");
        System.out.println(bb.BCDToLong());
    }

}
