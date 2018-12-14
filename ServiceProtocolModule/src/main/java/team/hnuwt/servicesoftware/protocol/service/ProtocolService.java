package team.hnuwt.servicesoftware.protocol.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.plugin.service.PluginService;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.protocol.util.PluginUtil;

public class ProtocolService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ProtocolService.class);

    private ByteBuilder pkg;

    public ProtocolService(String pkg)
    {
        this.pkg = new ByteBuilder(pkg);
    }

    @Override
    public void run()
    {
        if (!check())
        {
            return;
        }
        long pluginId = pkg.BINToLong(12, 13) + (pkg.BINToLong(14, 18) << 8);
        PluginService ps = PluginUtil.getInstance(pluginId);
        ps.translate(pkg);
    }

    private boolean check()
    {
        byte sum = 0;
        int len = pkg.length();
        for (int i = 6; i < len - 2; i++)
        {
            byte b = pkg.getByte(i);
            sum += b;
        }
        return sum == pkg.getByte(len - 2);
    }
}
