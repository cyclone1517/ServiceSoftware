package team.hnuwt.meter.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.meter.dao.DataDao;
import team.hnuwt.meter.model.Data;
import team.hnuwt.meter.model.ListImformation;
import team.hnuwt.meter.model.Meter;
import team.hnuwt.meter.model.Packet;
import team.hnuwt.meter.util.ProtocolUtil;
import team.hnuwt.plugin.service.PluginService;
import team.hnuwt.plugin.util.ByteBuilder;

public class PluginServiceImpl implements PluginService {
    private static Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);

    private static final String FIELD_NAME[] = new String[] { "control", "address", "afn", "seq", "dataId", "number",
            "id", "data", "valveState" };
    private static final int LENGTH[] = new int[] { 1, 5, 1, 1, 4, 2, 2, 4, 1 };
    private static final ListImformation LIST_IMFORMATION[] = new ListImformation[] {
            new ListImformation(5, 9, "team.hnuwt.meter.model.Meter", "meter") };

    @Override
    public void translate(long collectId, ByteBuilder pkg)
    {
        Packet p = new Packet();
        ProtocolUtil pu = new ProtocolUtil(FIELD_NAME, LENGTH, LIST_IMFORMATION);
        pu.translate(pkg, p);
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter())
        {
            datas.add(new Data(collectId, meter.getId(), meter.getData(), meter.getValveState()));
        }
        new DataDao().insertBatch(datas);
        logger.info("the number of data : " + datas.size());
    }
}
