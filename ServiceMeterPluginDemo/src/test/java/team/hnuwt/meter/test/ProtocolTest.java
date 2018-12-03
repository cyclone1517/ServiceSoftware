package team.hnuwt.meter.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.meter.model.ListImformation;
import team.hnuwt.meter.model.Packet;
import team.hnuwt.meter.util.ProtocolUtil;
import team.hnuwt.plugin.util.ByteBuilder;

public class ProtocolTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String fieldName[] = new String[] { "control", "address", "afn", "seq", "dataId", "number", "id", "data",
                "valveState" };
        int length[] = new int[] { 1, 5, 1, 1, 4, 2, 2, 4, 1 };
        ListImformation listImformation[] = new ListImformation[] {
                new ListImformation(5, 9, "team.hnuwt.meter.model.Meter", "meter") };
        String pkg = "8800000000008C6900000107010000006400000000";
        ProtocolUtil pu = new ProtocolUtil(fieldName, length, listImformation);
        Packet p = new Packet();
        pu.translate(new ByteBuilder(pkg), p);
        System.out.println(p);
    }

}
