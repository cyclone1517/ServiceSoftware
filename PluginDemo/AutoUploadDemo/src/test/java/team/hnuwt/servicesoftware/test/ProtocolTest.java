package team.hnuwt.servicesoftware.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.servicesoftware.prtcplugin.model.ListInformation;
import team.hnuwt.servicesoftware.prtcplugin.model.EncodeFormat;
import team.hnuwt.servicesoftware.prtcplugin.packet.PacketAutoUpload;
import team.hnuwt.servicesoftware.prtcplugin.util.ProtocolUtil;

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
        Integer length[] = new Integer[] { 1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 2, 1, 1 };
        EncodeFormat encodeFormat[] = new EncodeFormat[] { EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
                EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
                EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BCD, EncodeFormat.BIN,
                EncodeFormat.BIN, EncodeFormat.BIN };
        ListInformation listInformation[] = new ListInformation[] {
                new ListInformation(9, 13, "Meter", "meter") };
        String pkg = "6881008100688803130100008C60010001070300010000629900D1D002000000000000D1D00300000901D1D0F616";
        ProtocolUtil pu = new ProtocolUtil(fieldName, length, encodeFormat, listInformation);
        PacketAutoUpload p = new PacketAutoUpload();
        //pu.translate(new ByteBuilder(pkg), p, PkgExpUtil.isBulk(id));
        System.out.println(p);
    }

}
