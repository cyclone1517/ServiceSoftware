package team.hnuwt.servicesoftware.autoupload.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.autoupload.model.EncodeFormat;
import team.hnuwt.servicesoftware.autoupload.model.ListImformation;

/**
 * 协议解析工具类
 */
public class ProtocolUtil {
    private String filedName[];
    private int length[];
    private ListImformation listImformation[];
    private int locate, listImformationSize;
    private EncodeFormat encodeFormat[];

    private Logger logger = LoggerFactory.getLogger(ProtocolUtil.class);

    public ProtocolUtil(String filedName[], int length[], EncodeFormat encodeFormat[])
    {
        this.filedName = filedName;
        this.length = length;
        this.encodeFormat = encodeFormat;
        this.locate = 0;
        this.listImformationSize = 0;
    }

    public ProtocolUtil(String filedName[], int length[], EncodeFormat encodeFormat[],
            ListImformation listImformation[])
    {
        this.filedName = filedName;
        this.length = length;
        this.encodeFormat = encodeFormat;
        this.listImformation = listImformation;
        this.locate = 0;
        this.listImformationSize = listImformation.length;
    }

    public void translate(ByteBuilder pkg, Object model)
    {
        translate(0, filedName.length, pkg, model);
    }

    private void translate(int begin, int end, ByteBuilder pkg, Object model)
    {
        Map<String, Object> map = new HashMap<>();
        for (int i = begin, j = 0; i < end; i++)
        {
            long l = (encodeFormat[i] == EncodeFormat.BIN) ? pkg.BINToLong(locate, locate + length[i])
                    : pkg.BCDToLong(locate, locate + length[i]);
            locate += length[i];
            map.put(filedName[i], l);
            while (j < listImformationSize && listImformation[j].getStart() < i)
                j++;
            if (j < listImformationSize && listImformation[j].getStart() == i)
            {
                int number = (int) l;
                try {
                    Class clazz = Class.forName(listImformation[j].getClassName());
                    List<Object> list = new ArrayList<>();
                    for (int k = 0; k < number; k++)
                    {
                        Object o = clazz.newInstance();
                        translate(listImformation[j].getStart() + 1, listImformation[j].getEnd(), pkg, o);
                        list.add(o);
                    }
                    map.put(listImformation[j].getFieldName(), list);
                    i = listImformation[j].getEnd() - 1;
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        ModelUtil.setFieldValue(model, map);
    }
}
