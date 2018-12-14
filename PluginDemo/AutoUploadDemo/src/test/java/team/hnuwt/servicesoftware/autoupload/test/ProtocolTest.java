package team.hnuwt.servicesoftware.autoupload.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.autoupload.model.EncodeFormat;
import team.hnuwt.servicesoftware.autoupload.model.ListImformation;
import team.hnuwt.servicesoftware.autoupload.model.Packet;
import team.hnuwt.servicesoftware.autoupload.util.ProtocolUtil;

public class ProtocolTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String fieldName[] = new String[] { "firstStartChar", "firstLength", "secondLength", "secondStartChar",
                "control", "address", "afn", "seq", "dataId", "number", "id", "data", "state", "cs", "endChar" };
        int length[] = new int[] { 1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 2, 1, 1 };
        EncodeFormat encodeFormat[] = new EncodeFormat[] { EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
                EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
                EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BCD, EncodeFormat.BIN,
                EncodeFormat.BIN, EncodeFormat.BIN };
        ListImformation listImformation[] = new ListImformation[] {
                new ListImformation(9, 13, "team.hnuwt.servicesoftware.autoupload.model.Meter", "meter") };
        String pkg = "6881008100688803130100008C60010001070300010000629900D1D002000000000000D1D00300000901D1D0F616";
        ProtocolUtil pu = new ProtocolUtil(fieldName, length, encodeFormat, listImformation);
        Packet p = new Packet();
        pu.translate(new ByteBuilder(pkg), p);
        System.out.println(p);
    }

}
