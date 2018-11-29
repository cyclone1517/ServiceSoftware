package team.hnuwt.protocol.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.protocol.model.ListImformation;

/**
 * 协议解析工具类
 */
public class ProtocolUtil {
    private String filedName[];
    private int length[];
    private ListImformation listImformation[];
    private int locate, listImformationSize;

    private Logger logger = LoggerFactory.getLogger(ProtocolUtil.class);

    public ProtocolUtil(String filedName[], int length[])
    {
        this.filedName = filedName;
        this.length = length;
        this.locate = 0;
        this.listImformationSize = 0;
    }

    public ProtocolUtil(String filedName[], int length[], ListImformation listImformation[])
    {
        this.filedName = filedName;
        this.length = length;
        this.listImformation = listImformation;
        this.locate = 0;
        this.listImformationSize = listImformation.length;
    }

    public void translate(String pkg, Object model)
    {
        translate(0, filedName.length, pkg, model);
    }

    private void translate(int begin, int end, String pkg, Object model)
    {
        Map<String, Object> map = new HashMap<>();
        for (int i = begin, j = 0; i < end; i++)
        {
            String s = pkg.substring(locate, locate + length[i] * 2);
            locate += length[i] * 2;
            map.put(filedName[i], s);
            while (j < listImformationSize && listImformation[j].getStart() < i)
                j++;
            if (j < listImformationSize && listImformation[j].getStart() == i)
            {
                int number = StringUtil.toInt(s);
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
