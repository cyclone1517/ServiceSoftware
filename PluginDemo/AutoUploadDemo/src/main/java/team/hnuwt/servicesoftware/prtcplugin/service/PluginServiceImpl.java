package team.hnuwt.servicesoftware.prtcplugin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.plugin.service.PluginService;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.prtcplugin.model.EncodeFormat;
import team.hnuwt.servicesoftware.prtcplugin.model.ListInformation;
import team.hnuwt.servicesoftware.prtcplugin.packet.Packet;
import team.hnuwt.servicesoftware.prtcplugin.util.DataUtil;
import team.hnuwt.servicesoftware.prtcplugin.util.PkgExpUtil;
import team.hnuwt.servicesoftware.prtcplugin.util.ProtocolUtil;


/**
 * 协议解析类
 */
public class PluginServiceImpl implements PluginService {
    private Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);

//    private static final String FIELD_NAME[] = new String[]{"firstStartChar", "firstLength", "secondLength",
//            "secondStartChar", "control", "address", "afn", "seq", "dataId", "number", "id", "data", "state", "cs",
//            "endChar"};
//    private static final int LENGTH[] = new int[]{1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 2, 1, 1};
//    private static final ListInformation LIST_IMFORMATION[] = new ListInformation[]{
//            new ListInformation(9, 13, "Meter", "meter")};
//    private static final EncodeFormat ENCODEFORMAT[] = new EncodeFormat[]{EncodeFormat.BIN, EncodeFormat.BIN,
//            EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
//            EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BCD, EncodeFormat.BIN, EncodeFormat.BIN,
//            EncodeFormat.BIN};

    private static final String DATA = "Data";

    @Override
    public void translate(ByteBuilder pkg) {
        /* 读取包结构 AFN+ID 共同决定业务类型 */
        long id = pkg.BINToLong(12, 13) + (pkg.BINToLong(14, 18) << 8);
        if (PkgExpUtil.getBusiName(id)==null){
            logger.warn("NOT SUPPORTED BUSINESS TYPE @#@pkg:" + pkg + " @#@id:" + id);
            return;
        }

        String[] FIELD_NAME = PkgExpUtil.getFiledName(id);
        Integer[] LENGTH = PkgExpUtil.getFiledLen(id);
        EncodeFormat[] ENCODEFORMAT = PkgExpUtil.getFiledCode(id);
        ListInformation[] LIST_IMFORMATION = PkgExpUtil.getReptedListInfo(id);

        Packet p = PkgExpUtil.getPacketModel(id);       /* 通过反射获取Packet对象模板 */
        ProtocolUtil pu = new ProtocolUtil(FIELD_NAME, LENGTH, ENCODEFORMAT, LIST_IMFORMATION);     /* LIST_IMFORMATION可以为空 */
        pu.translate(pkg, p, PkgExpUtil.isBulk(id));    /* 把报文数据解析为明文 */

        DataUtil.putDataToRedis(p);     /* 把数据包装好放入中转Redis库 */
    }
}
