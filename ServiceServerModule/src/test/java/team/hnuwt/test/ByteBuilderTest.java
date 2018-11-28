package team.hnuwt.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.util.ByteBuilder;

public class ByteBuilderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test()
    {
        byte[] b = new byte[] { 0x12, 0x34 };
        ByteBuilder bb = new ByteBuilder(b);
        System.out.println(bb.toLong());
        bb = new ByteBuilder("AA1234");
        System.out.println(bb.toLong());
    }

}
