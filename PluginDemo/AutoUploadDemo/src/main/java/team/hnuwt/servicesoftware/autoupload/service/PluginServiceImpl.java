package team.hnuwt.servicesoftware.autoupload.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import team.hnuwt.servicesoftware.plugin.service.PluginService;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.plugin.util.RedisUtil;
import team.hnuwt.servicesoftware.autoupload.model.Data;
import team.hnuwt.servicesoftware.autoupload.model.EncodeFormat;
import team.hnuwt.servicesoftware.autoupload.model.ListImformation;
import team.hnuwt.servicesoftware.autoupload.model.Meter;
import team.hnuwt.servicesoftware.autoupload.model.Packet;
import team.hnuwt.servicesoftware.autoupload.util.ProtocolUtil;

/**
 * 协议解析类
 */
public class PluginServiceImpl implements PluginService {
    private Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);

    private static final String FIELD_NAME[] = new String[]{"firstStartChar", "firstLength", "secondLength",
            "secondStartChar", "control", "address", "afn", "seq", "dataId", "number", "id", "data", "state", "cs",
            "endChar"};
    private static final int LENGTH[] = new int[]{1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 2, 1, 1};
    private static final ListImformation LIST_IMFORMATION[] = new ListImformation[]{
            new ListImformation(9, 13, "team.hnuwt.servicesoftware.autoupload.model.Meter", "meter")};
    private static final EncodeFormat ENCODEFORMAT[] = new EncodeFormat[]{EncodeFormat.BIN, EncodeFormat.BIN,
            EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN,
            EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BIN, EncodeFormat.BCD, EncodeFormat.BIN, EncodeFormat.BIN,
            EncodeFormat.BIN};

    private static final String DATA = "Data";

    @Override
    public void translate(ByteBuilder pkg)
    {
        Packet p = new Packet();
        ProtocolUtil pu = new ProtocolUtil(FIELD_NAME, LENGTH, ENCODEFORMAT, LIST_IMFORMATION);
        pu.translate(pkg, p);
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter())
        {
            Data data = new Data(p.getAddress(), meter.getId(), meter.getData(), meter.getState());
            datas.add(data);
        }
        RedisUtil.pushQueue(DATA, JSON.toJSONString(datas));
        logger.info("the number of data : " + datas.size());
    }
}
