package team.hnuwt.plugin.service;

import team.hnuwt.plugin.util.ByteBuilder;

public interface PluginService {
    public void translate(long collectorId, ByteBuilder pkg);
}
