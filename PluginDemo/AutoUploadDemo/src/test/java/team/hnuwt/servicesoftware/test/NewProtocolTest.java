package team.hnuwt.servicesoftware.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.model.EncodeFormat;
import team.hnuwt.servicesoftware.model.ListInformation;
import team.hnuwt.servicesoftware.packet.Packet;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.util.DataUtil;
import team.hnuwt.servicesoftware.util.PkgExpUtil;
import team.hnuwt.servicesoftware.util.ProtocolUtil;

/**
 * @author yuanlong Chen
 * @date 19/2/25
 * 测试重构后，具有适配性的PluginServiceImpl
 */
public class NewProtocolTest {

    Logger logger = LoggerFactory.getLogger(NewProtocolTest.class);

    @Test
    public void test() {
        Long id = new Long("30081548684");
        //Long id = new Long("67112962");

        String fieldName[] = PkgExpUtil.getFiledName(id);
        Integer length[] = PkgExpUtil.getFiledLen(id);
        EncodeFormat encodeFormat[] = PkgExpUtil.getFiledCode(id);
        ListInformation listInformation[] = new ListInformation[] {
                new ListInformation(9, 13, "team.hnuwt.servicesoftware.model.Meter", "meter") };
        String pkg = "6881008100688803130100008C60010001070300010000629900D1D002000000000000D1D00300000901D1D0F616";
        //String pkg = "683500350068C90313010000027010000400221616";
        ProtocolUtil pu = new ProtocolUtil(fieldName, length, encodeFormat, listInformation);
        Packet p = PkgExpUtil.getPacketModel(id);
        if (null != p){
            pu.translate(new ByteBuilder(pkg), p, PkgExpUtil.isBulk(id));
            DataUtil.putDataToRedis(p);
        } else {
            logger.info("Such kind of packet is not supported: " + pkg);
        }
    }
}
