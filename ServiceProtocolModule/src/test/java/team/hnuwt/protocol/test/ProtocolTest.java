package team.hnuwt.protocol.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.protocol.model.ListImformation;
import team.hnuwt.protocol.model.Packet;
import team.hnuwt.protocol.util.ProtocolUtil;

public class ProtocolTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test()
    {
        String filedName[] = new String[] { "firstStartChar", "firstLength", "secondLength", "secondStartChar",
                "control", "address", "afn", "seq", "dataId", "cs", "endChar" };
        int length[] = new int[] { 1, 2, 2, 1, 1, 5, 1, 1, 4, 1, 1 };
        String pkg = "6854005400688800000000008C69000001070000000000000000000016";
        ProtocolUtil pu = new ProtocolUtil(filedName, length);
        Packet p = new Packet();
        pu.translate(pkg, p);
        System.out.println(p);
    }

    @Test
    public void testWithListImformation()
    {
        String fieldName[] = new String[] { "firstStartChar", "firstLength", "secondLength", "secondStartChar",
                "control", "address", "afn", "seq", "dataId", "number", "id", "data", "valveState", "cs", "endChar" };
        int length[] = new int[] { 1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 1, 1, 1 };
        ListImformation listImformation[] = new ListImformation[] {
                new ListImformation(9, 13, "team.hnuwt.bean.Meter", "meter") };
        String pkg = "6823002300688803130100008C6900000107" + "0300" + "01000080040000" + "02000000000000"
                + "03000009670000" + "F716";
        ProtocolUtil pu = new ProtocolUtil(fieldName, length, listImformation);
        Packet p = new Packet();
        pu.translate(pkg, p);
        System.out.println(p);
    }

}
