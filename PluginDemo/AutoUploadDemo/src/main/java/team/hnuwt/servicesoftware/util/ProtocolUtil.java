package team.hnuwt.servicesoftware.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.model.ListInformation;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.model.EncodeFormat;

/**
 * 协议解析工具类
 */
public class ProtocolUtil {
    private String filedName[];
    private Integer length[];
    private ListInformation listInformation[];
    private int locate, listImformationSize;
    private EncodeFormat encodeFormat[];

    private Logger logger = LoggerFactory.getLogger(ProtocolUtil.class);

    public ProtocolUtil(String filedName[], Integer length[], EncodeFormat encodeFormat[])
    {
        this.filedName = filedName;
        this.length = length;
        this.encodeFormat = encodeFormat;
        this.locate = 0;
        this.listImformationSize = 0;
    }

    public ProtocolUtil(String filedName[], Integer length[], EncodeFormat encodeFormat[],
                        ListInformation listInformation[])
    {
        this.filedName = filedName;
        this.length = length;
        this.encodeFormat = encodeFormat;
        this.listInformation = listInformation;
        this.locate = 0;
        this.listImformationSize = listInformation.length;
    }

    public void translate(ByteBuilder pkg, Object model, boolean isBulk)
    {
        if (isBulk) {
            if (listInformation == null) logger.error("No listInformation object injected here!");
            translateBulk(0, filedName.length, pkg, model);
        }
        else {
            translateSingle(0, filedName.length, pkg, model);
        }
    }

    private void translateSingle(int begin, int end, ByteBuilder pkg, Object model)
    {
        Map<String, Object> map = new HashMap<>();
        for (int i = begin; i < end; i++)
        {
            long l = (encodeFormat[i] == EncodeFormat.BIN) ? pkg.BINToLong(locate, locate + length[i])
                    : pkg.BCDToLong(locate, locate + length[i]);    //分解报文的数字，并转换为十进制
            locate += length[i];
            map.put(filedName[i], l);
        }
        ModelUtil.setFieldValue(model, map);
    }

    /**
     * 批量解析函数（数据包含有批量数据）
     * @param begin 用户数据起始下标（字段数为单位）
     * @param end 用户数据终止下标（字段数为单位）
     * @param pkg 报文
     * @param model 数据包实体模型对象
     */
    private void translateBulk(int begin, int end, ByteBuilder pkg, Object model)
    {
        Map<String, Object> map = new HashMap<>();
        for (int i = begin, j = 0; i < end; i++)
        {
            long l = (encodeFormat[i] == EncodeFormat.BIN) ? pkg.BINToLong(locate, locate + length[i])
                    : pkg.BCDToLong(locate, locate + length[i]);    //把报文数据转换为long
            locate += length[i];
            map.put(filedName[i], l);
            while (j < listImformationSize && listInformation[j].getStart() < i /* 有多种重复实体则遍历，如Meter表 */)
                j++;
            if (j < listImformationSize && listInformation[j].getStart() == i /* i从用户数据初始位置才会解析 */)
            {
                int number = (int) l;   /* 从start位置获取批量数据个数 */
                try {
                    Class clazz = Class.forName(listInformation[j].getClassName());     /* 获取批量读取字段的数据实体，如批量读取meterId/ */
                    List<Object> list = new ArrayList<>();
                    for (int k = 0; k < number; k++)    /* 顺序读取多个数据 */
                    {
                        Object o = clazz.newInstance();
                        translateBulk(listInformation[j].getStart() + 1, listInformation[j].getEnd(), pkg, o);
                        list.add(o);
                    }
                    map.put(listInformation[j].getFieldName(), list);   /* 把获取的批量消息存入相关字段 */
                    i = listInformation[j].getEnd() - 1;
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        ModelUtil.setFieldValue(model, map);
    }
}
