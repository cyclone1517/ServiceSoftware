package team.hnuwt.servicesoftware.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import team.hnuwt.servicesoftware.packet.Packet;
import team.hnuwt.servicesoftware.util.DataUtil;
import team.hnuwt.servicesoftware.util.PkgExpUtil;
import team.hnuwt.servicesoftware.plugin.service.PluginService;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.plugin.util.RedisUtil;
import team.hnuwt.servicesoftware.model.Data;
import team.hnuwt.servicesoftware.model.EncodeFormat;
import team.hnuwt.servicesoftware.model.ListImformation;
import team.hnuwt.servicesoftware.model.Meter;
import team.hnuwt.servicesoftware.packet.PacketAutoUpload;
import team.hnuwt.servicesoftware.util.ProtocolUtil;

/**
 * 协议解析类
 */
public class PluginServiceImpl implements PluginService {
    private Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);

//    private static final String FIELD_NAME[] = new String[]{"firstStartChar", "firstLength", "secondLength",
//            "secondStartChar", "control", "address", "afn", "seq", "dataId", "number", "id", "data", "state", "cs",
//            "endChar"};
//    private static final int LENGTH[] = new int[]{1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 2, 1, 1};
//    private static final ListImformation LIST_IMFORMATION[] = new ListImformation[]{
//            new ListImformation(9, 13, "Meter", "meter")};
//    private static final EncodeFormat ENCODEFORMAT[] = new EncodeFormat[]{EncodeFormat.BIN, EncodeFormat.BIN,
//            EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
//            EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BCD, EncodeFormat.BIN, EncodeFormat.BIN,
//            EncodeFormat.BIN};

    private static final String DATA = "Data";

    @Override
    public void translate(ByteBuilder pkg)
    {
        /* 读取包结构 */
        long id = pkg.BINToLong(12, 13) + (pkg.BINToLong(14, 18) << 8);
        String[] FIELD_NAME = PkgExpUtil.getFiledName(id);
        Integer[] LENGTH = PkgExpUtil.getFiledLen(id);
        EncodeFormat[] ENCODEFORMAT = PkgExpUtil.getFiledCode(id);
        ListImformation[] LIST_IMFORMATION = PkgExpUtil.getReptedListInfo(id);

        //PacketAutoUpload p = new PacketAutoUpload();
        Packet p = PkgExpUtil.getPacketModel(id);       /* 通过反射获取Packet对象模板 */
        ProtocolUtil pu = new ProtocolUtil(FIELD_NAME, LENGTH, ENCODEFORMAT, LIST_IMFORMATION);     /* LIST_IMFORMATION可以为空 */
        pu.translate(pkg, p, PkgExpUtil.isBulk(id));

        DataUtil.putDataToRedis(p);     /* 把数据包装好放入中转Redis库 */
    }
}
