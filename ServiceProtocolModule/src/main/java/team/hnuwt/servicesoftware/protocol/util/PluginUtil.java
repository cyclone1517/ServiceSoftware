package team.hnuwt.servicesoftware.protocol.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.plugin.service.PluginService;
import team.hnuwt.servicesoftware.protocol.model.Plugin;

/**
 * 插件工具类
 */
public class PluginUtil {
    private static Map<Long, String> map;

    private static URLClassLoader urlClassLoader;

    private final static String APPLICATION_FILE = "application.properties";

    private final static String XML_PATH;

    private static Properties props;

    private static Logger logger = LoggerFactory.getLogger(ConsumerUtil.class);

    static {
        try {
            props = new Properties();
            props.load(PluginUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }
        XML_PATH = props.getProperty("plugin.xmlPath");
        getPlugin();
    }

    /**
     * 从XML配置文件中加载插件
     */
    public static void getPlugin()
    {
        map = new HashMap<>();
        List<Plugin> list = new ArrayList<>();

        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new File(XML_PATH));
            Element root = document.getRootElement();
            List<?> plugins = root.elements("plugin");
            for (Object pluginObj : plugins)
            {
                Element pluginEle = (Element) pluginObj;
                Plugin plugin = new Plugin();
                long id = Long.parseLong(pluginEle.elementText("id"));
                plugin.setId(id);
                plugin.setJar(pluginEle.elementText("jar"));
                plugin.setClassName(pluginEle.elementText("class"));
                map.put(id, plugin.getClassName());
                list.add(plugin);
            }

            int num = list.size();
            URL[] urls = new URL[num];
            for (int i = 0; i < num; i++)
            {
                Plugin plugin = list.get(i);
                String filePath = plugin.getJar();
                urls[i] = new URL("file:" + filePath);
            }
            urlClassLoader = new URLClassLoader(urls);
        } catch (DocumentException e) {
            logger.error("", e);
        } catch (MalformedURLException e) {
            logger.error("", e);
        }
    }

    /**
     * 根据插件id找到相应的协议解析类
     * @param id
     * @return
     */
    public static PluginService getInstance(long id)
    {
        PluginService instance = null;
        String className = map.get(id);
        if (className == null)
        {
            return instance;
        }
        try {
            Class<?> clazz = urlClassLoader.loadClass(className);
            instance = (PluginService) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("未找到类，可能原因有:\n" +
                    "1.application.properties或plugin.xml路径未修改\n" +
                    "2.被引用的插件模块PluginDemo/AutoUploadDemo还未用maven编译", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        }
        return instance;
    }
}
