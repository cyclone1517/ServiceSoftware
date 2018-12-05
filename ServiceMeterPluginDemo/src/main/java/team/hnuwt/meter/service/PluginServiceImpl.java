package team.hnuwt.meter.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import team.hnuwt.meter.model.Data;
import team.hnuwt.meter.model.ListImformation;
import team.hnuwt.meter.model.Meter;
import team.hnuwt.meter.model.Packet;
import team.hnuwt.meter.util.ProtocolUtil;
import team.hnuwt.plugin.service.PluginService;
import team.hnuwt.plugin.util.ByteBuilder;
import team.hnuwt.plugin.util.RedisUtil;

public class PluginServiceImpl implements PluginService {
    private static final String FIELD_NAME[] = new String[] { "control", "address", "afn", "seq", "dataId", "number",
            "id", "data", "valveState" };
    private static final int LENGTH[] = new int[] { 1, 5, 1, 1, 4, 2, 2, 4, 1 };
    private static final ListImformation LIST_IMFORMATION[] = new ListImformation[] {
            new ListImformation(5, 9, "team.hnuwt.meter.model.Meter", "meter") };

    private static final String DATA = "Data";

    @Override
    public void translate(long collectId, ByteBuilder pkg)
    {
        Packet p = new Packet();
        ProtocolUtil pu = new ProtocolUtil(FIELD_NAME, LENGTH, LIST_IMFORMATION);
        pu.translate(pkg, p);
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter())
        {
            Data data = new Data(collectId, meter.getId(), meter.getData(), meter.getValveState());
            datas.add(data);
        }
        RedisUtil.pushQueue(DATA, JSON.toJSONString(datas));
    }
}
