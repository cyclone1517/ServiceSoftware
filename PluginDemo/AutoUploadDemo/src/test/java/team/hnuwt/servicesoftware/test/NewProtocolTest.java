package team.hnuwt.servicesoftware.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.prtcplugin.model.EncodeFormat;
import team.hnuwt.servicesoftware.prtcplugin.model.ListInformation;
import team.hnuwt.servicesoftware.prtcplugin.packet.Packet;
import team.hnuwt.servicesoftware.prtcplugin.util.DataUtil;
import team.hnuwt.servicesoftware.prtcplugin.util.PkgExpUtil;
import team.hnuwt.servicesoftware.prtcplugin.util.ProtocolUtil;

/**
 * @author yuanlong Chen
 * @date 19/2/25
 * 测试重构后，具有适配性的PluginServiceImpl
 */
public class NewProtocolTest {

    Logger logger = LoggerFactory.getLogger(NewProtocolTest.class);

    @Test
    public void test() {
        String pkg = "685500550068880000EC03008C60100001070100080000000015FF9816";
        Long id = new Long("30081552524");

        String fieldName[] = PkgExpUtil.getFiledName(id);
        Integer length[] = PkgExpUtil.getFiledLen(id);
        EncodeFormat encodeFormat[] = PkgExpUtil.getFiledCode(id);
        ListInformation listInformation[] = PkgExpUtil.getReptedListInfo(id);

        ProtocolUtil pu = new ProtocolUtil(fieldName, length, encodeFormat, listInformation);
        Packet p = PkgExpUtil.getPacketModel(id);
        if (null != p){
            pu.translate(new ByteBuilder(pkg), p, PkgExpUtil.isBulk(id));
            System.out.println("finished");
        } else {
            logger.info("Such kind of packet is not supported: " + pkg);
        }
    }
}
