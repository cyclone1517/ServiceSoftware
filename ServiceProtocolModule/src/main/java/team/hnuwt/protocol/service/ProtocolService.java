package team.hnuwt.protocol.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.plugin.service.PluginService;
import team.hnuwt.plugin.util.ByteBuilder;
import team.hnuwt.protocol.util.PluginUtil;

public class ProtocolService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ProtocolService.class);
    
    private ByteBuilder pkg;

    private ByteBuilder mainPkg = new ByteBuilder(); // ”¶”√≤„

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
        long control = pkg.toLong(6, 7);
        long collectId = pkg.toLong(7, 12);
        PluginService ps = PluginUtil.getInstance((int) control);
        ps.translate(collectId, mainPkg);
    }

    private boolean check()
    {
        byte sum = 0;
        int len = pkg.length();
        for (int i = 6; i < len - 2; i++)
        {
            byte b = pkg.getByte(i);
            sum += b;
            mainPkg.append(b);
        }
        return sum == pkg.getByte(len - 2);
    }
}
