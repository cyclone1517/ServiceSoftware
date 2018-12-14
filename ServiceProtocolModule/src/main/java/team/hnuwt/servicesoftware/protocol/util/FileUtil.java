package team.hnuwt.servicesoftware.protocol.util;

import org.apache.log4j.helpers.FileWatchdog;

public class FileUtil extends FileWatchdog {

    public FileUtil(String filename)
    {
        super(filename);
    }

    @Override
    protected void doOnChange()
    {
        PluginUtil.getPlugin();
    }

}
